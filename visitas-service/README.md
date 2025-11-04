# Visitas Service

Microservice responsible for managing visits and visitors in the Sistema Prisional (Prison System).

## Overview

This service is part of a distributed architecture that extracts visits and visitors functionality from the monolithic core into a separate microservice. It uses:

- **Quarkus 3.22.2** - Supersonic Subatomic Java Framework
- **PostgreSQL** - Database for storing visits and visitors data
- **Flyway** - Database migration tool
- **Kafka** - Event-driven messaging (Outbox pattern)
- **MicroProfile REST Client** - For communication with prisioneiro-core service

## Architecture

### Database Schema

The service maintains its own PostgreSQL database with the following tables:

- `visitantes` - Stores visitor information (name, CPF, contact details, lawyer status)
- `visitas` - Stores visit records (prisoner ID, visitor ID, date, status, observations)
- `outbox` - Transactional outbox for event-driven architecture

### REST API Endpoints

#### Visitantes (Visitors)

- `GET /api/visitantes` - List all visitors
- `GET /api/visitantes/{id}` - Get visitor by ID
- `POST /api/visitantes` - Create a new visitor
- `PUT /api/visitantes/{id}` - Update visitor information
- `DELETE /api/visitantes/{id}` - Delete a visitor

#### Visitas (Visits)

- `GET /api/visitas` - List all visits
- `GET /api/visitas/{id}` - Get visit by ID
- `GET /api/visitas/prisioneiro/{prisioneiroId}` - Get visits by prisoner ID
- `GET /api/visitas/visitante/{visitanteId}` - Get visits by visitor ID
- `POST /api/visitas` - Create a new visit
- `PUT /api/visitas/{id}/status` - Update visit status

#### Health

- `GET /health` - Health check endpoint

### Business Rules

#### Special Prisoner (ID 41 - "Velho Viril")

The service implements special rules for prisoner ID 41:

1. Only lawyers can visit this prisoner
2. The lawyer must have authorization code 666
3. Visits not meeting these criteria are automatically denied

#### Visit Status

Visits can have the following statuses:

- `PENDENTE` - Pending approval (default when prisioneiro-core is unavailable)
- `AUTORIZADA` - Authorized
- `NEGADA` - Denied

## Building and Running

### Prerequisites

- Java 17 or later
- Maven 3.9+
- Docker and Docker Compose (for local deployment)

### Local Development

#### 1. Build the application

```bash
cd visitas-service
./mvnw clean package
```

#### 2. Run with Docker Compose

From the repository root:

```bash
docker-compose up -d
```

This will start:
- Zookeeper
- Kafka
- PostgreSQL database for visitas-service
- PostgreSQL database for prisioneiro-core
- Visitas-service container
- Prisioneiro-core container (if image is available)

#### 3. Access the service

The service will be available at:
- API: `http://localhost:8081`
- Health: `http://localhost:8081/health`

### Running Tests

#### Unit Tests

```bash
cd visitas-service
./mvnw test
```

#### Integration Tests

```bash
cd visitas-service
./mvnw verify
```

Integration tests use Testcontainers to spin up real PostgreSQL instances.

### Development Mode

Run Quarkus in development mode with hot reload:

```bash
cd visitas-service
./mvnw quarkus:dev
```

## Configuration

### Environment Variables

The service can be configured using the following environment variables:

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_HOST` | PostgreSQL host | `localhost` |
| `DATABASE_PORT` | PostgreSQL port | `5432` |
| `DATABASE_NAME` | Database name | `visitas_db` |
| `DATABASE_USER` | Database user | `postgres` |
| `DATABASE_PASSWORD` | Database password | `postgres` |
| `PRISIONEIRO_SERVICE_URL` | URL of prisioneiro-core service | `http://localhost:8080` |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka bootstrap servers | `localhost:9092` |

### application.properties

See `src/main/resources/application.properties` for full configuration options.

## Distributed Architecture

### Communication with Prisioneiro-Core

The service communicates with prisioneiro-core using:

1. **Synchronous HTTP** - MicroProfile REST Client with fault tolerance
   - Used to validate prisoner existence when creating a visit
   - Implements timeout (5s) and fallback behavior
   - If service is unavailable, visits are created with `PENDENTE` status

2. **Asynchronous Kafka** - Event-driven messaging (future implementation)
   - Outbox pattern for reliable event publishing
   - Events published to `visitas-events` topic

### Fault Tolerance

The service implements fault tolerance using MicroProfile Fault Tolerance:

- **Timeout**: 5 seconds for REST client calls
- **Fallback**: When prisioneiro-core is unavailable, visits are created as PENDENTE
- **Circuit Breaker**: (TODO) Prevent cascading failures

## Database Migrations

Flyway migrations are executed automatically on startup. Migration scripts are located in:

```
src/main/resources/db/migration/
```

### Initial Migration (V1)

Creates:
- `visitantes` table with indexes
- `visitas` table with indexes
- `outbox` table for event sourcing
- Sample data for testing

## Docker

### Building the Docker Image

```bash
cd visitas-service
docker build -t sistema-prisional/visitas-service:latest .
```

### Running the Container

```bash
docker run -p 8081:8081 \
  -e DATABASE_HOST=host.docker.internal \
  -e DATABASE_PORT=5432 \
  -e DATABASE_NAME=visitas_db \
  -e DATABASE_USER=postgres \
  -e DATABASE_PASSWORD=postgres \
  -e PRISIONEIRO_SERVICE_URL=http://host.docker.internal:8080 \
  sistema-prisional/visitas-service:latest
```

## Monitoring and Observability

### Health Checks

The service exposes health checks at `/health`:

```bash
curl http://localhost:8081/health
```

Response includes:
- Database connectivity
- Overall service status

### Logging

Logs are written to stdout and can be viewed with:

```bash
docker logs visitas-service
```

## Development Workflow

1. **Create a feature branch** from main
2. **Make changes** to the code
3. **Run tests** to ensure everything works
4. **Build the Docker image** to test containerization
5. **Run docker-compose** to test the distributed setup
6. **Submit a pull request** for review

## Troubleshooting

### Database Connection Issues

If the service can't connect to PostgreSQL:

1. Check that PostgreSQL is running: `docker ps`
2. Verify environment variables are correct
3. Check PostgreSQL logs: `docker logs visitas-db`

### Prisioneiro-Core Communication Issues

If REST client calls are failing:

1. Verify prisioneiro-core is running: `curl http://localhost:8080/health`
2. Check the `PRISIONEIRO_SERVICE_URL` environment variable
3. Check service logs for timeout or connection errors

The service will gracefully handle failures and create visits with `PENDENTE` status.

### Test Failures

If integration tests fail:

1. Ensure Docker is running (Testcontainers requirement)
2. Check that no other service is using ports 8081, 5432, 5433
3. Run tests with verbose output: `./mvnw verify -X`

## Future Enhancements

- [ ] Implement complete Kafka event publishing
- [ ] Add circuit breaker for REST client
- [ ] Implement retry logic for failed operations
- [ ] Add metrics and monitoring (Prometheus/Grafana)
- [ ] Implement rate limiting for API endpoints
- [ ] Add API documentation (OpenAPI/Swagger)
- [ ] Implement authentication and authorization
- [ ] Add caching for frequently accessed data

## License

Copyright © 2025 Sistema Prisional
