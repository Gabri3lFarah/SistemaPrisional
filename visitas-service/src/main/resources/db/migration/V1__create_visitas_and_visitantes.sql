-- Create visitantes table
CREATE TABLE IF NOT EXISTS visitantes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    telefone VARCHAR(15),
    relacao_prisioneiro VARCHAR(100),
    is_advogado BOOLEAN NOT NULL DEFAULT FALSE,
    codigo_autorizacao INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_visitantes_cpf ON visitantes(cpf);
CREATE INDEX idx_visitantes_is_advogado ON visitantes(is_advogado);

-- Create visitas table
CREATE TABLE IF NOT EXISTS visitas (
    id BIGSERIAL PRIMARY KEY,
    prisioneiro_id BIGINT NOT NULL,
    visitante_id BIGINT NOT NULL,
    data_visita TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDENTE',
    motivo_negacao VARCHAR(500),
    observacoes VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_visita_visitante FOREIGN KEY (visitante_id) REFERENCES visitantes(id) ON DELETE CASCADE
);

CREATE INDEX idx_visitas_prisioneiro_id ON visitas(prisioneiro_id);
CREATE INDEX idx_visitas_visitante_id ON visitas(visitante_id);
CREATE INDEX idx_visitas_status ON visitas(status);
CREATE INDEX idx_visitas_data_visita ON visitas(data_visita);

-- Create outbox table for event-driven architecture (Transactional Outbox Pattern)
CREATE TABLE IF NOT EXISTS outbox (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(255) NOT NULL,
    aggregate_id BIGINT NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    processed_at TIMESTAMP
);

CREATE INDEX idx_outbox_processed ON outbox(processed);
CREATE INDEX idx_outbox_created_at ON outbox(created_at);
CREATE INDEX idx_outbox_aggregate ON outbox(aggregate_type, aggregate_id);

-- Insert some sample data for testing
INSERT INTO visitantes (nome, cpf, telefone, relacao_prisioneiro, is_advogado, codigo_autorizacao) VALUES
('João Silva', '12345678901', '(48)91234-5678', 'Amigo', FALSE, NULL),
('Maria Santos', '23456789012', '(48)92345-6789', 'Família', FALSE, NULL),
('Dr. Pedro Advogado', '34567890123', '(48)93456-7890', 'Advogado', TRUE, 666),
('Ana Costa', '45678901234', '(48)94567-8901', 'Irmã', FALSE, NULL);
