package br.com.fiap.file_management.repository;

import br.com.fiap.file_management.domain.FileStatus;
import br.com.fiap.file_management.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, UUID> {
    List<FileEntity> findByStatus(FileStatus status);
}
