CREATE TABLE bootcamp_capacity (
    id VARCHAR(36) PRIMARY KEY,
    bootcamp_id VARCHAR(36) NOT NULL,
    capacity_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (capacity_id) REFERENCES capacities(id) ON DELETE CASCADE,
    UNIQUE KEY unique_bootcamp_capacity (bootcamp_id, capacity_id)
);