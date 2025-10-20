-- Script de inicialización para TaskFlow Database
-- Este script se ejecuta automáticamente cuando se crea el contenedor

-- Crear base de datos de desarrollo
CREATE DATABASE IF NOT EXISTS taskflow_dev;
CREATE DATABASE IF NOT EXISTS taskflow_test;

-- Crear usuario para desarrollo (si no existe)
CREATE USER IF NOT EXISTS 'dev_user'@'%' IDENTIFIED BY 'dev_pass';
CREATE USER IF NOT EXISTS 'test_user'@'%' IDENTIFIED BY 'test_pass';

-- Otorgar permisos
GRANT ALL PRIVILEGES ON taskflow_db.* TO 'taskflow_user'@'%';
GRANT ALL PRIVILEGES ON taskflow_dev.* TO 'dev_user'@'%';
GRANT ALL PRIVILEGES ON taskflow_test.* TO 'test_user'@'%';

-- Aplicar cambios
FLUSH PRIVILEGES;

-- Mostrar información de las bases de datos creadas
SHOW DATABASES;
