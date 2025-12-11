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

        stage('Preparation') {
            steps {
                withCredentials([
                    file(credentialsId: 'google-services', variable: 'GS_FILE'),
                    file(credentialsId: 'local-properties', variable: 'LOCAL_PROPS_FILE')
                ]) {
                    sh """
                        echo "Copying google-services.json into app/"
                        cp "$GS_FILE" app/google-services.json
                    """
                    sh """
                        echo "Copying local.properties into project root"
                        cp "$LOCAL_PROPS_FILE" local.properties
                    """
                }
                sh "chmod +x gradlew"
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

        stage('Deploy to Firebase App Distribution') {
            steps {
                script {
                    def userInput = timeout(time: 30, unit: 'SECONDS') {
                        input(
                            id: 'userInputStep',
                            message: 'Input Groups and Release Notes for Firebase App Distribution',
                            parameters: [
                                string(name: 'GROUPS', defaultValue: '', description: 'Group name of testers for Firebase App Distribution (use alias)'),
                                text(name: 'RELEASE_NOTES', defaultValue: 'Update build from Jenkins CI/CD', description: 'Release notes')
                            ]
                        )
                    }

                    def extraArgs = ""

                    if (userInput.GROUPS?.trim()) {
                        extraArgs += "--groups='${userInput.GROUPS}'"
                    }

                    if (userInput.RELEASE_NOTES?.trim()) {
                        extraArgs += " --releaseNotes='${userInput.RELEASE_NOTES}'"
                    }

                    echo "Firebase Distribution Args: ${extraArgs}"

                    withCredentials([file(credentialsId: 'firebase-service-account', variable: 'FIREBASE_SERVICE_ACCOUNT')]) {
                        sh """
                            export FIREBASE_APP_DISTRIBUTION_SERVICE_CREDENTIALS="$FIREBASE_SERVICE_ACCOUNT"

                            ./gradlew appDistributionUploadDebug $extraArgs --warning-mode=all
                        """
                    }
                }
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
