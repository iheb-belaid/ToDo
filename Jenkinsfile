pipeline {
    agent any

    environment {
        SSH_VM = 'iheb@192.168.4.30'
        PROJECT_DIR = '/home/iheb/ToDo'
        JAR_NAME = 'ToDo-0.0.1-SNAPSHOT.jar'
    }

    stages {
        stage('Clone Repository') {
            steps {
                echo '-----Étape 1: Clonage du dépôt Git...----------'
                git url: 'https://github.com/iheb-belaid/ToDo.git', branch: 'main'
                echo '------Dépôt cloné avec succès.-----------'
            }
        }

        stage('Build Jar') {
            steps {
                echo '------------- Étape 2: Compilation de l\'application...------------'
                bat 'mvn clean package -DskipTests'
                echo '----------- Fichier .jar généré avec succès.---------------'
            }
        }

        stage('Copy Files to VM') {
            steps {
                echo '📤 Étape 3: Transfert des fichiers vers la VM...'
                bat """
                    ssh -o StrictHostKeyChecking=no iheb@192.168.4.30 "mkdir -p /home/iheb/ToDo/target"
                    scp -o StrictHostKeyChecking=no target\\ToDo-0.0.1-SNAPSHOT.jar iheb@192.168.4.30:/home/iheb/ToDo/target/
                    scp -o StrictHostKeyChecking=no docker-compose.yml iheb@192.168.4.30:/home/iheb/ToDo/
                    scp -o StrictHostKeyChecking=no Dockerfile iheb@192.168.4.30:/home/iheb/ToDo/
                """
            }
        }



        stage('Deploy on VM') {
            steps {
                echo ' Étape 4: Déploiement avec Docker Compose sur la VM...'
                bat """
                ssh %SSH_VM% ^
                    "cd %PROJECT_DIR% && docker compose down && docker compose build && docker compose up -d"
                """
                echo ' ----Déploiement terminé. !!!!!!'
            }
        }
    }

   post {
       success {
           echo '✅ Déploiement terminé avec succès !'
           emailext(
               to: 'ihebbelaid3@gmail.com',
               subject: "✅ Succès - Déploiement de ${env.JOB_NAME}",
               body: "Le pipeline Jenkins s'est terminé avec succès pour le projet ${env.JOB_NAME}.<br>Build: ${env.BUILD_URL}"
           )
       }
       failure {
           echo '❌ Erreur lors du pipeline.'
           emailext(
               to: 'ihebbelaid3@gmail.com',
               subject: "❌ Échec - Pipeline ${env.JOB_NAME}",
               body: "Le pipeline Jenkins a échoué pour le projet ${env.JOB_NAME}.<br>Vérifiez les logs ici : ${env.BUILD_URL}"
           )
       }
   }
}
