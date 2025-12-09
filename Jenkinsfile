pipeline {
    agent {
        docker {
            image 'android-builder'
            args '-v /var/jenkins_home/.gradle:/root/.gradle'
        }
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare Gradle') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew --version'
            }
        }

        stage('Build Debug') {
            steps {
                sh './gradlew clean assembleDebug --stacktrace'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew testDebugUnitTest --stacktrace'
            }
        }

    }

    post {
        success {
            echo "Build OK — Archiving artifacts"
            archiveArtifacts artifacts: '**/*.apk', fingerprint: true
        }
        failure {
            echo "Build Failed"
        }
    }
}
