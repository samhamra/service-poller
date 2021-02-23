HOW TO RUN:    
If you have docker and docker-compose installed, run from project root   
docker-compose up

If not:  

1. MySQL 5.7
Run your own MySQL instance and run server/init.sql. 
Also configure /server/src/main/java/service/poller/App.java
Input your config to the constructor of MySqlServiceModel
ServiceModel model = new MySqlServiceModel("database", 3306, "dev", "root", "secret");


2. JDK8 and Gradle
From server/ run:  
./gradlew run

3. NPM  & ReactJS

From client/ run:  
npm install && npm start
