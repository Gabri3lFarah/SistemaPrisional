.PHONY: help build up down logs clean dev-up dev-down rebuild build-apps

help: ## Show this help message
	@echo 'Usage: make [target]'
	@echo ''
	@echo 'Available targets:'
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'

build-apps: ## Build Java applications (required before docker build)
	@echo "Building Java applications..."
	mvn clean package -DskipTests -B
	mvn -f visitas-service clean package -DskipTests -B
	@echo "✓ Applications built successfully"

build: build-apps ## Build Java apps and Docker images
	docker compose build

up: ## Start all services (builds apps first if needed)
	@if [ ! -d "target/quarkus-app" ]; then \
		echo "Building applications first..."; \
		make build-apps; \
	fi
	docker compose up -d

down: ## Stop all services
	docker compose down

logs: ## View logs from all services
	docker compose logs -f

clean: ## Stop all services and remove volumes
	docker compose down -v

dev-up: ## Start only infrastructure services for development
	docker compose -f docker-compose.dev.yml up -d

dev-down: ## Stop development infrastructure services
	docker compose -f docker-compose.dev.yml down

rebuild: ## Rebuild and restart all services
	docker compose down
	make build-apps
	docker compose build --no-cache
	docker compose up -d

status: ## Show status of all services
	docker compose ps

prisioneiro-logs: ## View logs from prisioneiro-core
	docker compose logs -f prisioneiro-core

visitas-logs: ## View logs from visitas-service
	docker compose logs -f visitas-service

frontend-logs: ## View logs from frontend
	docker compose logs -f tela-ipen

db-shell: ## Connect to MySQL database
	docker compose exec prisioneiro-db mysql -u root -p1234 sistema_prisional

visitas-db-shell: ## Connect to PostgreSQL database
	docker compose exec visitas-db psql -U postgres visitas_db
