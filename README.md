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
