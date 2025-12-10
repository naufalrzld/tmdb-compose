pipeline {
    agent any

    environment {
        ANDROID_HOME = "/opt/android-sdk"
        ANDROID_SDK_ROOT = "/opt/android-sdk"
        JAVA_HOME = "/opt/java/openjdk"
        PATH = "/opt/android-sdk/platform-tools:/opt/android-sdk/cmdline-tools/latest/bin:/opt/android-sdk/emulator:$PATH"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Gradle Clean') {
            steps {
                sh "./gradlew clean --warning-mode=all"
            }
        }

        stage('Assemble Debug') {
            steps {
                sh "./gradlew assembleDebug --warning-mode=all"
            }
        }

        stage('List Build Output') {
            steps {
                sh "ls -R app/build/outputs"
            }
        }

        stage('Archive APK/AAB') {
            steps {
                archiveArtifacts artifacts: 'app/build/outputs/**', fingerprint: true
            }
        }
    }

    post {
        failure {
            echo "❌ Build gagal!"
        }
        success {
            echo "🎉 Build berhasil!"
        }
    }
}
