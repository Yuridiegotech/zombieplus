# Dockerfile
# Dockerfile para a API Node.js (React + Express) do projeto Zombie Plus.
# Ele instala as dependências e deixa o comando "npm run dev" disponível,
# exatamente como o script batch que você usa localmente.

# versão LTS que já suporta npm 9
FROM node:20-alpine

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia apenas o package.json e o lock – isso permite cache das dependências
COPY package*.json ./

# Instala as dependências (sem devDependencies, pois o CI não precisa de tooling)
RUN npm ci

# Copia o restante do código da API
COPY . ./

# Expõe a porta que a API usa
EXPOSE 3333

# Comando que será executado ao iniciar o container
CMD ["npm", "run", "dev"]
