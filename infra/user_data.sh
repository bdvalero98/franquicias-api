#!/bin/bash
sudo apt update -y
sudo apt install -y docker.io awscli
sudo systemctl start docker
sudo systemctl enable docker

# Login a ECR
aws ecr get-login-password --region us-east-1 | \
docker login --username AWS --password-stdin 051826737831.dkr.ecr.us-east-1.amazonaws.com

# Descargar y ejecutar la imagen
docker pull 051826737831.dkr.ecr.us-east-1.amazonaws.com/franquicias-api-ecr:latest
docker run -d -p 8080:8080 \
  --env-file /home/ubuntu/.env \
  051826737831.dkr.ecr.us-east-1.amazonaws.com/franquicias-api-ecr:latest
