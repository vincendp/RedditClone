# RedditClone

A Reddit like clone built using Angular, Spring Boot, Hibernate, and MySQL. It makes use of Angular features such as lazy loading to delay loading modules until necessary, Spring Boot Rest Controller to handle API calls, Spring Security to handle basic authentication using cookies and JWS, and Hibernate for object relational mapping. This app uses JUnit 5 to test for Java and Spring code.

## Prerequisites

npm https://www.npmjs.com/get-npm \
angular cli https://cli.angular.io/ \
mysql https://dev.mysql.com/downloads/mysql/ 

## Installing and Running

```
git clone https://github.com/vincendp/RedditClone.git
```

### Client 

**Step 1:**
Open a new terminal

**Step 2:**
```
cd /RedditClone/client
```

**Step 3:**
```
npm install
```

**Step 4:**
```
ng serve
```

### Database

**Step 5:**
Start MySQL server, setup database, and run `redditdb.sql` and then `data.sql` SQL scripts under RedditClone/server/src/main/resources/sql/

### Server

**Step 6:**
Edit `application.properties` at RedditClone/server/src/main/resources/ to match your database. 
  + Edit **spring.datasource.url** where it says **YOURDBHERE** and replace with your database name 
  + Edit **spring.datasource.username** where it says **YOURUSERNAMEHERE** and replace with your database username 
  + Edit **spring.datasource.password** where it says **YOURPASSWORDHERE** and replace with your database password 

**Step 7:**
Open another new terminal

**Step 8:**
```
cd /RedditClone/server
```

**Step 9:**
```
./mvnw spring-boot:run 
```

**Step 10:**
Go to your browser at localhost:4200/ to view app


## Testing

**Step 1:**
To run Java unit tests and integration tests, open up a terminal

**Step 2:**
```
cd /RedditClone/server
```

**Step 3:**
```
./mvnw test
```
