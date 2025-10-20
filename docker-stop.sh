#!/bin/bash

# Script para detener los contenedores Docker de TaskFlow

echo "🛑 Deteniendo contenedores Docker de TaskFlow..."

# Detener contenedores
docker-compose down

echo "✅ Contenedores detenidos correctamente!"
echo ""
echo "💡 Para eliminar también los volúmenes (datos): docker-compose down -v"
