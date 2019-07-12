
#Prerecuisites

 * Mysql 5
 * JDK 8
 * Maven (needed only to package project into jar file)

#Preparation to run

 * create database with this command
    CREATE DATABASE secure_pof CHARACTER SET utf8 COLLATE utf8_general_ci;

 * edit connection properties:
    spring.datasource.url=jdbc:mysql://localhost:3306/secure_pof?useUnicode=yes&amp;amp;characterEncoding=UTF-8&amp;amp;useSSL=true&amp;amp;verifyServerCertificate=false
    spring.datasource.username=username
    spring.datasource.password=pass


 * to prepare package run in root project folder this command:
    mvn clean package

    (after packaging should appear this file ./target/securePOF-0.5-SNAPSHOT.jar)

 * run application either way:

  -  java -jar /target/securePOF-0.5-SNAPSHOT.jar

  -  mvn spring-boot:run



```

On first run database is populated with following credentials:
    admin:12345  - role ADMIN
    user1:12345  - role USER
    user2:12345  - role USER
    frodo:12345  - no role assigned


# Check fields are nullified for different roles:
Use same endpoint for check:  http://localhost:8081/rest/dishes
This will return list of available dishes for users in roles ADMIN or USER
For ADMIN all fields should be present, for USER many will be nullified

ADMIN:
curl -u admin:12345 http://localhost:8081/rest/dishes
(should get dishes with all fields)

USER:
curl -u user1:12345 http://localhost:8081/rest/dishes
(should get dishes with nullified fields)

someone without role (same as anonymous)
curl -u user1:12345 http://localhost:8081/rest/dishes
(should get unauthorized error)







