# TX Prototype

An explorative approach to build a system of SCS using client side transclusion.

The whole thing is based on docker for infrastructure. So:

```bash
cd docker
docker-compose up
```

The Consul UI is available at http://localhost:8500/ui/#/dc1/services.

## Tech stack

Spring Boot
Spring Cloud
Consul
MongoDb

## User Administration

```bash
mvn spring-boot:run
```

Entry point is http://localhost:8087


## Keycloak related
http://www.keycloak.org/documentation.html

TODO Export import https://keycloak.gitbooks.io/documentation/server_admin/topics/export-import.html
http://blog.keycloak.org/2015/10/getting-started-with-keycloak-securing.html

Get Endpoints http http://localhost:8080/auth/realms/tx_prototype/.well-known/openid-configuration

http://localhost:8080/ Admin interface

http://localhost:8080/auth/realms/tx_prototype/account


### Working CLI example

curl -X POST http://127.0.0.1:8080/auth/realms/tx_prototype/protocol/openid-connect/token  -H "Content-Type: application/x-www-form-urlencoded" -d 'username=koenighotze' -d 'password=koenighotze' -d 'grant_type=password' -d 'client_id=tx_prototype-user' -d 'client_secret=d6f4dbe6-7586-4aad-b248-ac765022fb18' | jq -r '.access_token' | pbcopy

token=`pbpaste`

http http://127.0.0.1:8080/auth/realms/tx_prototype/protocol/openid-connect/userinfo "Authorization: Bearer $token"

## Kafka related
