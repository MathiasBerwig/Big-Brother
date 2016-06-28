DROP DATABASE IF EXISTS ponto;
CREATE DATABASE ponto;
USE ponto;

-- TABELA usuarios
DROP TABLE IF EXISTS usuarios;
CREATE TABLE usuarios (
 tag VARCHAR(10) NOT NULL,
 nome VARCHAR(64) NOT NULL,
 foto VARCHAR(256) NOT NULL,
 PRIMARY KEY (tag)
);

-- TABELA registros
DROP TABLE IF EXISTS registros;
CREATE TABLE registros (
 id_registro INT NOT NULL AUTO_INCREMENT,
 tag VARCHAR(10) NOT NULL,
 data_hora TIMESTAMP,
 PRIMARY KEY (id_registro),
 FOREIGN KEY (tag) REFERENCES usuarios (tag)
);