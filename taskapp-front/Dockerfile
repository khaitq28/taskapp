## Production Dockerfile for AWS EKS
## This builds the React app with production environment variables
#
## Stage 1: Build by node 20.17.0
#FROM node:20.17.0 AS builder
#WORKDIR /app
#COPY package.json package-lock.json ./
#RUN npm install
#COPY . .
#
## Build for production (uses .env.production)
#RUN npm run build
#
## Stage 2: Serve
#FROM nginx:alpine
#COPY --from=builder /app/dist /usr/share/nginx/html
#EXPOSE 80
#CMD ["nginx", "-g", "daemon off;"]


# Dockerfile for local dev/test with NGINX (not for prod - prod uses S3/CloudFront)
# Builds React app (Vite) with no baked env vars, config loaded runtime via /config.json

# Stage 1: Build
FROM node:20.17.0 AS builder
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build  # No env vars baked - config loaded runtime

# Stage 2: Serve (local/test only)
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]