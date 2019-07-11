 CREATE DATABASE secure_pof CHARACTER SET utf8 COLLATE utf8_general_ci;


#Voting System
```
Application is configured to work with H2 inmemory database (so no configuration is needed).
Configuarations for PostgresSQL and HSQL can be found in applicaiton.properties.

To run application in Tomcat Embeded mode:
mvn spring-boot:run

On first run database is populated with following credentials:
    admin:12345  - role ADMIN
    user1:12345  - role USER
    user2:12345  - role USER
    frodo:12345  - no role assigned

To populate database with initial test data run one of tests in DataTests.java like this
(remember spring-boot H2 requires exclusive access to its database file):

mvn -DskipTests=false -Dtest=DataTests#testEncode test


To package application as war file deployable to App server/container:
mvn package
```

#Curl commands for testing API:
(json files for curl located in root of the project for convinience)


* Restaurants
```
list of restaurants\n
curl -u admin:12345 http://localhost:8081/rest/restaurants

add restaurant
curl -u admin:12345  -H  "Content-Type: application/json" -X POST -d  @restaurant1.json  http://localhost:8081/rest/restaurants

delete restaurant (if it got no references)
curl -u admin:12345 -X DELETE http://localhost:8081/rest/restaurants/{RESTAURANT_ID}
```

* Dishes
```
list of dishes
curl -u admin:12345 http://localhost:8081/rest/dishes

add dish (file dish1.json should exist in directory where curl is running)*
curl -u admin:12345  -H  "Content-Type: application/json" -X PUT -d   @dish1.json  http://localhost:8081/rest/dish/add_dish_rest/{RESTAURANT_ID}

list of dishes by restaurant id
curl -u admin:12345 http://localhost:8081/rest/dish/dishes/{RESTAURANT_ID}

delete dish
curl -u admin:12345  -X DELETE http://localhost:8081/rest/dishes/{DISH_ID}
```

* Menus
```
list of today menus:
curl -u admin:12345 http://localhost:8081/rest/menues

add menu (file menu1.json should exist in directory where curl is running)
curl -u admin:12345  -H  "Content-Type: application/json" -X POST -d   @menu1.json  http://localhost:8081/rest/menues/{RESTAURANT_ID}

activate menu
curl -u admin:12345 -X PUT http://localhost:8081/rest/menues/{MENU_ID}?activate=true1

deactivate menu
curl -u admin:12345 -X PUT http://localhost:8081/rest/menues/{MENU_ID}?activate=true

delete menu
curl -u admin:12345  -X DELETE http://localhost:8081/rest/menues/{MENU_ID}

```

* Votes
```
list of today votes:
curl -u user1:12345 http://localhost:8081/rest/today_votes

vote for restaurant
curl -u user1:12345 http://localhost:8081/rest/vote/{RESTAURANT_ID}
```

#P.S.
Better to run JPA applications on normal databases like Postgres or MySQL.
Ways to connect to configured java in-memory databases in console:

```
H2 connect (jdbc:h2:file://{DIRECTORY_OF_PROJECT}/h2_skd_db)
java -cp %LOCATION_OF_MAVEN_REPO%\.m2\repository\com\h2database\h2\1.4.190\h2-1.4.190.jar  org.h2.tools.Console

HSQL connect
java -cp %LOCATION_OF_MAVEN_REPO%\.m2\repository\org\hsqldb\hsqldb\2.3.2\hsqldb-2.3.2.jar  org.hsqldb.util.DatabaseManagerSwing

```