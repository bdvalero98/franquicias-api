output "rds_endpoint" {
  value = aws_db_instance.franquicia_db.endpoint
}

output "ec2_public_ip" {
  value = aws_instance.franquicia_app.public_ip
}
