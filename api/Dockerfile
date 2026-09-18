# Dockerfile
# Dockerfile para a API Node.js do projeto Zombie Plus.
FROM node:20-alpine

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia package.json e package-lock.json
COPY package*.json ./

# Instala ferramentas de compilação e dependências
RUN apk add --no-cache python3 make g++ curl && npm ci

# Copia o restante do código da API
COPY . ./

# Expõe a porta da API
EXPOSE 3333

# Executa migrações, seeds e inicia a API
CMD ["sh", "-c", "npx sequelize db:migrate && npx sequelize db:seed:all && npm run dev"]
