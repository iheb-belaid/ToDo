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
            echo '--------Le pipeline CI/CD s\'est terminé avec succès.---------------------'
        }
        failure {
            echo '-----------------------------Le pipeline a échoué. Vérifie les étapes ci-dessus pour localiser l\'erreur.------------------------------'
        }
    }
}
