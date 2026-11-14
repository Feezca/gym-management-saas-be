-- ===========================
-- TRAINERS: AGREED AMOUNT + PAYMENT TYPE
-- ===========================

ALTER TABLE trainers ADD COLUMN IF NOT EXISTS agreed_amount NUMERIC(38,2);
ALTER TABLE trainers ADD COLUMN IF NOT EXISTS payment_type VARCHAR(255);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'trainers_payment_type_check'
    ) THEN
        ALTER TABLE trainers
            ADD CONSTRAINT trainers_payment_type_check
            CHECK (payment_type IN ('WEEKLY', 'BIWEEKLY', 'SESSION', 'MONTHLY'));
    END IF;
END $$;

-- ===========================
-- TRAINER PAYMENTS
-- ===========================

CREATE SEQUENCE IF NOT EXISTS trainer_payment_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS trainer_payment (
    id BIGINT NOT NULL DEFAULT nextval('trainer_payment_seq'),
    amount NUMERIC(38,2) NOT NULL,
    trainer_id BIGINT NOT NULL,
    date DATE NOT NULL,
    payment_type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6),
    CONSTRAINT trainer_payment_pkey PRIMARY KEY (id),
    CONSTRAINT trainer_payment_payment_type_check
        CHECK (payment_type IN ('WEEKLY', 'BIWEEKLY', 'SESSION', 'MONTHLY')),
    CONSTRAINT fk_trainer_payment_trainer
        FOREIGN KEY (trainer_id)
        REFERENCES trainers(id)
);

CREATE INDEX IF NOT EXISTS idx_trainer_payment_trainer
ON trainer_payment(trainer_id);

CREATE INDEX IF NOT EXISTS idx_trainer_payment_date
ON trainer_payment(date);
