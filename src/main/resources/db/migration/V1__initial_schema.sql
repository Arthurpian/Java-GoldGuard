CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(180) UNIQUE
);

CREATE TABLE bookmakers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE transactions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  bookmaker_id BIGINT NOT NULL,
  type VARCHAR(20) NOT NULL,               -- DEPOSIT / WITHDRAWAL
  amount DECIMAL(19,2) NOT NULL,
  currency VARCHAR(3) NOT NULL DEFAULT 'BRL',
  occurred_at TIMESTAMP NOT NULL,
  note VARCHAR(500),

  CONSTRAINT fk_tx_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_tx_bookmaker FOREIGN KEY (bookmaker_id) REFERENCES bookmakers(id),
  CONSTRAINT chk_tx_type CHECK (type IN ('DEPOSIT','WITHDRAWAL'))
);

-- Dados mínimos para teste
INSERT INTO users(name,email) VALUES ('Usuário Demo','demo@goldguard.local');
INSERT INTO bookmakers(name) VALUES ('bet365'),('Pinnacle'),('Stake');
