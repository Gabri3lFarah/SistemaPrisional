# Docker Guide - Sistema Prisional

Este guia explica como executar todo o Sistema Prisional usando Docker e Docker Compose.

## Pré-requisitos

- Docker Desktop instalado (ou Docker Engine + Docker Compose)
- 8GB de RAM disponível recomendado
- Portas disponíveis: 3306, 4200, 5433, 8080, 8081, 9092, 2181

## Arquitetura do Sistema

O sistema é composto por 7 serviços principais:

1. **prisioneiro-db** - Banco de dados MySQL para o serviço principal
2. **prisioneiro-core** - Backend principal (Quarkus + Java 21)
3. **visitas-db** - Banco de dados PostgreSQL para o microserviço de visitas
4. **visitas-service** - Microserviço de gerenciamento de visitas (Quarkus + Java 17)
5. **tela-ipen** - Frontend Angular
6. **kafka** + **zookeeper** - Sistema de mensageria para comunicação entre serviços

## Executando o Sistema

### Opção 1: Iniciar todos os serviços

```bash
docker compose up --build
```

Este comando irá:
- Construir as imagens Docker para os 3 serviços customizados
- Baixar as imagens necessárias do Docker Hub
- Iniciar todos os serviços na ordem correta
- Configurar a rede entre os containers

### Opção 2: Executar em background (modo daemon)

```bash
docker compose up -d --build
```

### Opção 3: Iniciar apenas serviços específicos

```bash
# Apenas o backend principal
docker compose up prisioneiro-core prisioneiro-db

# Apenas o microserviço de visitas
docker compose up visitas-service visitas-db kafka zookeeper

# Apenas o frontend
docker compose up tela-ipen
```

## Acessando os Serviços

Após iniciar, os serviços estarão disponíveis em:

- **Frontend (Angular)**: http://localhost:4200
- **Backend Principal**: http://localhost:8080
  - Health check: http://localhost:8080/q/health
  - Swagger UI: http://localhost:8080/q/swagger-ui
- **Microserviço de Visitas**: http://localhost:8081
  - Health check: http://localhost:8081/q/health
  - API de Visitantes: http://localhost:8081/visitantes
  - API de Visitas: http://localhost:8081/visitas
- **Banco MySQL**: localhost:3306
  - Database: sistema_prisional
  - User: root
  - Password: 1234
- **Banco PostgreSQL (Visitas)**: localhost:5433
  - Database: visitas_db
  - User: postgres
  - Password: postgres
- **Kafka**: localhost:9092

## Comandos Úteis

### Verificar status dos containers

```bash
docker compose ps
```

### Ver logs de todos os serviços

```bash
docker compose logs -f
```

### Ver logs de um serviço específico

```bash
docker compose logs -f prisioneiro-core
docker compose logs -f visitas-service
docker compose logs -f tela-ipen
```

### Parar todos os serviços

```bash
docker compose down
```

### Parar e remover volumes (apaga dados do banco)

```bash
docker compose down -v
```

### Reconstruir um serviço específico

```bash
docker compose up --build prisioneiro-core
```

### Executar comandos dentro de um container

```bash
# Acessar o shell do container
docker compose exec prisioneiro-core sh

# Executar comando Maven
docker compose exec prisioneiro-core mvn --version

# Acessar o banco MySQL
docker compose exec prisioneiro-db mysql -u root -p1234 sistema_prisional
```

### Reiniciar apenas um serviço

```bash
docker compose restart prisioneiro-core
```

## Solução de Problemas

### Porta já em uso

Se você receber erro de porta já em uso, você pode:

1. Identificar o processo usando a porta:
```bash
# Linux/Mac
lsof -i :8080

# Windows
netstat -ano | findstr :8080
```

2. Ou modificar as portas no arquivo `docker-compose.yml`

### Erro de memória

Se o Docker ficar sem memória:

1. Aumente a memória disponível para o Docker (Docker Desktop > Settings > Resources)
2. Ou execute apenas os serviços necessários

### Banco de dados não inicializa

Se o banco não inicializar corretamente:

```bash
# Remover volumes e reiniciar
docker compose down -v
docker compose up --build
```

### Build falha

Se o build de algum serviço falhar:

1. Limpe o cache do Docker:
```bash
docker builder prune
```

2. Reconstrua sem cache:
```bash
docker compose build --no-cache prisioneiro-core
```

### Container não consegue se conectar a outro

Verifique se os serviços estão na mesma rede:
```bash
docker network inspect sistemaprisional_sistema-prisional-network
```

## Desenvolvimento

### Desenvolvimento com Hot Reload

Para desenvolvimento, você pode rodar os serviços individualmente:

#### Backend (Quarkus Dev Mode)
```bash
# Apenas inicia o banco
docker compose up prisioneiro-db -d

# Roda o backend em modo dev (fora do Docker)
mvn quarkus:dev
```

#### Frontend (Angular Dev Mode)
```bash
# Inicia os backends
docker compose up prisioneiro-core visitas-service -d

# Roda o frontend em modo dev (fora do Docker)
cd tela_ipen
npm install
npm start
```

### Rebuild após mudanças no código

Sempre que fizer mudanças no código, reconstrua a imagem:

```bash
docker compose up --build prisioneiro-core
```

## Limpeza

### Remover containers, networks e volumes não utilizados

```bash
docker system prune -a --volumes
```

**Atenção**: Este comando remove TODOS os containers, imagens, volumes e networks não utilizados, não apenas do Sistema Prisional.

### Remover apenas os recursos deste projeto

```bash
docker compose down -v --rmi all
```

## Produção

Para executar em produção:

1. Modifique as senhas no `docker-compose.yml`
2. Configure variáveis de ambiente adequadas
3. Use volumes nomeados ou bind mounts para persistência
4. Configure backup dos volumes de dados
5. Use um orquestrador como Kubernetes para alta disponibilidade

## Variáveis de Ambiente

Principais variáveis que podem ser configuradas:

### Prisioneiro Core
- `QUARKUS_DATASOURCE_JDBC_URL`
- `QUARKUS_DATASOURCE_USERNAME`
- `QUARKUS_DATASOURCE_PASSWORD`
- `QUARKUS_HTTP_PORT`
- `QUARKUS_HTTP_CORS_ORIGINS`

### Visitas Service
- `DATABASE_HOST`
- `DATABASE_PORT`
- `DATABASE_NAME`
- `DATABASE_USER`
- `DATABASE_PASSWORD`
- `PRISIONEIRO_SERVICE_URL`
- `KAFKA_BOOTSTRAP_SERVERS`

## Monitoramento

### Health Checks

Todos os serviços possuem health checks configurados. Para verificar:

```bash
docker compose ps
```

Os serviços mostrarão status "healthy" quando estiverem prontos.

### Métricas

Acesse as métricas do Quarkus em:
- http://localhost:8080/q/metrics
- http://localhost:8081/q/metrics

## Segurança

**IMPORTANTE**: As configurações atuais são para desenvolvimento. Para produção:

1. ✅ Altere todas as senhas padrão
2. ✅ Use secrets do Docker ou variáveis de ambiente seguras
3. ✅ Configure SSL/TLS
4. ✅ Implemente autenticação e autorização adequadas
5. ✅ Configure firewall e regras de rede
6. ✅ Use imagens base atualizadas e escaneie vulnerabilidades

## Suporte

Para problemas ou dúvidas:
1. Verifique os logs: `docker compose logs -f`
2. Consulte a documentação oficial do Docker
3. Abra uma issue no repositório do projeto
