pipeline {
    agent any

    environment {
        APP_NAME = 'BooksApi'
        MAVEN_OPTS = "-Dmaven.test.failure.ignore=false"
    }
    
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/ydskumar/BookApi.git', branch: 'master'
            }
        }
        
        stage('Build & Test') {
            steps {
                sh 'mvn clean test'
            }
        }
        
        stage('Publish Test Results') {
            steps {
                junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
        
        stage('Checkout booksapitests') {
            steps {
                dir('booksapitests') {
                  git url: 'https://github.com/ydskumar/BooksApiTests.git'
                }
            }
        }
        
        stage('Run Integration Tests') {
            steps {            
                sh 'java -jar booksapi.jar --server.port=8081 & sleep 10'
                
                dir('booksapitests') {
                    sh 'mvn test' 
                }
            }
        }

    }
    
}
