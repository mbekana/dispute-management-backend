pipeline{

environment{
    DATE=new Date().format('yy.M')
    TAG = "${DATE}.${BUILD_NUMBER}"

}
tools{
    maven 'maven'
}
agent any
stages{
    stage("Build"){
        steps{
            sh "mvn -version"
            sh "mvn clean install -DskipTests"
        }
    }

     stage("Build Docker"){
        steps{
           script{
               docker.build("10.1.12.73:5000/dispute_management_backend.git:${TAG}")
           }
        }
    }
    stage("Push Docker Image to Local Registry"){
        steps{
           script{
               docker.withRegistry("http://10.1.12.73:5000"){
                   docker.image("10.1.12.73:5000/dispute_management_backend.git:${TAG}").push()
                   docker.image("10.1.12.73:5000/dispute_management_backend.git:${TAG}").push("latest")
               }
           }
        }
    }
    stage("Deliver for development"){
        when{
            branch "develop"
        }
        steps{
            sshagent(['ebdev']) {
                sh 'ssh -o StrictHostKeyChecking=no -l  ebdevuat 10.1.22.72      "docker stop dispute-api | true;     docker rm dispute-api | true;     docker run -v /mnt/dispute:/var/storage -p 9000:8080 -d --restart=always --name dispute-api 10.1.12.73:5000/dispute_management_backend.git:latest"'
            }
        }
    }

      stage("Deploy for production"){
        when{
            branch "main"
        }
        steps{
             sshagent(['enat-remedy-production']) {
                    sh 'ssh -o StrictHostKeyChecking=no -l  administrator 10.1.12.70      "docker stop dispute-api | true;     docker rm dispute-api | true;     docker run -v /mnt/dispute:/var/storage -p 9000:8080 -e "SPRING_PROFILES_ACTIVE=prod" -d --restart=always --name dispute-api 10.1.12.73:5000/dispute_management_backend.git:${TAG}"'
                }


        }
    }
}

post{
    always{
        cleanWs()
    }
    failure {
        sh """
        curl -X POST -H "Content-Type: application/json" -d '{"value1":"${JOB_NAME}","value2":"${BUILD_NUMBER}","value3":"Failed"}' https://maker.ifttt.com/trigger/Build_Notification/with/key/c9HE9K84X22YKOKsCiNivz
        """
    }
    success {
        sh """
        curl -X POST -H "Content-Type: application/json" -d '{"value1":"${JOB_NAME}","value2":"${BUILD_NUMBER}","value3":"Successful"}' https://maker.ifttt.com/trigger/Build_Notification/with/key/c9HE9K84X22YKOKsCiNivz
        """
    }

}
}
