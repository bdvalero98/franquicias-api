#!/bin/bash

# Actualizar el sistema y configurar el ambiente
sudo apt update -y
sudo apt install -y docker.io docker-compose unzip curl

# Instalar AWS CLI v2
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

# Verificar instalación de AWS CLI
aws --version

# Iniciar y habilitar Docker
sudo systemctl start docker
sudo systemctl enable docker

# Añadir el usuario ubuntu al grupo docker
sudo usermod -aG docker ubuntu

# Descargar y descomprimir el proyecto desde S3
aws s3 cp https://franquicia-api-deploy.s3.us-east-1.amazonaws.com/franquicia-api.zip /home/ubuntu/franquicia-api.zip
cd /home/ubuntu
unzip franquicia-api.zip
cd franquicia-api

# Login a ECR (sin interacción)
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin ***.dkr.ecr.us-east-1.amazonaws.com

# Extraer la última imagen desde ECR
docker pull ***.dkr.ecr.us-east-1.amazonaws.com/franquicias-api-ecr:latest

# Detener contenedor previo (si existe)
docker stop franquicia-api || true
docker rm franquicia-api || true

# Iniciar nuevo contenedor
docker run -d -p 8080:8080 --name franquicia-api ***.dkr.ecr.us-east-1.amazonaws.com/franquicias-api-ecr:latest

# Comprobar si Docker y Docker Compose están funcionando correctamente
docker --version
docker-compose --version

echo ">>> Despliegue completado exitosamente."
