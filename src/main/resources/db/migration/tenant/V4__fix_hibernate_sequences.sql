-- Hibernate generates sequence names from the table name (<table>_seq).
-- The initial tenant migration used singular names for some tables; align them.

-- ===========================
-- TRAINERS
-- ===========================

CREATE SEQUENCE IF NOT EXISTS trainers_seq START WITH 1 INCREMENT BY 50;

SELECT setval(
    'trainers_seq',
    COALESCE((SELECT MAX(id) FROM trainers), 1),
    EXISTS (SELECT 1 FROM trainers)
);

ALTER TABLE trainers ALTER COLUMN id SET DEFAULT nextval('trainers_seq');

DROP SEQUENCE IF EXISTS trainer_seq;

-- ===========================
-- PROGRESS PHOTOS
-- ===========================

CREATE SEQUENCE IF NOT EXISTS progress_photos_seq START WITH 1 INCREMENT BY 50;

SELECT setval(
    'progress_photos_seq',
    COALESCE((SELECT MAX(id) FROM progress_photos), 1),
    EXISTS (SELECT 1 FROM progress_photos)
);

ALTER TABLE progress_photos ALTER COLUMN id SET DEFAULT nextval('progress_photos_seq');

DROP SEQUENCE IF EXISTS progress_photo_seq;
