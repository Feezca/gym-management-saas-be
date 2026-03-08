
ALTER TABLE workout_exercise_logs DROP CONSTRAINT  fk_workout_exercise_exercise;
DROP TABLE IF EXISTS exercises;

-- ===========================
-- TABLE: exercises (TENANT)
-- ===========================

CREATE TABLE IF NOT EXISTS exercises (
    id BIGINT NOT NULL DEFAULT nextval('exercises_seq'),
    catalog_exercise_id BIGINT NOT NULL,
    is_enabled BOOLEAN NOT NULL DEFAULT true,
    custom_name VARCHAR(255),
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT exercises_pkey PRIMARY KEY (id),

    CONSTRAINT fk_exercises_catalog
    FOREIGN KEY (catalog_exercise_id)
    REFERENCES public.exercises(id)
    ON DELETE RESTRICT,

    CONSTRAINT exercises_catalog_unique
        UNIQUE (catalog_exercise_id)
);

ALTER TABLE workout_exercise_logs
ADD CONSTRAINT fk_workout_exercise_exercise
FOREIGN KEY (exercise_id)
REFERENCES exercises(id);

-- ===========================
-- INDEXES
-- ===========================

CREATE INDEX IF NOT EXISTS idx_tenant_exercises_catalog
ON exercises(catalog_exercise_id);

CREATE INDEX IF NOT EXISTS idx_tenant_exercises_enabled
ON exercises(is_enabled);

-- ===========================
-- SEED DATA
-- ===========================

INSERT INTO exercises (catalog_exercise_id, is_enabled)
SELECT id, true
FROM public.exercises;
