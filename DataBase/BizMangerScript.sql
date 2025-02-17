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
select * from order_items;
UPDATE products SET quantity = (quantity+10) WHERE id1;
update employees set email ='joao@gmail.com'where id =3;
SELECT * FROM orders WHERE responsible_id=1;

SELECT * FROM stock WHERE responsible_id=1;


SELECT 
    CASE 
        WHEN (stock.max_capacity - COALESCE(SUM(products.quantity), 0)) > 0 THEN 1
        ELSE 0
    END AS 'Espaço'
FROM stock
LEFT JOIN products ON stock.id = products.stock_id
WHERE stock.id = 1;

SELECT CASE
	WHEN(products.quantity-10)>0 THEN 1
    ELSE 0
    END AS 'Produto Qtd'
 FROM products
WHERE id=:id;


SELECT 
    CASE 
        WHEN (new_quantity - COALESCE(SUM(products.quantity), 0)) > 0 THEN 1
        ELSE 0
    END AS 'Espaço'
FROM stock
LEFT JOIN products ON stock.id = products.stock_id
WHERE stock.id = 1;


SELECT
    p.id AS product_id,
    p.name AS product_name,
    p.quantity,
    p.stock_id
FROM
    products p
ORDER BY
    p.quantity ASC
LIMIT 7;


SELECT
    p.id AS product_id,
    p.name AS product_name,
    p.quantity,
    p.stock_id,
    s.company_id,
    CASE
        WHEN p.quantity < 10 THEN 'Baixo'
        WHEN p.quantity BETWEEN 10 AND 50 THEN 'Médio'
        ELSE 'Alto'
    END AS volume_estoque
FROM
    products p
JOIN
    stock s ON p.stock_id = s.id
WHERE s.company_id=1
	ORDER BY
    p.quantity ASC
LIMIT 7;

SELECT p.id AS product_id, p.name AS product_name,p.category, p.quantity, p.stock_id, s.company_id, CASE WHEN p.quantity < 10 THEN 'Baixo' WHEN p.quantity BETWEEN 10 AND 50 THEN 'Médio' ELSE 'Alto' END AS stock_volume FROM products p JOIN stock s ON p.stock_id = s.id WHERE s.company_id=:id ORDER BY p.quantity ASC LIMIT 7;
