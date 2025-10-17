variable "repository_name" {
  type        = string
  description = "Name of the ECR repository"
}

variable "env" {
  type        = string
  description = "Environment (e.g., prod, uat)"
}


variable "keep_last" {
  type = number
  default = 1
}
variable "untagged_days" {
  type = number
  default = 1
}


resource "aws_ecr_repository" "this" {
  name                 = "${var.repository_name}-${var.env}"
  image_tag_mutability = "MUTABLE"
  force_delete = true
  tags = {
    Environment = var.env
  }
}


resource "aws_ecr_lifecycle_policy" "this" {
  repository = aws_ecr_repository.this.name
  policy     = jsonencode({
    rules = [
      {
        rulePriority = 1
        description  = "Expire untagged images older than ${var.untagged_days} days"
        selection = {
          tagStatus   = "untagged"
          countType   = "sinceImagePushed"
          countUnit   = "days"
          countNumber = var.untagged_days
        }
        action = { type = "expire" }
      },
      {
        rulePriority = 10
        description  = "Keep last ${var.keep_last} images (any tag)"
        selection = {
          tagStatus   = "any"
          countType   = "imageCountMoreThan"
          countNumber = var.keep_last
        }
        action = { type = "expire" }
      }
    ]
  })
}

output "repository_url" {
  value = aws_ecr_repository.this.repository_url
}