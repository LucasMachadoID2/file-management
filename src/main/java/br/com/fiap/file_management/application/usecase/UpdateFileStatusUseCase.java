package br.com.fiap.file_management.application.usecase;

import br.com.fiap.file_management.domain.entity.File;
import br.com.fiap.file_management.domain.enums.FileStatus;
import br.com.fiap.file_management.domain.repository.FileRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateFileStatusUseCase {
    
    private final FileRepository repository;
    
    public UpdateFileStatusUseCase(FileRepository repository) {
        this.repository = repository;
    }
    
    public File execute(String email, String originalFileName, String contentType, 
                       byte[] fileData, FileStatus status, String userId) {
        
        File file = new File();
        file.setUserId(userId != null ? userId : "system");
        file.setEmail(email);
        file.setOriginalFileName(originalFileName);
        file.setContentType(contentType);
        file.setSize((long) fileData.length);
        file.setFileData(fileData);
        file.setStatus(status);
        file.setCreatedAt(LocalDateTime.now());
        file.setUpdatedAt(LocalDateTime.now());
        
        File savedFile = repository.save(file);
        
        String storedFileName = email + "_" + savedFile.getId().toString();
        savedFile.setOriginalFileName(storedFileName);
        
        return repository.save(savedFile);
    }
}
