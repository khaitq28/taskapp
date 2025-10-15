
variable "create_oidc_provider" {
  type        = bool
  default     = false
  description = "Set true only if the account has no GitHub OIDC provider yet."
}

variable "oidc_provider_arn" {
  type        = string
  default     = null
  description = "Existing GitHub OIDC provider ARN (if already created)."
}

variable "role_name" {
  description = "Name of the IAM role used by GitHub Actions (e.g., taskapp-ci-cd)"
  type        = string
}

variable "repo_owner" {
  description = "GitHub org/user (e.g., khaitq28)"
  type        = string
}

variable "repo_name" {
  description = "GitHub repository name (e.g., taskapp)"
  type        = string
}

variable "branch" {
  type        = string
  default     = "main"
}

variable "ecr_repo_arn" {
  type        = string
}

variable "allow_s3" {
  type        = bool
  default     = false
}

variable "s3_bucket_arn" {
  type        = string
  default     = null
}

variable "allow_cloudfront" {
  description = "Grant CloudFront invalidation permissions?"
  type        = bool
  default     = false
}

variable "cloudfront_distribution_id" {
  description = "CloudFront distribution ID to invalidate (if allow_cloudfront = true)"
  type        = string
  default     = null
}



locals {
  default_github_oidc_arn = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:oidc-provider/token.actions.githubusercontent.com"
}

resource "aws_iam_openid_connect_provider" "github" {
  count           = var.create_oidc_provider ? 1 : 0
  url             = "https://token.actions.githubusercontent.com"
  client_id_list  = ["sts.amazonaws.com"]
  thumbprint_list = ["6938fd4d98bab03faadb97b34396831e3780aea1"]
}



data "aws_iam_policy_document" "gha_trust" {
  statement {
    effect = "Allow"

    principals {
      type        = "Federated"
      identifiers = [
        coalesce(
          var.oidc_provider_arn,
          try(aws_iam_openid_connect_provider.github[0].arn, null),
          local.default_github_oidc_arn
        )
      ]
    }

    actions = ["sts:AssumeRoleWithWebIdentity"]

    condition {
      test     = "StringEquals"
      variable = "token.actions.githubusercontent.com:aud"
      values   = ["sts.amazonaws.com"]
    }

    condition {
      test     = "StringLike"
      variable = "token.actions.githubusercontent.com:sub"
      values   = ["repo:${var.repo_owner}/${var.repo_name}:ref:refs/heads/${var.branch}"]
    }
  }
}

resource "aws_iam_role" "gha_role" {
  name               = var.role_name
  assume_role_policy = data.aws_iam_policy_document.gha_trust.json
  description        = "OIDC role for GitHub Actions (repo ${var.repo_owner}/${var.repo_name}, branch ${var.branch})"
  tags = {
    Project = var.repo_name
    Owner   = var.repo_owner
    Use     = "GitHubActions"
  }
}

data "aws_caller_identity" "current" {}

data "aws_iam_policy_document" "ecr_push" {
  statement {
    sid     = "ECRAuth"
    effect  = "Allow"
    actions = ["ecr:GetAuthorizationToken"]
    resources = ["*"]
  }

  statement {
    sid     = "ECRPushPull"
    effect  = "Allow"
    actions = [
      "ecr:BatchGetImage",
      "ecr:BatchCheckLayerAvailability",
      "ecr:CompleteLayerUpload",
      "ecr:GetDownloadUrlForLayer",
      "ecr:InitiateLayerUpload",
      "ecr:PutImage",
      "ecr:UploadLayerPart",
      "ecr:DescribeRepositories",
      "ecr:ListImages"
    ]
    resources = [var.ecr_repo_arn]
  }
}

resource "aws_iam_policy" "ecr_push" {
  name        = "${var.role_name}-ecr-push"
  description = "Allow GitHub Actions to push Docker images to ECR repo"
  policy      = data.aws_iam_policy_document.ecr_push.json
}

resource "aws_iam_role_policy_attachment" "ecr_push_attach" {
  role       = aws_iam_role.gha_role.name
  policy_arn = aws_iam_policy.ecr_push.arn
}

data "aws_iam_policy_document" "s3_put" {
  count = var.allow_s3 && var.s3_bucket_arn != null ? 1 : 0

  statement {
    sid     = "S3PutObjects"
    effect  = "Allow"
    actions = [
      "s3:PutObject",
      "s3:PutObjectAcl",
      "s3:DeleteObject"
    ]
    resources = ["${var.s3_bucket_arn}/*"]
  }

  statement {
    sid     = "S3ListBucket"
    effect  = "Allow"
    actions = ["s3:ListBucket"]
    resources = [var.s3_bucket_arn]
  }
}

resource "aws_iam_policy" "s3_put" {
  count       = var.allow_s3 && var.s3_bucket_arn != null ? 1 : 0
  name        = "${var.role_name}-s3-put"
  description = "Allow GitHub Actions to upload frontend assets to S3"
  policy      = data.aws_iam_policy_document.s3_put[0].json
}

resource "aws_iam_role_policy_attachment" "s3_put_attach" {
  count      = var.allow_s3 && var.s3_bucket_arn != null ? 1 : 0
  role       = aws_iam_role.gha_role.name
  policy_arn = aws_iam_policy.s3_put[0].arn
}

data "aws_iam_policy_document" "cloudfront_invalidate" {
  count = var.allow_cloudfront && var.cloudfront_distribution_id != null ? 1 : 0

  statement {
    sid     = "InvalidateCF"
    effect  = "Allow"
    actions = [
      "cloudfront:CreateInvalidation",
      "cloudfront:GetDistribution",
      "cloudfront:GetInvalidation",
      "cloudfront:ListInvalidations"
    ]
    resources = ["*"]
  }
}

resource "aws_iam_policy" "cloudfront_invalidate" {
  count       = var.allow_cloudfront && var.cloudfront_distribution_id != null ? 1 : 0
  name        = "${var.role_name}-cloudfront-invalidate"
  description = "Allow GitHub Actions to create CloudFront invalidations"
  policy      = data.aws_iam_policy_document.cloudfront_invalidate[0].json
}

resource "aws_iam_role_policy_attachment" "cloudfront_invalidate_attach" {
  count      = var.allow_cloudfront && var.cloudfront_distribution_id != null ? 1 : 0
  role       = aws_iam_role.gha_role.name
  policy_arn = aws_iam_policy.cloudfront_invalidate[0].arn
}

output "role_arn" {
  description = "IAM Role ARN for GitHub Actions OIDC"
  value       = aws_iam_role.gha_role.arn
}
