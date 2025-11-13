# SistemaPrisional

Este repositório contém o Sistema Prisional completo, incluindo:
- **prisioneiro-core**: Backend principal (Quarkus + Java 21 + MySQL)
- **visitas-service**: Microserviço de visitas (Quarkus + Java 17 + PostgreSQL)
- **tela_ipen**: Frontend Angular

## 🐳 Docker (Recomendado)

A maneira mais fácil de executar todo o sistema é usando Docker. Veja o [DOCKER.md](DOCKER.md) para instruções completas.

**Início rápido:**
```bash
# 1. Construir as aplicações Java
./build.sh

# 2. Iniciar todos os serviços
docker compose up --build

# Ou usar o Makefile
make up
```

Após iniciar, acesse:
- Frontend: http://localhost:4200
- Backend Principal: http://localhost:8080
- Microserviço de Visitas: http://localhost:8081

---

# SistemaPrisional — visitas-service

Este repositório contém o módulo `visitas-service`, um microserviço Quarkus responsável por gerenciar Visitas e Visitantes no sistema prisional. O objetivo é extrair essas responsabilidades do serviço core para um serviço dedicado, com DB próprio e pronto para operar em arquitetura distribuída (event-driven + HTTP fallback).

O que eu fiz
- Extraí e scaffoldei um microserviço Quarkus chamado `visitas-service` (Java 17, Quarkus 3.22.2).
- Adicionei entidades JPA (Visita, Visitante), repositório Panache, camada de serviço e recursos REST.
- Adicionei Flyway migration (cria tabelas `visitantes`, `visitas` e `outbox`).
- Criei um Dockerfile do serviço e um `docker-compose.yml` para ambiente local com Kafka/Zookeeper, Postgres e um placeholder/possibilidade de mock do Prisioneiro Core.
- Preparei instruções para rodar em modo dev, com Docker Compose e para testar endpoints.
- Recomendei e documentei a estratégia distribuída: Event-driven (Kafka) + Outbox Pattern + HTTP fallback com circuit-breaker.

Visão geral da arquitetura
- visitas-service
  - Banco: PostgreSQL (base dedicada `visitasdb`)
  - Comunicação inter-serviços:
    - Recomendado: eventos Kafka (PrisioneiroCreated/Deleted) + consumidor no visitas-service para manter um read-model local de prisioneiros.
    - Fallback síncrono: MicroProfile Rest Client (`PrisioneiroClient`) para validar existencia de prisioneiro (com circuit breaker/timeouts).
  - Consistência: eventual (event-driven); Outbox pattern para publicação confiável de eventos VisitaCreated.
- Infra local de desenvolvimento (docker-compose):
  - Zookeeper + Kafka (eventos)
  - visitas-db (Postgres)
  - visitas-service (buildado a partir do módulo)
  - prisioneiro-core (placeholder ou mock container)

Pré-requisitos (para rodar localmente)
- Java 17 (JDK)
- Maven 3.6+ / 3.8+
- Docker Desktop (ou Docker Engine) + Docker Compose
- (Opcional) Python 3 para rodar mock local sem Docker
- VS Code (ou outro IDE) se quiser editar o código

Como rodar — opção recomendada (Docker Compose)
1. Na raiz do repositório:
   - Certifique-se que o JAR do serviço foi gerado ou que o Dockerfile consegue construir via Maven:
     mvn -f visitas-service package -DskipTests
   - Se você prefere que o compose construa a imagem do serviço a partir do código, apenas rode:
     docker-compose up --build

2. Serviços expostos (conforme compose padrão):
   - visitas-service: http://localhost:8081
   - prisioneiro-core mock (se configurado no compose): http://localhost:8080
   - Kafka: em kafka:9092 (dentro do compose) e, se exposto, em localhost:29092

3. Testes rápidos:
   - Criar visitante:
     curl -X POST http://localhost:8081/visitantes -H "Content-Type: application/json" -d '{"nome":"Joao Silva","telefone":"48999999999","cpf":"00000000000"}'
   - Criar visita (ajuste `prisioneiroId` conforme seu mock/core):
     curl -X POST http://localhost:8081/visitas -H "Content-Type: application/json" -d '{"prisioneiroId":1,"dataVisita":"2025-11-05T10:00:00","nomeVisitante":"Joao Silva"}'

