-- ===========================
-- SEQUENCE
-- ===========================

CREATE SEQUENCE IF NOT EXISTS exercises_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- ===========================
-- TABLE: exercises (GLOBAL CATALOG)
-- ===========================

CREATE TABLE IF NOT EXISTS exercises (
    id BIGINT NOT NULL DEFAULT nextval('exercises_seq'),
    name VARCHAR(255) NOT NULL,

    muscle_group VARCHAR(100) NOT NULL
        CHECK (muscle_group IN (
            'CHEST',
            'BACK',
            'LEGS',
            'SHOULDERS',
            'ARMS',
            'CORE',
            'GLUTES',
            'FULL_BODY',
            'CARDIO'
        )),

    equipment VARCHAR(100)
        CHECK (equipment IN (
            'BARBELL',
            'DUMBBELL',
            'MACHINE',
            'CABLE',
            'BODYWEIGHT',
            'KETTLEBELL',
            'RESISTANCE_BAND'
        )),

    description TEXT,
    media_url VARCHAR(500),

    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT exercises_pkey PRIMARY KEY (id),
    CONSTRAINT exercises_name_unique UNIQUE (name)
);

-- ===========================
-- INDEXES
-- ===========================

CREATE INDEX IF NOT EXISTS idx_exercises_muscle_group
ON exercises(muscle_group);

CREATE INDEX IF NOT EXISTS idx_exercises_equipment
ON exercises(equipment);

-- ===========================
-- SEED DATA
-- ===========================

INSERT INTO exercises (name, muscle_group, equipment, description)
VALUES
('Bench Press', 'CHEST', 'BARBELL', 'Classic chest compound press'),
('Incline Bench Press', 'CHEST', 'BARBELL', 'Upper chest focused press'),
('Decline Bench Press', 'CHEST', 'BARBELL', 'Lower chest focused press'),

('Squat', 'LEGS', 'BARBELL', 'Fundamental lower body compound lift'),
('Front Squat', 'LEGS', 'BARBELL', 'Quadriceps focused squat'),
('Leg Press', 'LEGS', 'MACHINE', 'Machine-based leg press'),

('Deadlift', 'BACK', 'BARBELL', 'Posterior chain compound lift'),
('Romanian Deadlift', 'BACK', 'BARBELL', 'Hamstring focused deadlift'),

('Lat Pulldown', 'BACK', 'MACHINE', 'Vertical pulling exercise'),
('Pull Up', 'BACK', 'BODYWEIGHT', 'Bodyweight vertical pull'),

('Seated Cable Row', 'BACK', 'CABLE', 'Horizontal pulling movement'),
('Bent Over Row', 'BACK', 'BARBELL', 'Compound rowing movement'),

('Shoulder Press', 'SHOULDERS', 'DUMBBELL', 'Overhead shoulder press'),
('Lateral Raise', 'SHOULDERS', 'DUMBBELL', 'Isolation for lateral deltoids'),
('Front Raise', 'SHOULDERS', 'DUMBBELL', 'Anterior deltoid isolation'),

('Barbell Curl', 'ARMS', 'BARBELL', 'Biceps curl using barbell'),
('Dumbbell Curl', 'ARMS', 'DUMBBELL', 'Biceps curl with dumbbells'),
('Hammer Curl', 'ARMS', 'DUMBBELL', 'Neutral grip biceps curl'),

('Tricep Pushdown', 'ARMS', 'CABLE', 'Cable triceps extension'),
('Overhead Tricep Extension', 'ARMS', 'DUMBBELL', 'Long head triceps isolation'),

('Plank', 'CORE', 'BODYWEIGHT', 'Core stabilization exercise'),
('Hanging Leg Raise', 'CORE', 'BODYWEIGHT', 'Lower abs focused movement'),
('Crunch', 'CORE', 'BODYWEIGHT', 'Basic abdominal contraction');