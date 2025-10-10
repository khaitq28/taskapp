variable "env" {
  type = string
}

variable "vpc_id" {
  type = string
}

variable "private_subnets" {
  type = list(string)
}

variable "db_password" {
  type      = string
  sensitive = true
}

variable "eks_cluster_security_group_id" {
  type = string
}


resource "aws_db_subnet_group" "rds" {
  name       = "rds-subnet-group-${var.env}"
  subnet_ids = var.private_subnets
  tags = {
    Environment = var.env
  }
}


resource "aws_security_group" "rds" {
  name        = "rds-sg-${var.env}"
  description = "Security group for RDS ${var.env}"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    security_groups = [var.eks_cluster_security_group_id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Environment = var.env
  }
}


resource "aws_db_instance" "taskapp" {
  identifier             = "rds-${var.env}-taskapp"
  engine                 = "postgres"
  engine_version         = "15.7"
  instance_class         = "db.t3.micro"
  allocated_storage      = 20
  db_name                = "taskapp"
  username               = "postgres"
  password               = var.db_password
  db_subnet_group_name   = aws_db_subnet_group.rds.name
  vpc_security_group_ids = [aws_security_group.rds.id]
  skip_final_snapshot    = true
  publicly_accessible    = false

  tags = {
    Environment = var.env
  }
}