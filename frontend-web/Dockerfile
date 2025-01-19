# Stage 1: Build the Angular application with a compatible Node.js version
FROM node:18.19 AS build

WORKDIR /usr/src/app

# Install dependencies
COPY package*.json ./

# Copy the source code
COPY . .

RUN npm install
RUN npm run build -- --configuration=production

# Stage 2: Serve the application using NGINX
FROM nginx:alpine
COPY --from=build /usr/src/app/dist/news-publishing-app/browser /usr/share/nginx/html

COPY nginx.conf /etc/nginx/nginx.conf

# Expose port
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
