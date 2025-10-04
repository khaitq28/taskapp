# TaskApp Configuration Guide

This guide explains how to configure the TaskApp for different environments: local Docker Compose and AWS EKS production deployment.

## Environment Configuration

### Local Development (Docker Compose)

1. **Update Google OAuth2 Configuration:**
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new OAuth2 client or use existing one
   - Add authorized redirect URIs:
     - `http://localhost:8080/taskapp/login/oauth2/code/google`
   - Update `docker-compose.yml` with your Google Client ID and Secret

2. **Run the application:**
   ```bash
   docker-compose up --build
   ```

### Local Development (React Only)

1. **React app automatically uses `.env.local` file:**
   ```bash
   cd taskapp-front
   npm install
   npm run dev
   ```

### Production (AWS EKS)

1. **Update production environment:**
   - Edit `taskapp-front/.env.production` with your production URLs
   - Update `k8s/configmap.yaml` with your production values
   - Update `k8s/backend-deployment.yaml` and `k8s/frontend-deployment.yaml` with your domain
   - Update `k8s/ingress.yaml` with your domain names

2. **Build production images:**
   ```bash
   # Build backend
   docker build -t taskapp-back:production ./taskapp-back
   
   # Build frontend (uses .env.production)
   docker build -t taskapp-front:production ./taskapp-front
   ```

3. **Create secrets and deploy:**
   ```bash
   kubectl apply -f k8s/configmap.yaml
   kubectl create secret generic taskapp-secrets \
     --from-literal=SPRING_DATASOURCE_PASSWORD=your-secure-password \
     --from-literal=GOOGLE_CLIENT_ID=your-google-client-id \
     --from-literal=GOOGLE_CLIENT_SECRET=your-google-client-secret
   
   kubectl apply -f k8s/backend-deployment.yaml
   kubectl apply -f k8s/frontend-deployment.yaml
   kubectl apply -f k8s/ingress.yaml
   ```

## Environment Files

### React App Environment Files

The React app uses Vite's built-in environment file loading:

- **`.env.local`** - Automatically loaded in development mode
- **`.env.production`** - Automatically loaded when building for production

### Environment Variables

| Variable | Local (.env.local) | Production (.env.production) | Description |
|----------|-------------------|------------------------------|-------------|
| `VITE_API_BASE_URL` | `http://localhost:8080/taskapp/api/v1` | `https://api.your-domain.com/taskapp/api/v1` | API base URL |
| `VITE_BACKEND_BASE_URL` | `http://localhost:8080/taskapp` | `https://api.your-domain.com/taskapp` | Backend base URL |
| `VITE_FRONTEND_BASE_URL` | `http://localhost` | `https://your-domain.com` | Frontend base URL |
| `VITE_GOOGLE_OAUTH_URL` | `http://localhost:8080/taskapp/oauth2/authorization/google` | `https://api.your-domain.com/taskapp/oauth2/authorization/google` | Google OAuth URL |
| `VITE_OAUTH_POPUP_ORIGIN` | `http://localhost:8080` | `https://api.your-domain.com` | OAuth popup origin |

## Google OAuth2 Configuration

### Required Redirect URIs

**Local Development:**
- `http://localhost:8080/taskapp/login/oauth2/code/google`

**Production:**
- `https://api.your-domain.com/taskapp/login/oauth2/code/google`

## Troubleshooting

### OAuth2 Issues

1. **Redirect URI mismatch:**
   - Ensure Google Console redirect URI matches your environment
   - Check that redirect URI in docker-compose.yml or Kubernetes config is correct

2. **CORS issues:**
   - Verify `FRONTEND_BASE_URL` is correctly set in backend configuration
   - Check browser console for CORS errors

### Docker Compose Issues

1. **Services not communicating:**
   - Use service names (`back`, `front`, `postgres`) for internal communication
   - Use `localhost` for external access

2. **Frontend not connecting to backend:**
   - Verify `.env.local` file exists and has correct URLs
   - Check that backend is accessible from frontend container

### React Build Issues

1. **Environment variables not available:**
   - Ensure `.env.local` or `.env.production` files exist
   - Check that variables start with `VITE_` prefix
   - Restart development server after changing environment files

2. **Build fails with undefined variables:**
   - Check that all required environment variables are set in the appropriate .env file

## Migration to AWS EKS

1. **Prepare your domain:**
   - Set up DNS records pointing to your EKS cluster
   - Configure SSL certificates (recommend cert-manager with Let's Encrypt)

2. **Update configuration:**
   - Replace placeholder domains in `taskapp-front/.env.production`
   - Replace placeholder domains in Kubernetes manifests
   - Update Google OAuth2 redirect URIs
   - Configure production database (RDS recommended)

3. **Deploy:**
   - Build and push Docker images to ECR
   - Apply Kubernetes manifests
   - Verify all services are running and accessible

## Security Considerations

- Never commit `.env` files to version control
- Use Kubernetes secrets for sensitive data in production
- Enable HTTPS in production
- Regularly rotate OAuth2 client secrets
- Use least privilege IAM roles for EKS
