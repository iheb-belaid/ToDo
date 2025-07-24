pipeline {
    agent any

    environment {
        SSH_VM = 'iheb@192.168.150.129'
        PROJECT_DIR = '/home/iheb/ToDo'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Clone Repository') {
            steps {
                git url: 'https://github.com/iheb-belaid/ToDo.git', branch: 'main'
            }
        }

        stage('Build Jar') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Copy Files to VM') {
            steps {
                sh """
                ssh -o StrictHostKeyChecking=no $SSH_VM "mkdir -p $PROJECT_DIR"
                scp -o StrictHostKeyChecking=no target/*.jar $SSH_VM:$PROJECT_DIR/
                scp -o StrictHostKeyChecking=no docker-compose.yml $SSH_VM:$PROJECT_DIR/
                """
            }
        }

        stage('Deploy on VM') {
            steps {
                sh """
                ssh $SSH_VM << EOF
                  cd $PROJECT_DIR
                  docker compose down
                  docker compose build
                  docker compose up -d
                EOF
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
