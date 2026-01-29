package br.com.fiap.file_management.domain.repository;

import br.com.fiap.file_management.domain.entity.File;
import br.com.fiap.file_management.domain.enums.FileStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository {
    List<File> findAll();
    List<File> findByUserId(String userId);
    List<File> findByEmail(String email);
    List<File> findByUserIdAndStatus(String userId, FileStatus status);
    List<File> findByStatus(FileStatus status);
    Optional<File> findById(UUID id);
    File save(File file);
}
