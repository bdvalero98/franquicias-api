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

# Ejecutar docker-compose para levantar el contenedor
docker-compose up -d

# Comprobar si Docker y Docker Compose están funcionando correctamente
docker --version
docker-compose --version

echo ">>> Despliegue completado exitosamente."
