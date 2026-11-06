CREATE TABLE instructed_transaction (
    id BIGSERIAL PRIMARY KEY,

    transaction_id VARCHAR(50) NOT NULL,

    scheme VARCHAR(50) NOT NULL,

    instruction JSONB NOT NULL,

    status VARCHAR(20) NOT NULL,

    rejection_source VARCHAR(50),

    rejection_code VARCHAR(100),

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_instructed_transaction_instruction_id
        UNIQUE (transaction_id),

    CONSTRAINT chk_instructed_transaction_status
        CHECK (status IN ('ACCEPTED', 'REJECTED')),

    CONSTRAINT chk_instructed_transaction_rejection
        CHECK (
            (status = 'REJECTED' AND rejection_source IS NOT NULL)
                OR
            (status = 'ACCEPTED'
                AND rejection_source IS NULL
                AND rejection_code IS NULL)
            )
);

CREATE TABLE execution_transaction (
       id BIGSERIAL PRIMARY KEY,

       instructed_transaction_id BIGINT NOT NULL,

       execution_date DATE NOT NULL,

       status VARCHAR(20) NOT NULL,

       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

       CONSTRAINT fk_execution_transaction_instructed_transaction
           FOREIGN KEY (instructed_transaction_id)
               REFERENCES instructed_transaction(id),

       CONSTRAINT uk_execution_transaction_instructed_transaction
           UNIQUE (instructed_transaction_id),

       CONSTRAINT chk_execution_transaction_status
           CHECK (
               status IN (
                          'READY',
                          'EXECUTING',
                          'SUBMITTED',
                          'FAILED'
                   )
               )
);

CREATE INDEX idx_execution_transaction_ready
    ON execution_transaction (status, execution_date);