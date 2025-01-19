# Fullstack Java Project

## Symon Blazejczak (3AONb)

## Setup

**It's possible that you need to wait around half a minute for the discovery service and who knows what else is ready, before anything shows up in the Angular app.**

### Backend

0. Open 'backend-java' folder in an IDE (recommended: IntelliJ IDEA)
1. Start Docker Desktop
2. Run `docker compose up` in the 'backend-java' folder (contains `docker-compose.yml`)
3. Run all microservices by going to the 'Services' tab in the bottom-left side. 

- Make sure to first run these 3 in this order, the others can be run in any order:
  - a. ConfigServiceApp
  - b. DiscoveryServiceApp
  - c. GatewayServiceApp

- Run each microservice **one by one** to prevent interesing issues...

### Frontend

0. Open 'frontend-web' folder in an IDE (recommended: VSCode)
1. Run `ng serve -o`



## Folder structure

- Readme.md
- _architecture_: this folder contains documentation regarding the architecture of your system.
- `docker-compose.yml` : to start the backend (starts all microservices)
- _backend-java_: contains microservices written in java
- _demo-artifacts_: contains images, files, etc that are useful for demo purposes.
- _frontend-web_: contains the Angular webclient
