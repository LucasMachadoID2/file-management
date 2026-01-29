package br.com.fiap.file_management.infrastructure.persistence.repository;

import br.com.fiap.file_management.domain.enums.FileStatus;
import br.com.fiap.file_management.infrastructure.persistence.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileJpaRepository extends JpaRepository<FileEntity, UUID> {
    List<FileEntity> findByUserId(String userId);
    List<FileEntity> findByEmail(String email);
    List<FileEntity> findByUserIdAndStatus(String userId, FileStatus status);
    List<FileEntity> findByStatus(FileStatus status);
    Optional<FileEntity> findByEmailAndOriginalFileName(String email, String originalFileName);
}
