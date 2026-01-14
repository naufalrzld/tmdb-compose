pipeline {
    agent { label 'android-36-agent' }

    parameters {
        string(
            name: 'APP_DISTRIBUTION_GROUPS',
            defaultValue: ' qa-team',
            description: 'App Distribution Testers & Groups (comma separated)'
        )
        text(
            name: 'APP_DISTRIBUTION_RELEASE_NOTES',
            defaultValue: 'Update build from Jenkins CI/CD',
            description: 'Release notes'
        )
        booleanParam(
            name: 'CLEAN_BUILD',
            defaultValue: false,
            description: 'Gradle Clean?'
        )
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
                script {
                    if (params.CLEAN_BUILD) {
                        echo "Running Gradle Clean..."
                        sh "./gradlew clean --warning-mode=all"
                    } else {
                        echo "Skipping Gradle Clean."
                    }
                }
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
                    def extraArgs = ""

                    if (params.APP_DISTRIBUTION_GROUPS.trim()) {
                        extraArgs += "--groups='${params.APP_DISTRIBUTION_GROUPS}'"
                    }

                    if (params.APP_DISTRIBUTION_RELEASE_NOTES.trim()) {
                        extraArgs += " --releaseNotes='${params.APP_DISTRIBUTION_RELEASE_NOTES}'"
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
