## Fullstack project: TaskApp deploy to AWS by Helm and ArgoCD
### - Backend: Spring boot  + React + PostgresSQL
### - Security: JWT + OAuth2 Google
### - Local test with Docker compose
### - CI by Github actions
### - CD by ArgoCD to EKS
### - Infra as code by Terraform
### - Infra AWS: ECR + EKS + RDS + S3 + IAM + VPC + CloudWatch + CloudFront

## Helm:

+ Create helm chart for backend service

`cd infra/k8s/helm
`

`  helm create backend
`


#### When create EKS eks-prod-taskapp by terraform (done)

+ To add eks cluster to kubeconfig

`  aws eks --region us-east-1 update-kubeconfig --name eks-prod-taskapp
`
+ To list all contexts:

`  kubectl config get-contexts
`
+ To select default context:

`  kubectl config use-context arn:aws:eks:us-east-1:123456789012:cluster/eks-prod-taskapp`


+ By kubeconfig, helm can connect to eks cluster


+ Test helm with install dry command: (no real install)

`  helm install backend-test ./backend --dry-run
`


## Multi env:
+ Create different values.yaml file for each env: values-uat.yaml, values-prod.yaml

+ Terraform create diff eks for each env: eks-uat-taskapp, eks-prod-taskapp
+ Kubeconfig add context for each env:

`aws eks update-kubeconfig --region eu-west-3 --name eks-uat-taskapp --alias uat-context
`

`
aws eks update-kubeconfig --region eu-west-3 --name eks-prod-taskapp --alias prod-context
`


### Helm deploy:

+ For uat env:

`kubectl config use-context uat-context`

`helm install backend-uat ./backend -f values-uat.yaml`

+ For prod env:

`kubectl config use-context prod-context`

`helm install backend-prod ./backend -f values-prod.yaml
`

### Check list:

`helm list
`

### Auto with ArgoCD

ArgoCD manages contexts by Application config. 
For ex: 
+ Application for UAT  point to uat-context, 
+ Application for Prod points to prod-context.

