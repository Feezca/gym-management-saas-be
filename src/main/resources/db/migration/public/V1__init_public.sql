-- ===========================
-- SEQUENCES
-- ===========================

CREATE SEQUENCE IF NOT EXISTS gyms_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- ===========================
-- TABLE: gyms (TENANTS)
-- ===========================

CREATE TABLE IF NOT EXISTS gyms (
    id BIGINT NOT NULL DEFAULT nextval('gyms_seq'),
    name VARCHAR(255) NOT NULL,
    schema_name VARCHAR(255) NOT NULL,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP(6),
    CONSTRAINT gyms_pkey PRIMARY KEY (id),
    CONSTRAINT gyms_schema_name_key UNIQUE (schema_name)
);

-- ===========================
-- TABLE: users
-- ===========================

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL DEFAULT nextval('users_seq'),
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    avatar VARCHAR(255),
    gym_id BIGINT,
    tenant_name VARCHAR(255),
    created_at TIMESTAMP(6),
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_email_key UNIQUE (email),
    CONSTRAINT users_role_check CHECK (
        role IN ('SUPERADMIN', 'ADMIN', 'TRAINER', 'MEMBER')
    )
);

-- ===========================
-- TABLE: refresh_tokens
-- ===========================

CREATE TABLE IF NOT EXISTS refresh_tokens (
    id UUID NOT NULL,
    token VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    revoked BOOLEAN NOT NULL,
    expiry_date TIMESTAMP(6) NOT NULL,
    CONSTRAINT refresh_tokens_pkey PRIMARY KEY (id),
    CONSTRAINT refresh_tokens_token_key UNIQUE (token)
);

-- ===========================
-- FOREIGN KEYS
-- ===========================

ALTER TABLE gyms
    ADD CONSTRAINT fk_gyms_owner
    FOREIGN KEY (owner_id)
    REFERENCES users(id);

ALTER TABLE users
    ADD CONSTRAINT fk_users_gym
    FOREIGN KEY (gym_id)
    REFERENCES gyms(id);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT fk_refresh_token_user
    FOREIGN KEY (user_id)
    REFERENCES users(id);
