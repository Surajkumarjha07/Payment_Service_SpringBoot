ALTER TABLE payments ADD COLUMN "idempotencyKey" VARCHAR(255);

UPDATE payments SET "idempotencyKey" = 'legacy-' || id WHERE "idempotencyKey" IS NULL;

ALTER TABLE payments ALTER COLUMN "idempotencyKey" SET NOT NULL;
ALTER TABLE payments ADD CONSTRAINT uq_idempotency_key UNIQUE ("idempotencyKey");
