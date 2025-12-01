# Microservices — Gestion Keynotes & Conférences

Un projet d'architecture microservices pour gérer des conférences et des keynotes, comprenant services backend Java/Spring Boot, une SPA Angular, et une infrastructure minimale pour l'authentification, la découverte et la configuration centralisée.

## Aperçu

- **Objectif** : fournir une plateforme modulaire pour créer, lire, modifier et supprimer des keynotes et conférences, ainsi que gérer les avis (reviews) sur les conférences.
- **Approche** : microservices indépendants qui communiquent via HTTP/Feign et sont découverts dynamiquement via un annuaire.
- **Composants clés** : API Gateway, Config Server, Service de découverte, services métiers (keynote & conference), application front Angular, Keycloak pour l'identité.

---

## Technologies principales

- Java 17+ / Spring Boot 3.x
- Spring Cloud : Gateway, Config Server, Eureka (discovery)
- Spring Data JPA (H2 en dev, PostgreSQL en production)
- OpenFeign pour appels inter-services
- Resilience4J (retry, circuit breaker, timeouts, bulkhead)
- Springdoc OpenAPI (Swagger) pour documentation REST
- Keycloak (OIDC/OAuth2) pour l'authentification et la gestion des rôles
- Angular 16+ pour l'interface client
- Docker & Docker Compose pour l'orchestration locale

---

## Architecture et flux

L'ensemble forme une architecture modulaire : chaque microservice expose une API REST documentée, s'enregistre auprès du service de découverte, et récupère sa configuration depuis le Config Server.

- Les clients (SPA Angular ou autres clients HTTP) appellent la Gateway.
- La Gateway applique le routage, vérifie les jetons JWT et relaie les requêtes vers les services backend.
- Les services backend s'enregistrent automatiquement dans Eureka pour être localisables.
- Les paramètres (URL des services, properties) proviennent du Config Server central.

Flux simplifié : Client → Gateway → Service métier (via Eureka + Feign)

ASCII Diagramme (schéma simplifié)

```
           +-----------+        +-------------+
           |  Keycloak | <---->  |  Angular UI |
           +-----------+        +-------------+
                ^  |                  |
                |  | JWT              | HTTPS
                |  v                  v
            +-------------------------------+
            |            Gateway            |
            |  (routage, sécurité JWT/OAuth) |
            +-------------------------------+
               |                  |     ^
      +--------+                  |     |
      |                           v     |
  +--------+    +----------------------+ |
  | Config |    |     Eureka Server     | |
  | Server |    +----------------------+ |
  +--------+             |               |
       |                 / \              |
       |                /   \             |
       v               v     v            |
  +-----------+   +---------------+   +---------+
  | keynote-  |   | conference-   |   |  other  |
  | service   |   | service       |   | services|
  +-----------+   +---------------+   +---------+

```

---

## Services et ports (exemples locaux)

| Service | Port local |
|---|---:|
| Keycloak (IDP) | `8080` |
| Config Server | `8888` |
| Discovery (Eureka) | `8761` |
| Gateway | `8081` |
| Keynote Service | `8090` |
| Conference Service | `8091` |
| Angular Frontend | `4200` |

> Ces numéros servent d'exemple pour un environnement de développement. En production, ajustez-les dans vos fichiers de configuration ou orchestrateur.

---

## API — Principaux endpoints

