# Docker Compose (machines)

This service expects the following environment variables (see `src/main/resources/application.yml`):

- `jwk.set.uri`
- `db.url`
- `db.username`
- `db.password`

## Quick start

```bash
docker compose up --build
```

### Common overrides

Use a `.env` file (same folder as `docker-compose.yml`) to set values, for example:

```env
JWK_SET_URI=http://host.docker.internal:8080/oauth2/jwks
POSTGRES_DB=worksite-db
POSTGRES_USER=worksite
POSTGRES_PASSWORD=worksite

# Optional overrides if you don't want the defaults derived from the Postgres vars
DB_URL=jdbc:postgresql://db:5432/worksite-db
DB_USERNAME=worksite
DB_PASSWORD=worksite
```

## Notes

- The compose file uses `depends_on: condition: service_healthy` so the API waits for Postgres.
- `host.docker.internal` works on Docker Desktop (macOS/Windows). If your JWK endpoint is another container, add it as another compose service and point `JWK_SET_URI` to that service name.

