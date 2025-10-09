terraform {
  backend "s3" {
    bucket         = "terraform-state-taskapp"
    key            = "state/uat/terraform.tfstate"  # Key for prod
    region         = "eu-west-3"
    dynamodb_table = "terraform-lock"
    encrypt        = true
  }
}

provider "aws" {
  region = "eu-west-3"
}
