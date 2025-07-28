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
                bat 'ssh -o StrictHostKeyChecking=no %SSH_VM% "mkdir -p %PROJECT_DIR%"'
                bat 'scp -o StrictHostKeyChecking=no target\\ToDo-0.0.1-SNAPSHOT.jar %SSH_VM%:%PROJECT_DIR%/'
                bat 'scp -o StrictHostKeyChecking=no docker-compose.yml %SSH_VM%:%PROJECT_DIR%/'
            }
        }

        stage('Test SSH Connection') {
            steps {
                bat 'ssh -o StrictHostKeyChecking=no %SSH_VM% "echo ✅ Connexion SSH OK depuis Jenkins"'
            }
        }

        stage('Deploy on VM') {
            steps {
                bat 'ssh -o StrictHostKeyChecking=no %SSH_VM% "cd %PROJECT_DIR% && docker compose down"'
                bat 'ssh -o StrictHostKeyChecking=no %SSH_VM% "cd %PROJECT_DIR% && docker compose build"'
                bat 'ssh -o StrictHostKeyChecking=no %SSH_VM% "cd %PROJECT_DIR% && docker compose up -d"'
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
