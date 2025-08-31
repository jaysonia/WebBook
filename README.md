# WebBook

## Description
This is a basic web app to replicate a bookshop that provides the following functionality

- User Auth/registration
- Add/edit/delete books
- List books
- Add items to cart
- Place orders

## Requirements
- Docker/docker compose/sql server
- Java version 24
- Maven version 3.9.9

## Running SQL
### Docker
The SQL server can be started up using docker compose with the command `docker compose up -d` in the project root
### Local
Install MySQL locally and setup the database with a database called `bookshop` ensure to update the [properties](src/main/resources/application.properties) file with the correct user details before launching the application.

## Running the App
To run the app you can run the following commands.
First you will need to set Some Environment variables
```bash
export DB_USER=username
export DB_PASS=password
```
Once the database variables are exported you can run the application with the either of the following commands.
- linux/mac `./mvnw spring-boot:run`
- Windows: `mvnw.cmd spring-boot:run`
