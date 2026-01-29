CREATE TABLE files (
    id UUID PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    size BIGINT NOT NULL,
    file_data BYTEA,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_files_user_id ON files(user_id);
CREATE INDEX idx_files_status ON files(status);
CREATE INDEX idx_files_user_status ON files(user_id, status);
