variable "aws_region" {
  description = "Región AWS donde se desplegarán los recursos"
  type        = string
}

variable "db_instance_class" {
  description = "Tipo de instancia RDS"
  type        = string
}

variable "db_name" {
  description = "Nombre de la base de datos"
  type        = string
}

variable "db_user" {
  description = "Usuario de la base de datos"
  type        = string
}

variable "db_password" {
  description = "Contraseña de la base de datos"
  type        = string
  sensitive   = true
}

variable "ec2_instance_type" {
  description = "Tipo de instancia EC2"
  type        = string
}

variable "app_key_pair" {
  description = "Nombre del par de llaves EC2"
  type        = string
}

variable "ami_id" {
  description = "AMI para la instancia EC2"
  type        = string
}