Toutes les APIs exposent des routes REST documentées par OpenAPI (accessible via `/v3/api-docs` ou l'interface Swagger UI selon chaque service).

### Keynote Service (CRUD)

Endpoints typiques :

- `GET /api/keynotes` — lister toutes les keynotes
- `GET /api/keynotes/{id}` — obtenir une keynote par son ID
- `POST /api/keynotes` — créer une nouvelle keynote
- `PUT /api/keynotes/{id}` — mettre à jour une keynote
- `DELETE /api/keynotes/{id}` — supprimer une keynote

Exemple de payload (KeynoteDTO) :

```json
{
  "id": 42,
  "title": "Architecture Cloud Native",
  "speaker": "Dr. A. Example",
  "durationMinutes": 45,
  "scheduledAt": "2026-05-10T14:00:00"
}
```

### Conference Service

Ce service gère les conférences et les avis (reviews).

Endpoints principaux :

- Conférences
  - `GET /api/conferences` — lister
  - `GET /api/conferences/{id}` — détails
  - `POST /api/conferences` — créer
  - `PUT /api/conferences/{id}` — mettre à jour
  - `DELETE /api/conferences/{id}` — supprimer

- Reviews
  - `GET /api/conferences/{confId}/reviews` — lister avis d'une conférence
  - `POST /api/conferences/{confId}/reviews` — ajouter un avis
  - `DELETE /api/conferences/{confId}/reviews/{reviewId}` — supprimer un avis

Exemple de DTO pour une conférence (ConferenceDTO) :

```json
{
  "id": 7,
  "title": "Microservices Patterns",
  "location": "Salle A",
  "startDate": "2026-06-21",
  "endDate": "2026-06-21",
  "keynoteId": 42
}
```

Exemple de DTO pour un avis (ReviewDTO) :

```json
{
  "id": 101,
  "author": "user@example.com",
  "rating": 4,
  "comment": "Session claire et bien rythmée",
  "createdAt": "2026-06-21T16:30:00"
}
```

Inter-service : le `conference-service` peut récupérer des données sur une keynote en appelant le `keynote-service` via OpenFeign (découverte via Eureka).

---

## Sécurité

- L'authentification et l'autorisation sont assurées par Keycloak (OIDC). Les clients obtiennent un token JWT puis appellent la Gateway.
- La Gateway est le point d'entrée central : elle valide les JWT, applique des règles de routage et transmet les en-têtes d'authentification aux services en aval.
- Les services backend font des vérifications supplémentaires sur les rôles/scopes contenus dans le token pour appliquer des contrôles d'accès (par exemple : distinction **ADMIN** / **USER**).

Rôles et usages :

- **USER** : accès en lecture et écriture limité (ex : ajouter un review, consulter conférences)
- **ADMIN** : droits étendus (création/modification/suppression de ressources)

Conseils : centraliser les règles d'autorisation dans la Gateway et compléter par des vérifications côté service pour les opérations sensibles.

---

## Résilience, observabilité et documentation

- Résilience : utilisation de Resilience4J pour protéger les appels inter-services (timeouts, retry, circuit breaker, et isolation par bulkhead).
- Observabilité : Spring Boot Actuator expose endpoints de monitoring et métriques (ex : `/actuator/health`, `/actuator/metrics`). Ces métriques peuvent être scrappées par Prometheus ou affichées via Grafana.
- Documentation : chaque service publie une spécification OpenAPI/Swagger pour faciliter l'exploration et les tests d'API.

---

## Docker & exécution locale

Les microservices sont empaquetés en images Docker; un fichier `docker-compose` orchestre l'exécution pour le développement.

Extrait illustratif de `docker-compose.yml` (exemple ré-imaginé) :

```yaml
version: '3.8'
services:
  keycloak:
    image: quay.io/keycloak/keycloak:21.1.1
    environment:
      - KC_HTTP_RELATIVE_PATH=/auth
    ports:
      - "8080:8080"

  config-server:
    build: ./config-service
    ports:
      - "8888:8888"

  discovery:
    build: ./discovery-service
    ports:
      - "8761:8761"

  gateway:
    build: ./gateway-service
    ports:
      - "8081:8081"

  keynote:
    build: ./keynote-service
    ports:
      - "8090:8090"

  conference:
    build: ./conference-service
    ports:
      - "8091:8091"

  angular:
    build: ./angular-front-app
    ports:
      - "4200:4200"

networks:
  default:
    driver: bridge
```

Exécution locale (exemples de commandes PowerShell) :

```powershell
# construire et lancer tous les services
docker compose up --build

# arrêter et supprimer
docker compose down --volumes --remove-orphans
```

Notes :

- Ajustez les images et variables d'environnement pour les secrets et la configuration Keycloak.
- En développement, la base H2 peut être activée; en production, configurez PostgreSQL et les variables de connexion.

---

## Bonnes pratiques recommandées

- Externaliser les secrets (Vault, Secrets Manager).
- Activer TLS entre la Gateway et les services en production.
- Ajouter un observateur centralisé (Prometheus + Grafana) et traces distribuées (OpenTelemetry).

---

## Next Steps ➜ évolutions possibles

- Améliorer l'interface Angular pour une expérience utilisateur complète (recherche, filtres, pagination).
- Ajouter un service de notifications (emails / WebSocket) pour alerter sur les nouveaux reviews ou annonces.
- Migrer la persistence vers PostgreSQL en production et ajouter des scripts de migration (Flyway/Liquibase).
- Conteneuriser proprement et déployer sur un cluster Kubernetes (Helm charts, Ingress, cert-manager).

---

## Fichiers utiles

- `gateway-service/` — API Gateway Spring Cloud
- `discovery-service/` — Eureka server
- `config-service/` — Spring Cloud Config Server
- `keynote-service/` — microservice Keynote (CRUD)
- `conference-service/` — microservice Conference + Reviews
- `angular-front-app/` — client Angular

---

