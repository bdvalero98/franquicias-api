resource "aws_db_instance" "franquicia_db" {
  identifier             = "franquicia-db"
  allocated_storage      = 20
  engine                 = "mysql"
  engine_version         = "8.0"
  instance_class         = var.db_instance_class
  db_name                = var.db_name
  username               = var.db_user
  password               = var.db_password
  skip_final_snapshot    = true
  publicly_accessible    = true
  vpc_security_group_ids = [aws_security_group.franquicia_sg.id]
}

resource "aws_security_group" "franquicia_sg" {
  name = "franquicia-sg"

  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "franquicia_app" {
  ami                    = var.ami_id
  instance_type          = var.ec2_instance_type
  key_name               = var.app_key_pair
  security_groups        = [aws_security_group.franquicia_sg.name]

  user_data = <<-EOF
              #!/bin/bash
              yum update -y
              yum install -y docker git
              service docker start
              usermod -a -G docker ubuntu
              docker run -d -p 8080:8080 --name franquicia-api \
                -e SPRING_DATASOURCE_URL=jdbc:mysql://${aws_db_instance.franquicia_db.address}:3306/${var.db_name} \
                -e SPRING_DATASOURCE_USERNAME=${var.db_user} \
                -e SPRING_DATASOURCE_PASSWORD=${var.db_password} \
                bdvalero/franquicia-api:latest
              EOF

  tags = {
    Name = "franquicia-api-server"
  }
}
