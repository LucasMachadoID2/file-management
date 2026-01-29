package br.com.fiap.file_management.infrastructure.persistence.adapter;

import br.com.fiap.file_management.domain.entity.File;
import br.com.fiap.file_management.domain.enums.FileStatus;
import br.com.fiap.file_management.domain.repository.FileRepository;
import br.com.fiap.file_management.infrastructure.persistence.entity.FileEntity;
import br.com.fiap.file_management.infrastructure.persistence.mapper.FileMapper;
import br.com.fiap.file_management.infrastructure.persistence.repository.FileJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class FileRepositoryAdapter implements FileRepository {
    
    private final FileJpaRepository jpaRepository;
    private final FileMapper mapper;
    
    public FileRepositoryAdapter(FileJpaRepository jpaRepository, FileMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public List<File> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<File> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<File> findByEmail(String email) {
        return jpaRepository.findByEmail(email).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<File> findByUserIdAndStatus(String userId, FileStatus status) {
        return jpaRepository.findByUserIdAndStatus(userId, status).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<File> findByStatus(FileStatus status) {
        return jpaRepository.findByStatus(status).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<File> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }
    
    @Override
    public File save(File file) {
        FileEntity entity = mapper.toEntity(file);
        FileEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
