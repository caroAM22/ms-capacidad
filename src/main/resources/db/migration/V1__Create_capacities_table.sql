CREATE TABLE capacities (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(90) NOT NULL
);

-- Índices para mejorar performance
CREATE INDEX idx_capacities_name ON capacities(name);