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
                echo '📥 Étape 1: Clonage du dépôt Git...'
                git url: 'https://github.com/iheb-belaid/ToDo.git', branch: 'main'
                echo '✅ Dépôt cloné avec succès.'
            }
        }

        stage('Build Jar') {
            steps {
                echo '🏗️ Étape 2: Compilation de l\'application...'
                bat 'mvn clean package -DskipTests'
                echo '✅ Fichier .jar généré avec succès.'
            }
        }

        stage('Copy Files to VM') {
            steps {
                echo '📤 Étape 3: Transfert du fichier .jar vers la VM...'
                bat """
                dir target
                echo Transfert du JAR...
                scp -v -o StrictHostKeyChecking=no target\\%JAR_NAME% %SSH_VM%:%PROJECT_DIR%/
                """
                echo '✅ .jar transféré.'

                echo '📤 Transfert du fichier docker-compose.yml vers la VM...'
                bat """
                dir
                echo Transfert du docker-compose.yml...
                scp -v -o StrictHostKeyChecking=no docker-compose.yml %SSH_VM%:%PROJECT_DIR%/
                """
                echo '✅ docker-compose.yml transféré.'
            }
        }

        stage('Deploy on VM') {
            steps {
                echo '🚀 Étape 4: Déploiement avec Docker Compose sur la VM...'
                bat """
                ssh %SSH_VM% ^
                    "cd %PROJECT_DIR% && docker compose down && docker compose build && docker compose up -d"
                """
                echo '✅ Déploiement terminé.'
            }
        }
    }

    post {
        success {
            echo '🎉 Le pipeline CI/CD s\'est terminé avec succès.'
        }
        failure {
            echo '❌ Le pipeline a échoué. Vérifie les étapes ci-dessus pour localiser l\'erreur.'
        }
    }
}
