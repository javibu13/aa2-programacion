# AA 2

To work with this tomcat web application it is needed to create a database using de dbCreation.sql script and configure the env file with the database connection information.

## Database configuration

Create in the repository's root path a file named `env` with the following content:
```env
# MySQL Configuration
MYSQL_ROOT_PASSWORD=S3cr3tP4ssw0rd
MYSQL_DATABASE=epi_db
MYSQL_USER=user
MYSQL_PASSWORD=P4ssw0rd
MYSQL_PORT=3306
```

To deploy the docker container to create and run the database, run the following command:

```bash
docker compose up -d
```

To stop the database container, run the following command:

```bash
docker compose down
```

## Deploy the web application

To deploy the web application, run the following command:

```bash
mvn tomcat7:deploy
```

To undeploy the web application, run the following command:

```bash
mvn tomcat7:undeploy
```