terraform {
  backend "s3" {
    bucket         = "terraform-state-taskapp"
    key            = "state/prod/terraform.tfstate"  # Key for prod
    region         = "eu-west-3"
    dynamodb_table = "terraform-lock"
    encrypt        = true
  }
}


provider "aws" {
  region = "eu-west-3"
}

module "vpc_prod" {
  source = "../modules/vpc"
  env    = "prod"
}

module "eks_prod" {
  source = "../modules/eks"
  env    = "prod"
  vpc_id = module.vpc_prod.vpc_id
  private_subnets = module.vpc_prod.private_subnets
}

module "rds_prod" {
  source = "../modules/rds"
  env    = "prod"
  vpc_id = module.vpc_prod.vpc_id
  private_subnets = module.vpc_prod.private_subnets
  db_password = "postgres123"
  eks_cluster_security_group_id = module.eks_prod.cluster_security_group_id
}