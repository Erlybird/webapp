# webapp
This repo is used for Network Structures and Cloud Computing Course Assignments CSYE6225-Fall2023.

## project info
Basic spring-boot project with maven and swagger docs setup. Added Basic auth settings to the project.

## steps to run this project
1. git clone https://github.com/Erlybird/webapp.git
2. Install maven in your system and java 17 version JDK.
3. change to project directory in terminal and run the below commands.
4. mvn clean install
5. mvn spring-boot:run
6. check the logs for the application url.
7. Default port is 8080 and runs in localhost.
8. press command+c to stop the server.
9. if you wish not to install maven.You can run following commands on respective OS.
10. ./mvnw spring-boot:run  (unix)
11. ./mvnw.cmd spring-boot:run  (windows)

## urls
1. http://localhost:8080/healthz (health check api)
2. http://localhost:8080/v1/assignments (POST -- create assignment)
3. http://localhost:8080/v1/assignments (GET -- get all assignments of a user)
4. http://localhost:8080/v1/assignments/{id} (GET -- get the assignment with this id)
5. http://localhost:8080/v1/assignments/{id} (DELETE -- delete the assignment with this id)
6. http://localhost:8080/v1/assignments/{id} (PUT -- update the assignment with this id)