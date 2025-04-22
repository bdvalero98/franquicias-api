#!/bin/bash
sudo apt update -y
sudo apt install -y docker.io docker-compose unzip
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker ubuntu

# Descargar y descomprimir el proyecto desde S3
aws s3 cp https://franquicia-api-deploy.s3.us-east-1.amazonaws.com/franquicia-api.zip /home/ubuntu/franquicia-api.zip
cd /home/ubuntu
unzip franquicia-api.zip
cd franquicia-api

# Ejecutar docker-compose
docker-compose up -d
