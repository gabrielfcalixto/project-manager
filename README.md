# Project Manager

Project Manager é um sistema de gerenciamento de projetos desenvolvido com Java 21, Spring Boot e PostgreSQL. Ele oferece funcionalidades para controle de projetos, tarefas, horas trabalhadas e gerenciamento de usuários, com autenticação e autorização via JWT.

## Tecnologias Utilizadas
- Java 21
- Spring Boot 3.4.2
- MySQL
- Spring Data JPA
- Spring Security
- JWT (Json Web Token)
- Lombok
- Springdoc OpenAPI
- Maven

## Configuração do Ambiente

1. **Clone o Repositório:**
```bash
git https://github.com/gabrielfcalixto/project-manager.git
```

2. **Configure o Banco de Dados:**
Certifique-se de ter o MySQL instalado e crie um banco de dados:
```sql
CREATE DATABASE projeto;
```

3. **Configure o `application.properties` ou `application.yml`:**
```properties
# Configurações para usar o banco de dados MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/projeto?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect


# Configurações do Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.defer-datasource-initialization=true

# Desabilita a execução automática de data.sql
spring.sql.init.mode=always

# Configurações do JWT
projeto.jwtSecret=bxOksa8BHgdAhR80Y3pEYvS5M+MnF2sheFDqprkTqQ4odqoszJLW1ikw64/nT/dTvlgrcBTq7HfK1B9Gai2h5A==
projeto.jwtExpirationMs=9000000


# Configurações do servidor de e-mail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=example@gmail.com
spring.mail.password=tttt tttt tttt tttt (coloque a senha de aplicativo aqui)
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true


## Como Executar

1. **Compile o Projeto:**
```bash
mvn clean install
```
2. **Execute a Aplicação:**
```bash
mvn spring-boot:run
```
A aplicação estará disponível em: [http://localhost:8080](http://localhost:8080)

## Documentação da API

A documentação interativa (Swagger) está disponível em:
[http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

### Autenticação
Utilize o endpoint `/auth/login` para obter o token JWT:
```json
POST /auth/login
{
  "username": "admin",
  "password": "admin123"
}
```
O token JWT deve ser usado no cabeçalho `Authorization` para acessar os demais endpoints:
```http
Authorization: Bearer <seu_token_jwt>
```

## Gerando Artefato

Caso queira gerar o arquivo `.jar` do projeto:
```bash
mvn package
```
O arquivo gerado ficará em `target/project-manager-0.0.1-SNAPSHOT.jar` e pode ser executado com:
```bash
java -jar target/project-manager-0.0.1-SNAPSHOT.jar
```

## Contribuindo
1. Fork o repositório.
2. Crie uma nova branch:
```bash
git checkout -b feature/nova-funcionalidade
```
3. Faça suas alterações e comite:
```bash
git commit -m "Adiciona nova funcionalidade X"
```
4. Envie para seu fork:
```bash
git push origin feature/nova-funcionalidade
```
5. Abra um Pull Request.

## Licença
Este projeto é licenciado sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---
Desenvolvido com por Gabriel 🚀.

