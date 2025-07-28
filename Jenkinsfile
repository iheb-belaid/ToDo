pipeline {
    agent any

    environment {
        SSH_VM = 'iheb@192.168.4.30'
        PROJECT_DIR = '/home/iheb/ToDo'
    }

    stages {

        stage('Clone Repository') {
            steps {
                git url: 'https://github.com/iheb-belaid/ToDo.git', branch: 'main'
            }
        }

        stage('Build Jar') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Copy Files to VM') {
            steps {
                // Créer le dossier cible
                bat 'ssh -o StrictHostKeyChecking=no %SSH_VM% "mkdir -p %PROJECT_DIR%"'

                // Copier le fichier JAR sans scp
                bat '''
                type target\\ToDo-0.0.1-SNAPSHOT.jar | ssh -o StrictHostKeyChecking=no %SSH_VM% "cat > %PROJECT_DIR%/ToDo-0.0.1-SNAPSHOT.jar"
                '''

                // Copier le fichier docker-compose.yml
                bat '''
                type docker-compose.yml | ssh -o StrictHostKeyChecking=no %SSH_VM% "cat > %PROJECT_DIR%/docker-compose.yml"
                '''
            }
        }

        stage('Deploy on VM') {
            steps {
                bat """
                ssh %SSH_VM% ^
                  "cd %PROJECT_DIR% && docker compose down && docker compose build && docker compose up -d"
                """
            }
        }
    }

    post {
        success {
            echo '✅ Déploiement terminé avec succès !'
        }
        failure {
            echo '❌ Erreur lors du pipeline.'
        }
    }
}
