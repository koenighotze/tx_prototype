# TX Prototype

Current build status: [![Build Status](https://travis-ci.org/koenighotze/tx_prototype.svg?branch=master)](https://travis-ci.org/koenighotze/tx_prototype)



An explorative approach to build a system of SCS using client side transclusion.






## Tech stack

* Spring Boot
* Spring Cloud
* Consul
* MongoDb
* Keycloak for IAM incl. Postgres

## Infrastructure

The whole thing is based on Docker for infrastructure. So:

```bash
cd docker
docker-compose up
```

* The Consul UI is available at http://localhost:8500/ui/#/dc1/services
* Keycloak Admin interface is available at http://localhost:8080. Login using admin/admin

After the initial start, you need to import `docker/tx_prototype-*.json`, i.e. the client configuration.

## Sub-Application overview

This section explains the actual microservices of this prototype setup.

### User Administration

```bash
mvn spring-boot:run
```

Entry point is http://localhost:8088/crm/

## Bucket List

Things I will try out one after another.

* Secure REST endpoints
* Integrate Kafka
* Drop Kafka Data into TS buckets
* Integrate Eventstore into programming model
* Expose Endpoints Using ATOM/PUB


## Keycloak setup

* http://www.keycloak.org/documentation.html
* https://keycloak.gitbooks.io/documentation/server_admin/topics/export-import.html
* http://blog.keycloak.org/2015/10/getting-started-with-keycloak-securing.html
* Get available endpoints `http http://localhost:8080/auth/realms/tx_prototype/.well-known/openid-configuration`

### Using the REST endpoints

Login as admin:

```bash
http --form POST  http://localhost:8080/auth/realms/master/protocol/openid-connect/token grant_type=password client_id=admin-cli username=admin password=admin
```

Store the access token:

```bash
access_token=`http --form POST  http://localhost:8080/auth/realms/master/protocol/openid-connect/token grant_type=password client_id=admin-cli username=admin password=admin | jq -r '.access_token'`
```

Get available realms:

```bash
http http://localhost:8080/auth/admin/realms "Authorization: Bearer $token"
```

### Working CLI example

```bash
curl -X POST http://127.0.0.1:8080/auth/realms/tx_prototype/protocol/openid-connect/token  -H "Content-Type: application/x-www-form-urlencoded" -d 'username=koenighotze' -d 'password=koenighotze' -d 'grant_type=password' -d 'client_id=tx_prototype-user' -d 'client_secret=d6f4dbe6-7586-4aad-b248-ac765022fb18' | jq -r '.access_token' | pbcopy
token=`pbpaste`
http http://127.0.0.1:8080/auth/realms/tx_prototype/protocol/openid-connect/userinfo "Authorization: Bearer $token"
```

## Kafka setup


## Eventstore

* Admin interface: Running at `http://127.0.0.1:2113/web/index.html#/`.
 * Log in using `admin` and `changeit`

* Stream == Aggregate