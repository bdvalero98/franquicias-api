#!/bin/bash
exec > /var/log/user-data.log 2>&1

echo "### ACTUALIZANDO E INSTALANDO DEPENDENCIAS ###"
apt-get update -y
apt-get install -y docker.io unzip curl awscli

echo "### INICIANDO DOCKER ###"
systemctl start docker
systemctl enable docker

echo "### AGREGANDO USUARIO AL GRUPO DOCKER ###"
usermod -aG docker ubuntu

echo "### DESCARGANDO ZIP DESDE S3 ###"
cd /home/ubuntu
aws s3 cp s3://${var.s3_bucket}/franquicia-api.zip .
unzip franquicia-api.zip
chown -R ubuntu:ubuntu /home/ubuntu
chmod -R 755 /home/ubuntu

echo "### INSTALANDO DOCKER COMPOSE ###"
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

echo "### ESPERANDO A MYSQL (RDS) ###"
sleep 60

echo "### LEVANTANDO SERVICIO ###"
cd /home/ubuntu/franquicia-api || exit 1
docker-compose up -d
