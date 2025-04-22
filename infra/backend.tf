terraform {
  backend "s3" {
    bucket         = "franquicias-api-tf-state"
    key            = "terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform-locks"
    encrypt        = true
  }
}
