#!/bin/bash
set -e

echo "======================================"
echo "Building Sistema Prisional"
echo "======================================"
echo ""

# Build prisioneiro-core (main application)
echo "[1/2] Building prisioneiro-core..."
mvn clean package -DskipTests -B
echo "✓ prisioneiro-core built successfully"
echo ""

# Build visitas-service (microservice)
echo "[2/2] Building visitas-service..."
mvn -f visitas-service clean package -DskipTests -B
echo "✓ visitas-service built successfully"
echo ""

echo "======================================"
echo "✓ All services built successfully!"
echo "======================================"
echo ""
echo "You can now run:"
echo "  docker compose up --build"
echo "or"
echo "  make up"
echo ""
