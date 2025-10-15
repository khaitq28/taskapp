

## $\textcolor{Green}{\textsf{ Fullstack project: TaskApp deploy to AWS by Helm and ArgoCD}}$	



#### - Backend: Spring boot  + React + PostgresSQL
#### - Security: JWT + OAuth2 Google
#### - Local test with Docker compose
#### - CI by Github actions
#### - CD by ArgoCD to EKS
#### - Infra as code by Terraform
#### - Infra AWS: 
- ECR + EKS for Backend deployement in Kubernetes 
- RDS for database postgres
- S3 for webapp react
- IAM + VPC + CloudWatch + CloudFront


With a real project we have separate environments: DEV, UAT, PRO 

----------------------------------------------------------------------------

## $\textcolor{Green}{\textsf{ Local}}$	

Windows : 

`docker-compose up -d `

Macos/Linux

`docker compose up -d`

Webapp url: http://localhost

Web API swagger url:   http://localhost:8080/taskapp/swagger-ui/index.html 

----------------------------------------------------------------------------
## $\textcolor{Green}{\textsf{ Infra with Terraform  }}$	

[Here](https://github.com/khaitq28/taskapp/tree/main/infra) is the repo of terraform scripts. IaC help us to create/destroy quickly necessary infra for multiple env. 

- We have 2 env PROD and UAT, each contains main.tf file, which will by apply and link to multiple modules: VPC, EKS, S3, RDS...
- To create all infra AWS for PROD: 

`cd prod  `

`terraform init  `

`terraform plan`

`terraform apply 
`

Then "yes" to confirm

Add new created eks cluster to kubeconfig:

`aws eks update-kubeconfig --region eu-west-3 --name eks-prod-taskapp `


From now helm and kubectl can connect to eks cluster

-------------------------------

Login to AWS ECR to push docker image

`aws ecr get-login-password --region eu-west-3 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.eu-west-3.amazonaws.com`


Build and tag image:
`docker build -t backend . `

Tag image:
`docker tag backend:latest <account-id>.dkr.ecr.eu-west-3.amazonaws.com/backend:latest
`

Push image to ECR:
`docker push <account-id>.dkr.ecr.eu-west-3.amazonaws.com/backend:latest
`

----------------------------------------------------------------------------
## $\textcolor{Green}{\textsf{ Helm  }}$	

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

Push image to ECR/DockerHub to test.

+ Test real install:

`
   helm install backend-test ./backend -f backend/values-prod.yaml
`

Then check with:

`  helm list
`

`  kubectl get pods
`

Test web api:

http://xxxx.eu-west-3.elb.amazonaws.com/taskapp/swagger-ui/index.htm

Replace by your real elb url

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

### CD by ArgoCD

ArgoCD manages contexts by Application config. 
For ex: 
+ Application for UAT  point to uat-context, 
+ Application for Prod points to prod-context.


### $\textcolor{Green}{\textsf{CI by Github actions}}$	
+ Github action (VM)
  * connect to AWS by OIDC (OpenID Connect) token instead of storing accessKey + accessSecret in Github secret:
  * build and push backend image to ECR
  * build and push frontend/dist to S3  
  * invalidate cache cloudfront (optional)
 
Terraform destroy all: 

* kubectl delete ingress --all -A || true; kubectl delete svc --all -A || true; kubectl delete deploy --all -A || true

* cd infra/prod && terraform destroy

