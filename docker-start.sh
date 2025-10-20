#!/bin/bash

# Script para iniciar los contenedores Docker de TaskFlow

echo "🐳 Iniciando contenedores Docker para TaskFlow..."

# Verificar si Docker está corriendo
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker no está corriendo. Por favor, inicia Docker Desktop."
    exit 1
fi

# Iniciar contenedores
echo "📦 Iniciando MySQL y phpMyAdmin..."
docker-compose up -d

# Esperar a que MySQL esté listo
echo "⏳ Esperando a que MySQL esté listo..."
sleep 10

# Verificar estado de los contenedores
echo "📊 Estado de los contenedores:"
docker-compose ps

echo ""
echo "✅ Contenedores iniciados correctamente!"
echo ""
echo "🔗 Servicios disponibles:"
echo "   • MySQL: localhost:3307"
echo "   • phpMyAdmin: http://localhost:8081"
echo ""
echo "📋 Credenciales:"
echo "   • Usuario: taskflow_user"
echo "   • Contraseña: taskflow_pass"
echo "   • Base de datos: taskflow_db"
echo ""
echo "🛑 Para detener los contenedores: ./docker-stop.sh"
