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

module "s3_prod" {
  source     = "../modules/s3"
  bucket_name = "taskapp-frontend"
  env        = "prod"
}

module "ecr_prod" {
  source          = "../modules/ecr"
  repository_name = "taskapp-back"
  env             = "prod"
}

output "ecr_prod_url" {
  value = module.ecr_prod.repository_url
}


module "iam_github_oidc" {
  source = "../modules/iam-github-oidc"

  create_oidc_provider = false
  oidc_provider_arn    = "arn:aws:iam::864230187726:oidc-provider/token.actions.githubusercontent.com"

  role_name  = "taskapp-ci-cd"
  repo_owner = "khaitq28"
  repo_name  = "taskapp"
  branch     = "main"

  ecr_repo_arn = "arn:aws:ecr:eu-west-3:864230187726:repository/taskapp-back-prod"

  allow_s3               = true
  s3_bucket_arn          = "arn:aws:s3:::taskapp-frontend-prod"
  allow_cloudfront = false
}



output "gh_actions_role_arn" {
  value = module.iam_github_oidc.role_arn
}
