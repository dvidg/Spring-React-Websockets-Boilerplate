start cmd /k call runNpm.bat
start cmd /k call openWebsite.bat
cd backend
mvn package && java -jar target/gs-spring-boot-0.1.0.jar

