pipeline {
  agent {
    docker {
      image 'maven:3.9-eclipse-temurin-21'   // DockerHub official image
      args '-v /root/.m2:/root/.m2'          // Optional: cache Maven repo
    }
  }

  parameters {
    booleanParam(name: 'IS_RELEASE', defaultValue: false, description: 'Is this a release build?')
  }

  environment {
    APP_NAME = 'BooksApi'
    MAVEN_OPTS = "-Dmaven.test.failure.ignore=false"
    NEXUS_USER = credentials('nexus-creds')
    NEXUS_PASS = credentials('nexus-creds')
  }

  stages {

    stage('Set Version') {
      steps {
        script {
          def baseVersion = "1.0"
          if (params.IS_RELEASE) {
            env.REVISION = "${baseVersion}.${BUILD_NUMBER}"
          } else {
            env.REVISION = "${baseVersion}.${BUILD_NUMBER}-SNAPSHOT"
          }
          echo "Using version: ${env.REVISION}"
        }
      }
    }

    stage('Checkout') {
      steps {
        git url: 'https://github.com/ydskumar/BookApi.git', branch: 'master'
      }
    }

    stage('Build & Unit Test') {
      steps {
        configFileProvider([configFile(fileId: 'maven-settings-with-nexus', variable: 'MAVEN_SETTINGS')]) {
          sh 'mvn -s $MAVEN_SETTINGS clean test -Drevision=${REVISION}'
        }
      }
    }

    stage('Publish Test Results') {
      steps {
        junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
      }
    }

    stage('Package') {
      steps {
        configFileProvider([configFile(fileId: 'maven-settings-with-nexus', variable: 'MAVEN_SETTINGS')]) {
          sh 'mvn -s $MAVEN_SETTINGS package spring-boot:repackage -DskipTests -Drevision=${REVISION}'
        }
      }
    }

    stage('Deploy to Nexus') {
      steps {
        configFileProvider([configFile(fileId: 'maven-settings-with-nexus', variable: 'MAVEN_SETTINGS')]) {
          sh 'mvn -s $MAVEN_SETTINGS deploy -DskipTests -Drevision=${REVISION}'
        }
      }
    }
  } // closes stages block

  post {
    success {
      script {
        if (params.IS_RELEASE) {
          echo "Release ${env.REVISION} deployed"
        } else {
          echo "Snapshot ${env.REVISION} deployed"
        }
      }
    }
    failure {
      echo "Build or deployment failed for version: ${env.REVISION}"
    }
  }
}
