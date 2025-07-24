pipeline {
    agent any

    environment {
        SSH_VM = 'iheb@192.168.150.129'
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
                bat 'ssh -o StrictHostKeyChecking=no iheb@192.168.150.129 "mkdir -p /home/iheb/ToDo"'
                bat 'scp -o StrictHostKeyChecking=no target\\ToDo-0.0.1-SNAPSHOT.jar iheb@192.168.150.129:/home/iheb/ToDo/'
                bat 'scp -o StrictHostKeyChecking=no docker-compose.yml iheb@192.168.150.129:/home/iheb/ToDo/'
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
