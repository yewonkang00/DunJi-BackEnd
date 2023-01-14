pipeline {
    agent any

//     triggers {
//         pollSCM('*/3 * * * *')
//     }

    environment {
        imagename = "dungzi/backend"
        registryCredential = 'docker-hub-login'
        dockerImage = ''
    }

    stages {
        // git에서 repository clone
        stage('Prepare') {
          steps {
            echo 'Clonning Repository'
            git url: 'https://github.com/Nestsoft-Team/DunJi-BackEnd.git',
              branch: 'aws_setting',
              credentialsId: 'dungzi-backend-token'
            }
            post {
             success {
               echo 'Successfully Cloned Repository'
             }
           	 failure {
               error 'This pipeline stops here...'
             }
          }
        }

        // gradle build
        stage('Bulid Gradle') {
          agent any
          steps {
            echo 'Bulid Gradle'
            dir ('.'){
                sh """
                chmod +x ./gradlew
                ./gradlew clean build --exclude-task test
                """
            }
          }
          post {
            failure {
              error 'This pipeline stops here...'
            }
          }
        }

        // docker build
        stage('Bulid Docker') {
          agent any
          steps {
            echo 'Bulid Docker'
            script {
                dockerImage = docker.build imagename
            }
          }
          post {
            failure {
              error 'This pipeline stops here...'
            }
          }
        }

        // docker push
        stage('Push Docker') {
          agent any
          steps {
            echo 'Push Docker'
            script {
                docker.withRegistry( '', registryCredential) {
                    dockerImage.push("1.0")  // ex) "1.0"
                }
            }
          }
          post {
            failure {
              error 'This pipeline stops here...'
            }
          }
        }

        //docker pull and 배포
//         stage('SSH SERVER EC2') {
//           steps {
//             echo 'SSH'
//
//             sshagent(['ec2-user']) {
//                 sh 'ssh -o StrictHostKeyChecking=no ec2-user@172.31.39.4 "whoami"'
//                 sh "ssh -o StrictHostKeyChecking=no ec2-user@172.31.39.4 'cd /home/ec2-user/docker-compose/dungzi-backend'"
//                 sh "ssh -o StrictHostKeyChecking=no ec2-user@172.31.39.4 'docker-compose down'"
//                 sh "ssh -o StrictHostKeyChecking=no ec2-user@172.31.39.4 'docker-compose down'"
//                 sh "ssh -o StrictHostKeyChecking=no ec2-user@172.31.39.4 'docker-compose pull'"
//                 sh "ssh -o StrictHostKeyChecking=no ec2-user@172.31.39.4 'docker-compose up -d'"
//             }
//           }
//        }
    }
}