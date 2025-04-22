terraform {
  backend "s3" {
    bucket         = "franquicia-api-terraform-state"
    key            = "state/terraform.tfstate"
    region         = "us-east-1"
    encrypt        = true
    dynamodb_table = "terraform-locks"
    use_lockfile = true
  }
}
