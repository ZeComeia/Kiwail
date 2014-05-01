create schema Email;

use Email;

-- -----------------------------------------
-- -----------------------------------------
-- -------------Tabelas De Usuario----------
-- -----------------------------------------
-- -----------------------------------------
CREATE TABLE Usuario 
(
	ID INT PRIMARY KEY AUTO_INCREMENT,
	usuario VARCHAR(100) UNIQUE NOT NULL, -- o usuario nao pode repetir
	senha VARCHAR(20) NOT NULL
);

--
-- Tabela de Cadastro de emails do usuario
--
CREATE TABLE Cadastrado
(
	ID INT PRIMARY KEY AUTO_INCREMENT,
	ID_Usuario INT NOT NULL, 
	ID_Servidor INT NOT NULL,
	uri VARCHAR(50) NOT NULL,
	senha VARCHAR(50) NOT NULL,
	-- Foreign Key
	FOREIGN KEY (ID_Servidor) REFERENCES Servidor( ID ) 
	ON DELETE CASCADE
	ON UPDATE CASCADE, 

	FOREIGN KEY (ID_Usuario) REFERENCES Usuario( ID ) 
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

-- -----------------------------------------
-- -----------------------------------------
-- -------------Tabelas Globais-------------
-- -----------------------------------------
-- -----------------------------------------

--
-- Tabela Servidor:
--   Essa tabela tem como função armazenar os
--   dominios dos Servidores que o site poderá
--   utilizar.
-- Campos Exemplo:
--    ID              : 1
--    dominio		  : '@gmail.com'
--    uri_SMTP        : 'smtp.gmail.com'
--    porta_SMTP      : 465	
--    uri_IMap        : 'imap.gmail.com'
--    porta_IMap      : 993	
--
CREATE TABLE Servidor
(
	ID INT PRIMARY KEY AUTO_INCREMENT,
	dominio VARCHAR(20) NOT NULL,
	uri_SMTP varchar(30) NOT NULL,
	porta_SMTP INT NOT NULL,
	uri_IMap VARCHAR(30) NOT NULL,
	porta_IMap INT NOT NULL
);


