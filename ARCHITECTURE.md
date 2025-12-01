Architecture technique - Microservices Conférences & Keynotes

Résumé
------
Cette application est une architecture micro-services pour gérer des Keynotes et des Conférences.
Composants fonctionnels :
- keynote-service : gestion des Keynote (CRUD)
- conference-service : gestion des Conférences et Reviews (CRUD)
- gateway-service : API Gateway (Spring Cloud Gateway)
- discovery-service : Service Discovery (Eureka)
- config-service : Configuration centralisée (Spring Cloud Config)
- angular-front-app : frontend Angular
- keycloak : provider OIDC/OAuth2 pour l'authentification

Principes et choix technos
--------------------------
- Java 17+ et Spring Boot 3.x
- Spring Cloud (Spring Cloud Gateway, Eureka, Spring Cloud Config)
- Spring Data JPA + H2 (développement) / PostgreSQL (production)
- OpenFeign pour communication inter-services
- Resilience4J pour circuit breaker / retry / bulkhead
- Springdoc OpenAPI (Swagger) pour documentation REST
- Keycloak pour l'authentification (OIDC/OAuth2)
- Angular 16+ pour le front-end
- Docker & Docker Compose pour la distribution locale

Topologie et flux
-----------------
- Tous les services s'enregistrent auprès de `discovery-service` (Eureka).
- `gateway-service` assure la face publique : routage, auth, filtrage et propagation des tokens.
- `config-service` fournit les configurations centralisées (Git-backed ou fichiers locaux pour l'exercice).
- `keynote-service` et `conference-service` exposent des API REST décrites par OpenAPI.
- `conference-service` peut appeler `keynote-service` via Feign pour récupérer des informations sur une keynote liée.

ASCII diagramme
---------------

                 +-----------+
                 |  Keycloak |
                 +-----+-----+
                       |
                 +-----v-----+         +---------------+
                 |  Gateway  | <-----> |  Angular SPA   |
                 |  (OAuth)  |         +---------------+
                 +--+---+----+
                    |   |
   +----------------+   +----------------+
   |                                     |
+--v-----+                          +----v----+
| Eureka  |                          | Config  |
|Discovery|                          | Service |
+--+-----+                          +----+----+
   |                                     |
+--v---+   +-----------------+   +-----v-----+
|Keynote|   | ConferenceSvc  |   |  Other(s)  |
|Service|   | (with Reviews) |   |            |
+------+   +-----------------+   +-----------+

Ports (suggestion local)
------------------------
- Keycloak : 8080 (docker default)
- Config Service : 8888
- Eureka Discovery : 8761
- Gateway Service : 8081
- Keynote Service : 8082
- Conference Service : 8083
- Angular Front : 4200

Contrats API (extraits)
-----------------------
1) Keynote-service (Base path: /api/keynotes)
- GET /api/keynotes - lister keynotes
- POST /api/keynotes - créer keynote (body: KeynoteDTO)
- GET /api/keynotes/{id} - details
- PUT /api/keynotes/{id} - mise à jour
- DELETE /api/keynotes/{id}

KeynoteDTO
{
  id: Long,
  nom: String,
  prenom: String,
  email: String,
  fonction: String
}

2) Conference-service (Base path: /api/conferences)
- GET /api/conferences - lister
- POST /api/conferences - créer (body: ConferenceDTO)
- GET /api/conferences/{id}
- PUT /api/conferences/{id}
- DELETE /api/conferences/{id}
- GET /api/conferences/{id}/reviews - lister reviews
- POST /api/conferences/{id}/reviews - ajouter review (ReviewDTO)

ConferenceDTO
{
  id: Long,
  titre: String,
  type: "ACADEMIQUE" | "COMMERCIALE",
  date: ISODateTime,
  dureeMinutes: Integer,
  nbInscrits: Integer,
  score: Double,
  keynoteId: Long (optionnel)
}

ReviewDTO
{
  id: Long,
  date: ISODateTime,
  texte: String,
  note: Integer (1..5)
}

Sécurité
--------
- Keycloak gère les utilisateurs, rôles (USER, ADMIN) et les clients.
- Gateway valide/initialise le token JWT et le propage aux micro-services.
- Services vérifient les scopes/roles (via Spring Security Resource Server ou OAuth2 client).
- Endpoints d'administration (config, eureka) protégés par role ADMIN.

Resilience et Observabilité
---------------------------
- Resilience4J ajouté sur clients Feign pour timeouts, retries et circuit breakers.
- Spring Boot Actuator exposé et sécurisé (health, metrics).
- OpenAPI généré par springdoc-openapi.

Docker / Déploiement local (approche)
------------------------------------
- Chaque service packagé en image Docker.
- Un `docker-compose.yml` orchester : réseaux, volumes, dépendances (keycloak, config, eureka, gateway, services, angular). 
- `docker-compose` exécute Keycloak (first), Config, Eureka, Services, Gateway.

Extrait docker-compose (haut niveau)
------------------------------------
version: '3.8'
services:
  keycloak:
    image: quay.io/keycloak/keycloak:21.0
    command: start-dev
    ports: ["8080:8080"]
  config-service:
    build: ./config-service
    ports: ["8888:8888"]
  discovery-service:
    build: ./discovery-service
    ports: ["8761:8761"]
  gateway-service:
    build: ./gateway-service
    ports: ["8081:8081"]
  keynote-service:
    build: ./keynote-service
    ports: ["8082:8082"]
  conference-service:
    build: ./conference-service
    ports: ["8083:8083"]

Prochaines étapes recommandées
------------------------------
1. Générer le parent Maven multi-module et les modules (squelettes) — je peux le faire automatiquement.
2. Implémenter les services infra (config, discovery, gateway) et vérifier qu'ils démarrent.
3. Implémenter `keynote-service` puis `conference-service`.

Annexes
-------
- Choix alternatifs : Consul à la place d'Eureka; Spring Cloud Config vs Consul Config.
- Pour un déploiement sur Kubernetes, remplacer docker-compose par manifests/Helm et utiliser Spring Cloud Kubernetes ou K8s-native service discovery.

Fin du document.
