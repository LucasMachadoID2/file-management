ALTER TABLE files ADD COLUMN email VARCHAR(255);

CREATE INDEX idx_files_email ON files(email);
