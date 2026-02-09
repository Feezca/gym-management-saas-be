-- ===========================
-- SEQUENCES
-- ===========================

CREATE SEQUENCE IF NOT EXISTS members_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS routines_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS exercises_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS workout_logs_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS workout_exercise_logs_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS payments_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS progress_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS progress_photo_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS products_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS trainer_seq START WITH 1 INCREMENT BY 1;

-- ===========================
-- MEMBERS
-- ===========================

CREATE TABLE IF NOT EXISTS members (
    id BIGINT NOT NULL DEFAULT nextval('members_seq'),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    membership_type VARCHAR(255),
    join_date DATE NOT NULL,
    trainer_id BIGINT NOT NULL,
    created_at TIMESTAMP(6),
    CONSTRAINT members_pkey PRIMARY KEY (id),
    CONSTRAINT members_status_check CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

-- ===========================
-- ROUTINES
-- ===========================

CREATE TABLE IF NOT EXISTS routines (
    id BIGINT NOT NULL DEFAULT nextval('routines_seq'),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    member_id BIGINT NOT NULL,
    trainer_id BIGINT,
    created_at TIMESTAMP(6),
    CONSTRAINT routines_pkey PRIMARY KEY (id)
);

-- ===========================
-- EXERCISES
-- ===========================

CREATE TABLE IF NOT EXISTS exercises (
    id BIGINT NOT NULL DEFAULT nextval('exercises_seq'),
    name VARCHAR(255) NOT NULL,
    sets INTEGER,
    reps INTEGER,
    rest INTEGER,
    routine_id BIGINT NOT NULL,
    notes TEXT,
    created_at TIMESTAMP(6),
    CONSTRAINT exercises_pkey PRIMARY KEY (id)
);

-- ===========================
-- WORKOUT LOGS
-- ===========================

CREATE TABLE IF NOT EXISTS workout_logs (
    id BIGINT NOT NULL DEFAULT nextval('workout_logs_seq'),
    date DATE NOT NULL,
    member_id BIGINT NOT NULL,
    routine_id BIGINT NOT NULL,
    created_at TIMESTAMP(6),
    CONSTRAINT workout_logs_pkey PRIMARY KEY (id)
);

-- ===========================
-- WORKOUT EXERCISE LOGS
-- ===========================

CREATE TABLE IF NOT EXISTS workout_exercise_logs (
    id BIGINT NOT NULL DEFAULT nextval('workout_exercise_logs_seq'),
    workout_log_id BIGINT NOT NULL,
    exercise_id BIGINT NOT NULL,
    weight NUMERIC(38,2),
    completed BOOLEAN NOT NULL,
    notes TEXT,
    created_at TIMESTAMP(6),
    CONSTRAINT workout_exercise_logs_pkey PRIMARY KEY (id)
);

-- ===========================
-- PAYMENTS
-- ===========================

CREATE TABLE IF NOT EXISTS payments (
    id BIGINT NOT NULL DEFAULT nextval('payments_seq'),
    amount NUMERIC(38,2) NOT NULL,
    date DATE NOT NULL,
    member_id BIGINT NOT NULL,
    method VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6),
    CONSTRAINT payments_pkey PRIMARY KEY (id),
    CONSTRAINT payments_status_check CHECK (status IN ('PAID','PENDING','OVERDUE'))
);

-- ===========================
-- PROGRESS
-- ===========================

CREATE TABLE IF NOT EXISTS progress (
    id BIGINT NOT NULL DEFAULT nextval('progress_seq'),
    member_id BIGINT NOT NULL,
    weight NUMERIC(38,2),
    body_fat NUMERIC(38,2),
    chest NUMERIC(38,2),
    waist NUMERIC(38,2),
    arms NUMERIC(38,2),
    legs NUMERIC(38,2),
    date DATE NOT NULL,
    created_at TIMESTAMP(6),
    CONSTRAINT progress_pkey PRIMARY KEY (id),
    CONSTRAINT progress_member_id_key UNIQUE (member_id)

);

CREATE TABLE IF NOT EXISTS progress_photos (
    id BIGINT NOT NULL DEFAULT nextval('progress_photo_seq'),
    progress_id BIGINT NOT NULL,
    photo_url VARCHAR(255)
);

-- ===========================
-- PRODUCTS
-- ===========================

CREATE TABLE IF NOT EXISTS products (
    id BIGINT NOT NULL DEFAULT nextval('products_seq'),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(38,2) NOT NULL,
    stock INTEGER NOT NULL,
    category VARCHAR(255),
    created_at TIMESTAMP(6),
    CONSTRAINT products_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS trainers (
    id BIGINT NOT NULL DEFAULT nextval('trainer_seq'),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    created_at TIMESTAMP(6),
    CONSTRAINT trainers_pkey PRIMARY KEY (id)
);

-- ===========================
-- FOREIGN KEYS
-- ===========================

ALTER TABLE members
    ADD CONSTRAINT fk_members_trainer
    FOREIGN KEY (trainer_id)
    REFERENCES trainers(id);

ALTER TABLE routines
    ADD CONSTRAINT fk_routines_member
    FOREIGN KEY (member_id)
    REFERENCES members(id);

ALTER TABLE routines
    ADD CONSTRAINT fk_routines_trainer
    FOREIGN KEY (trainer_id)
    REFERENCES trainers(id);

ALTER TABLE exercises
    ADD CONSTRAINT fk_exercises_routine
    FOREIGN KEY (routine_id)
    REFERENCES routines(id);

ALTER TABLE workout_logs
    ADD CONSTRAINT fk_workout_logs_member
    FOREIGN KEY (member_id)
    REFERENCES members(id);

ALTER TABLE workout_logs
    ADD CONSTRAINT fk_workout_logs_routine
    FOREIGN KEY (routine_id)
    REFERENCES routines(id);

ALTER TABLE workout_exercise_logs
    ADD CONSTRAINT fk_workout_exercise_log
    FOREIGN KEY (workout_log_id)
    REFERENCES workout_logs(id);

ALTER TABLE workout_exercise_logs
    ADD CONSTRAINT fk_workout_exercise_exercise
    FOREIGN KEY (exercise_id)
    REFERENCES exercises(id);

ALTER TABLE payments
    ADD CONSTRAINT fk_payments_member
    FOREIGN KEY (member_id)
    REFERENCES members(id);

ALTER TABLE progress
    ADD CONSTRAINT fk_progress_member
    FOREIGN KEY (member_id)
    REFERENCES members(id);

ALTER TABLE progress_photos
    ADD CONSTRAINT fk_progress_photo
    FOREIGN KEY (progress_id)
    REFERENCES progress(id);


