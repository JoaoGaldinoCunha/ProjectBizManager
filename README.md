# Projeto Angular + Spring Boot com OAuth2, Spring Security e MySQL

Este projeto implementa um sistema de autenticação utilizando **OAuth2**, com **Spring Boot** no backend e **Angular** no frontend, integrados ao **MySQL**.

## Tecnologias Usadas

- **Frontend**: Angular
- **Backend**: Spring Boot
- **Banco de Dados**: MySQL
- **Autenticação**: OAuth2 + Spring Security

## Pré-Requisitos

- **Java 11+**
- **Maven**
- **MySQL 5.7+**
- **Node.js e Angular CLI**

## Passos para Execução

### 1. Banco de Dados

1. Crie o banco de dados no MySQL:
   O script para criação e configuração do banco de dados está disponível no seguinte caminho:
   `database/scriptFinish.sql`

   ```sql
   CREATE DATABASE BizManager;
   USE BizManager;
   CREATE TABLE roles (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(255) UNIQUE NOT NULL
   );

   INSERT INTO roles (name) VALUES ('Admin'), ('Company'), ('Employees');

   CREATE TABLE company (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       email VARCHAR(255) UNIQUE NOT NULL,
       name VARCHAR(255) NOT NULL,
       phone VARCHAR(15) NOT NULL,
       password VARCHAR(255) NOT NULL,
       cnpj VARCHAR(14) UNIQUE NOT NULL,
       cep VARCHAR(8) NOT NULL,
       street VARCHAR(60) NOT NULL,
       neighborhood VARCHAR(60) NOT NULL,
       city VARCHAR(60) NOT NULL,
       state VARCHAR(120) NOT NULL,
       complement VARCHAR(15),
       number VARCHAR(20) NOT NULL,
       role_id BIGINT DEFAULT 1,
       FOREIGN KEY (role_id) REFERENCES roles(id)
   );

   CREATE TABLE employees (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
       birth_date DATE NOT NULL,
       email VARCHAR(255) UNIQUE NOT NULL,
       password VARCHAR(255) NOT NULL,
       company_id BIGINT,
       role_id BIGINT DEFAULT 2,
       FOREIGN KEY (company_id) REFERENCES company(id),
       FOREIGN KEY (role_id) REFERENCES roles(id)
   );

   CREATE TABLE stock (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
       company_id BIGINT,
       responsible_id BIGINT,
       max_capacity INT NOT NULL,
       stock_type VARCHAR(20) CHECK (stock_type IN ('Frigorifico', 'Geral', 'Seco', 'Liquido')),
       created_at DATE,
       FOREIGN KEY (company_id) REFERENCES company(id),
       FOREIGN KEY (responsible_id) REFERENCES employees(id)
   );

   CREATE TABLE products (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       stock_id BIGINT,
       name VARCHAR(255) NOT NULL,
       description TEXT,
       category VARCHAR(20) NOT NULL CHECK (category IN ('Unidade', 'Litro', 'Quilograma', 'Metro', 'Caixa', 'Pacote', 'Galão')),
       expiration_date DATE,
       purchase_price DECIMAL(10, 2) NOT NULL,
       quantity INT NOT NULL,
       FOREIGN KEY (stock_id) REFERENCES stock(id)
   );

   CREATE TABLE orders (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       responsible_id BIGINT,
       client_cnpj VARCHAR(20) NOT NULL,
       destination VARCHAR(255) NOT NULL,
       status VARCHAR(20) NOT NULL CHECK (status IN ('Pendente', 'Negado', 'Enviado', 'Entregue')),
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (responsible_id) REFERENCES employees(id)
   );

   CREATE TABLE order_items (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       order_id BIGINT,
       product_id BIGINT,
       quantity INT NOT NULL,
       FOREIGN KEY (order_id) REFERENCES orders(id),
       FOREIGN KEY (product_id) REFERENCES products(id)
   );

   select * from roles;
   select * from company;
   select * from employees;
   select * from stock;
   select * from products;
   select * from orders;

## 2. 💻 Backend (Spring Boot)

Navegue até o diretório do Backend:

   ```bash
   cd Backend
   ```

Abra o arquivo application.properties e configure as informações do banco de dados:

```bash
properties
spring.datasource.url=jdbc:mysql://localhost:3306/BizManager
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
````
Compile e execute o backend:

## 3. 🌐 Frontend (Angular)

Navegue até o diretório do frontend:

   ```bash
   cd Frontend
   ```
Instale as dependências do Angular:
```bash
npm install
```

Execute o servidor do Angular:
bash
```ng serve```
O frontend estará disponível em http://localhost:4200.

## 📑 Uso
Após realizar a instalação e configuração, você pode acessar a aplicação frontend em http://localhost:4200. Utilize as credenciais configuradas para autenticação OAuth2 e faça login para acessar as funcionalidades do sistema.


