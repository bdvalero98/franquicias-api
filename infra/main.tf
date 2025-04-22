provider "aws" {
  region = var.region
}

resource "aws_instance" "franquicias_api" {
  ami           = var.ami_id
  instance_type = var.instance_type
  key_name      = var.key_pair_name

  user_data = file("${path.module}/user_data.sh")

  tags = {
    Name        = "FranquiciasAPIInstances"
    Environment = "production"
    Project     = "franquicias"
    Terraform   = "true"
  }

  vpc_security_group_ids = [aws_security_group.franquicias_sg.id]
  associate_public_ip_address = true
}

resource "aws_security_group" "franquicias_sg" {
  name        = "franquicias-sg"
  description = "Permitir trafico HTTP y SSH"
  vpc_id      = data.aws_vpc.default.id

  lifecycle {
    create_before_destroy = true
  }

  ingress {
    description = "Permitir HTTP"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Permitir SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "Permitir todo trafico saliente"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "franquicias-sg"
  }
}

data "aws_vpc" "default" {
  default = true
}
