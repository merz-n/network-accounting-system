CREATE SCHEMA IF NOT EXISTS networks;

CREATE TABLE IF NOT EXISTS networks.network (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS networks.device (
    id BIGSERIAL PRIMARY KEY,
    network_id BIGINT REFERENCES networks.network(id) ON DELETE SET NULL,
    name VARCHAR(255) NOT NULL,
    ip_address VARCHAR(64) NOT NULL,
    mac_address VARCHAR(64),
    type VARCHAR(128),
    status VARCHAR(64),
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS networks.connection (
    id BIGSERIAL PRIMARY KEY,
    device_from_id BIGINT REFERENCES networks.device(id) ON DELETE CASCADE,
    device_to_id BIGINT REFERENCES networks.device(id) ON DELETE CASCADE,
    connection_type VARCHAR(128) NOT NULL,
    status VARCHAR(64),
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT connection_devices_diff CHECK (device_from_id <> device_to_id)
);
