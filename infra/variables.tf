variable "region" {
  description = "Región de AWS"
  default     = "us-east-1"
}

variable "instance_type" {
  default = "t3.micro"
}

variable "key_pair_name" {
  description = "Nombre del key pair importado"
}

variable "ecr_repository_url" {
  description = "URL del repositorio ECR sin tag"
}

variable "ami_id" {
  description = "AMI ID de Ubuntu (ej. Ubuntu 22.04)"
}

variable "docker_image_tag" {
  description = "Tag de la imagen a ejecutar"
}
