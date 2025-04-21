resource "aws_security_group" "franquicia_sg" {
  name = "franquicia-sg"

  ingress {
    from_port = 3306
    to_port   = 3306
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 22
    to_port   = 22
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 8080
    to_port   = 8080
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_instance" "franquicia_db" {
  identifier          = "franquicia-db"
  allocated_storage   = 20
  engine              = "mysql"
  engine_version      = "8.0"
  instance_class      = var.db_instance_class
  db_name             = var.db_name
  username            = var.db_user
  password            = var.db_password
  skip_final_snapshot = true
  publicly_accessible = true
  vpc_security_group_ids = [aws_security_group.franquicia_sg.id]
}

resource "aws_s3_bucket" "franquicia-api_bucket" {
  bucket        = var.s3_bucket
  force_destroy = true
}

resource "aws_s3_object" "franquicia_zip" {
  bucket = aws_s3_bucket.franquicia-api_bucket.bucket
  key    = "franquicia-api.zip"
  source = "franquicia-api.zip"  # Ruta local del archivo .zip
  acl    = "private"
}

resource "aws_instance" "franquicia_app" {
  ami           = var.ami_id
  instance_type = var.ec2_instance_type
  key_name      = var.app_key_pair
  security_groups = [aws_security_group.franquicia_sg.name]

  user_data = <<-EOF
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
              EOF


  tags = {
    Name = "franquicia-api-server"
  }
}

