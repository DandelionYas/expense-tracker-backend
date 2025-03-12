# Expense Tracker Backend

Expense tracker is a simple interface you can use to add and categorize your expenses. 
Generate monthly reports based on the inputs and write custom alerts for things, like, “spending too much money on coffee… as always.”

## Architecture

Microservices architecture has been chosen as a good fit for this project. The requirement implemented in three microservices:

1. Auth Service (User Management): Handles user registration, login, and JWT-based authentication with [Keycloak](https://www.keycloak.org/guides#getting-started).
2. Expense Service: Manages CRUD operation for expenses and categories.
3. Notification Service: Listens to events and sends custom alerts.

SSO approach is a good choice for authenticating once and letting client perform requests for all microservices. 
Even-driven Approach also is another aspect of the requirement which lets client be aware of notifications real-time.
RDBMS such as, PostgreSQL is a good fit for managing data for the core logic, e.g, Expenses in this application.
MongoDB as a NoSQL and Schema-free Database operates efficiently for persisting and retrieving notifications.

The mentioned tools should be up and running before booting up the microservices. So, you need to start docker compose in current directory: 
```
docker-compose up -d
```
It will boot up the following services:
* Keycloak
* PostgreSQL
* RabbitMQ
* MongoDB

## Auth-Service

Keycloak is an opensource software product to allow single sign-on with identity and access management aimed in modern application.
Auth services is the integration of Spring Boot 3.4.3 and Spring Security 6.4.3 with Keycloak. 
Keycloak management dashboard is accessible through this url:
```
http://localhost:8080/admin/master/console
```
You can sign in with default credentials: _admin/admin_ 
Auth Application needs its own realm to be imported into Keycloak. 
```
auth-service/src/main/resources/keycloak-realm-setup.json
```
You just need to import setup file manually into Keycloak from browser.
The following config file is required by keycloak authz client, which has been used as a tool to get token in login API. 
```
auth-service/src/main/resources/keycloak.json
```
You should replace the client secret key with the one from you localhost. It can be obtained from Keycloak Management console as following: 
```
Clients menu => expense-app => Credentials => Client Secret
```

To access open api documentation of Auth Service, you can refer to the following link:  
```
https://localhost:9000/swagger-ui/index.html
```
This service's development progress is much further than the others:
- ✅ Integrate the last version of Spring security with Keycloak for authentication and method security (authorization)
- ✅ Open API Documentation
- ✅ Rest Service Error handling
- ✅ Use Google Jib to build docker image
- ✅ Write API and Unit Tests
- ✅ Secure the API by self-signed SSL keys
- ✅ Secure the credentials in application.properties by Jasypt maven plugin
- ✅ Dto validation
- ✅ Use mapstruct as a clean way to map entities to response objects

The remaining todo list for this service:
- ⚠️ Implement addRole API 
- ⚠️ Add logs in all required levels

## Expense Service

Expense Service exposes APIs to:
* Add, update, delete expenses.
* Fetch expenses by category and month.
* Generate monthly reports.
* Emit events via RabbitMQ when a report is generated.

## Notification Service

Notification Service listens to report generation events and send custom alerts

### Jasypt Configuration
Set Environment Variable: 
```
JASYPT_ENCRYPTOR_PASSWORD = secretKey
```
Encrypt your environment value using this command:
```bash
mvn jasypt:encrypt-value -Djasypt.encryptor.password="secretKey" -Djasypt.plugin.value="SuperSecret"
```

### Dockerization
The service uses [Google Jib](https://cloud.google.com/java/getting-started/jib) for packaging

To build an image for at **local workstation**:
```bash
mvn clean verify -DskipTests -U jib:dockerBuild 
```
- ⚠️ Docker engine required to be working on localhost
- ⚠️ You need to set proxy on your maven setting to bypass sanctions from Iran 
- ⚠️ For this reason, I forced maven plugin to use local base image. Now you just need to pull base image first:
```bash
docker pull alpine/java:21-jdk  
```