variable "repository_name" {
  type        = string
  description = "Name of the ECR repository"
}

variable "env" {
  type        = string
  description = "Environment (e.g., prod, uat)"
}

resource "aws_ecr_repository" "this" {
  name                 = "${var.repository_name}-${var.env}"
  image_tag_mutability = "MUTABLE"
  tags = {
    Environment = var.env
  }
}

output "repository_url" {
  value = aws_ecr_repository.this.repository_url
}