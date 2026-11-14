ALTER TABLE execution_transaction
DROP CONSTRAINT chk_execution_transaction_status;

UPDATE execution_transaction
SET status = 'EXECUTED',
    updated_at = CURRENT_TIMESTAMP
WHERE status = 'SUBMITTED';

ALTER TABLE execution_transaction
    ADD CONSTRAINT chk_execution_transaction_status
        CHECK (
            status IN (
                       'READY',
                       'EXECUTING',
                       'EXECUTED',
                       'FAILED'
                )
            );