Como rodar em modo dev (hot reload) sem Docker para o serviço
1. Se preferir rodar só o serviço localmente (recomendado para desenvolvimento rápido):
   - Startar um banco Postgres local (ou use o container `visitas-db` do compose):
     docker run --name visitas-db -e POSTGRES_DB=visitasdb -e POSTGRES_USER=visitas -e POSTGRES_PASSWORD=visitas -p 5433:5432 -d postgres:15
   - Exportar variáveis de ambiente (exemplo):
     export VISITAS_JDBC_URL=jdbc:postgresql://localhost:5433/visitasdb
     export VISITAS_DB_USER=visitas
     export VISITAS_DB_PASSWORD=visitas
     export PRISIONEIRO_CORE_URL=http://localhost:8080
   - Rodar Quarkus em modo dev:
     mvn -f visitas-service quarkus:dev
   - Acesse o serviço em http://localhost:8080 (ou conforme configuração de porta)

Mock do Prisioneiro Core
- Para evitar depender de uma imagem externa, você pode:
  - Incluir um serviço `prisioneiro-mock` no `docker-compose.yml` (pequeno Flask/FastAPI) que responde `GET /prisioneiros/{id}/exists` com `true`.
  - Ou rodar um script Python localmente que serve esse endpoint e apontar `PRISIONEIRO_CORE_URL` para `host.docker.internal` (ou `localhost` se rodar o serviço fora do container).

Banco de dados e migrações
- O módulo usa Flyway para aplicar migrações ao iniciar.
- Migração principal está em:
  visitas-service/src/main/resources/db/migration/V1__create_visitas_and_visitantes.sql
  - Cria tabelas `visitantes`, `visitas` e `outbox` (para padrão Outbox).

Testes
- Unit tests: JUnit 5 + Quarkus testing support.
- Testes de integração: Testcontainers (Postgres) para validar endpoints e persistência.
- Com Docker ativo, execute:
  mvn -f visitas-service test

Endpoints principais (resumo)
- Visitantes
  - POST /visitantes — cria visitante
  - GET /visitantes — lista visitantes
  - GET /visitantes/{id} — obtém visitante por id
- Visitas
  - POST /visitas — cria visita (valida prisioneiro via PrisioneiroClient ou read-model)
  - GET /visitas — lista todas visitas
  - GET /visitas/{id} — obtém visita por id
  - GET /visitas/prisioneiro/{prisioneiroId} — lista visitas de um prisioneiro

Configuração (variáveis importantes)
- VISITAS_JDBC_URL — JDBC URL do Postgres
- VISITAS_DB_USER — usuário do DB
- VISITAS_DB_PASSWORD — senha do DB
- PRISIONEIRO_CORE_URL — URL base do Prisioneiro Core (ex.: http://prisioneiro-core:8080)
- KAFKA_BOOTSTRAP_SERVERS — bootstrap servers do Kafka (no compose: kafka:9092)

Boas práticas / próximos passos recomendados
- Implementar consumer Kafka no Prisioneiro Core para publicar eventos PrisioneiroCreated/Deleted.
- Implementar consumer no visitas-service para manter um read-model (prisioneiros ids) e reduzir chamadas síncronas.
- Implementar Outbox pattern: persistir evento em `outbox` na mesma transação que a criação da visita e ter um worker que publica no Kafka e marca `published=true`.
- Adicionar circuit-breaker/timeouts (SmallRye Fault Tolerance ou Resilience4j) no `PrisioneiroClient`.
- Adicionar tracing (Jaeger), métricas (Prometheus) e logs estruturados.
- Configurar CI (GitHub Actions) para build/tests e CD para deploy.

Solução de problemas comuns
- Erro: target/*-runner.jar not found
  - Rode: mvn -f visitas-service package -DskipTests
- Erro: porta já em uso
  - Identifique processo usando a porta e libere ou altere as portas no docker-compose.
- Testcontainers falhando nos testes
  - Certifique-se Docker está rodando e que o backend WSL2 está habilitado no Windows.


Licença
- (Adicione a licença desejada aqui — ex.: MIT)

