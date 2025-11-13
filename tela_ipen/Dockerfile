####
# This Dockerfile is used to build the tela_ipen (Angular frontend) container image.
# It uses a multi-stage build to optimize the final image size.
####

## Stage 1: Build the Angular application
FROM node:20-alpine AS build
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm ci

# Copy source code and build the application
COPY . .
RUN npm run build

## Stage 2: Serve the application with nginx
FROM nginx:1.25-alpine
WORKDIR /usr/share/nginx/html

# Remove default nginx static assets
RUN rm -rf ./*

# Copy built application from build stage
COPY --from=build /app/dist/tela_ipen/browser ./

# Copy custom nginx configuration if needed
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
