# Como Rodar o Sistema Prisional Completo

Este guia explica como executar todo o sistema (backend, frontend e microserviço).

## Pré-requisitos

- Docker e Docker Compose instalados
- Java 17 (JDK)
- Maven
- Node.js e npm

## Passo 1: Subir os Bancos de Dados com Docker

Na raiz do projeto, execute:

```powershell
docker-compose up -d prisioneiro-db visitas-db kafka zookeeper
```

Isso vai iniciar:
- **MySQL** (porta 3306) - banco de dados do backend principal
- **PostgreSQL** (porta 5433) - banco de dados do visitas-service
- **Kafka + Zookeeper** - para mensageria

Aguarde alguns segundos para os containers iniciarem completamente.

## Passo 2: Importar os Dados no MySQL (Primeira Vez)

Se você tem o arquivo `sistema_prisional.sql`, importe-o no MySQL:

```powershell
# Copie o arquivo para o container
docker cp sistema_prisional.sql prisioneiro-db:/tmp/sistema_prisional.sql

# Importe no MySQL
docker exec -i prisioneiro-db mysql -uroot -p1234 sistema_prisional < sistema_prisional.sql
```

## Passo 3: Iniciar o Backend Principal (porta 8080)

Na raiz do projeto:

```powershell
mvn quarkus:dev
```

Aguarde até ver a mensagem "Quarkus started in...". O backend estará em **http://localhost:8080**

## Passo 4: Iniciar o Microserviço Visitas (porta 8081)

O visitas-service pode ser iniciado via Docker:

```powershell
docker-compose up -d visitas-service
```

Ou manualmente:

```powershell
cd visitas-service
mvn quarkus:dev
```

O microserviço estará em **http://localhost:8081**

## Passo 5: Iniciar o Frontend Angular (porta 4200)

Em outro terminal:

```powershell
cd tela_ipen
npm install    # apenas na primeira vez
npm start
```

O frontend estará em **http://localhost:4200**

## Verificar se Está Funcionando

1. Abra http://localhost:8080/prisoners/list - deve retornar JSON com lista de prisioneiros
2. Abra http://localhost:8081/health - deve retornar status "UP"
3. Abra http://localhost:4200 - deve carregar a interface

## Estrutura do Sistema

```
Sistema Prisional/
├── Backend Principal (Quarkus)  → localhost:8080 → MySQL (porta 3306)
├── Frontend (Angular)           → localhost:4200
├── Visitas Service (Quarkus)    → localhost:8081 → PostgreSQL (porta 5433)
└── Kafka + Zookeeper            → localhost:9092
```

## Parando Tudo

```powershell
# Parar containers Docker
docker-compose down

# Parar backend e frontend: Ctrl+C nos terminais
```

## Problemas Comuns

### Backend não inicia (erro de conexão ao banco)
- Verifique se o MySQL está rodando: `docker ps | findstr prisioneiro-db`
- Teste a conexão: `docker exec -it prisioneiro-db mysql -uroot -p1234 -e "SHOW DATABASES;"`

### Frontend não carrega dados
- Verifique se o backend está rodando em http://localhost:8080
- Abra o Console do navegador (F12) e veja se há erros de CORS ou conexão

### Porta já em uso
- Windows: `netstat -ano | findstr :8080` (ou :3306, :4200)
- Mate o processo ou use outra porta
