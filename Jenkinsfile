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
               docker.build("*.*.*.*:*/dispute_management_backend.git:${TAG}")
           }
        }
    }
    stage("Push Docker Image to Local Registry"){
        steps{
           script{
               docker.withRegistry("http://*.*.*.*:*"){
                   docker.image("*.*.*.*:*/dispute_management_backend.git:${TAG}").push()
                   docker.image("*.*.*.*:*/dispute_management_backend.git:${TAG}").push("latest")
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
                sh 'ssh -o StrictHostKeyChecking=no -l  test *.*.*.*    "docker stop dispute-api | true;     docker rm dispute-api | true;     docker run -v /mnt/dispute:/var/storage -p 9000:8080 -d --restart=always --name dispute-api *.*.*.*:*/dispute_management_backend.git:latest"'
            }
        }
    }

      stage("Deploy for production"){
        when{
            branch "main"
        }
        steps{
             sshagent(['enat-remedy-production']) {
                    sh 'ssh -o StrictHostKeyChecking=no -l  administrator *.*.*.*      "docker stop dispute-api | true;     docker rm dispute-api | true;     docker run -v /mnt/dispute:/var/storage -p 9000:8080 -e "SPRING_PROFILES_ACTIVE=prod" -d --restart=always --name dispute-api *.*.*.*:*/dispute_management_backend.git:${TAG}"'
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
