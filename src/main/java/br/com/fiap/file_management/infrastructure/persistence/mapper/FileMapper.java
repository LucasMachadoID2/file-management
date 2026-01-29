package br.com.fiap.file_management.infrastructure.persistence.mapper;

import br.com.fiap.file_management.domain.entity.File;
import br.com.fiap.file_management.infrastructure.persistence.entity.FileEntity;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
    
    public File toDomain(FileEntity entity) {
        if (entity == null) {
            return null;
        }
        
        File file = new File();
        file.setId(entity.getId());
        file.setUserId(entity.getUserId());
        file.setEmail(entity.getEmail());
        file.setOriginalFileName(entity.getOriginalFileName());
        file.setContentType(entity.getContentType());
        file.setSize(entity.getSize());
        file.setFileData(entity.getFileData());
        file.setStatus(entity.getStatus());
        file.setCreatedAt(entity.getCreatedAt());
        file.setUpdatedAt(entity.getUpdatedAt());
        return file;
    }
    
    public FileEntity toEntity(File file) {
        if (file == null) {
            return null;
        }
        
        FileEntity entity = new FileEntity();
        entity.setId(file.getId());
        entity.setUserId(file.getUserId());
        entity.setEmail(file.getEmail());
        entity.setOriginalFileName(file.getOriginalFileName());
        entity.setContentType(file.getContentType());
        entity.setSize(file.getSize());
        entity.setFileData(file.getFileData());
        entity.setStatus(file.getStatus());
        entity.setCreatedAt(file.getCreatedAt());
        entity.setUpdatedAt(file.getUpdatedAt());
        return entity;
    }
}
