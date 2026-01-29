package br.com.fiap.file_management.application.usecase;

import br.com.fiap.file_management.domain.entity.File;
import br.com.fiap.file_management.domain.enums.FileStatus;
import br.com.fiap.file_management.domain.repository.FileRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UploadFileUseCase {
    
    private final FileRepository fileRepository;
    
    public UploadFileUseCase(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    
    public File execute(MultipartFile multipartFile, String email) {
        try {
            UUID fileId = UUID.randomUUID();
            
            File file = new File();
            file.setId(fileId);
            file.setUserId("system");
            file.setEmail(email);
            file.setOriginalFileName(email + "_" + fileId);
            file.setContentType(multipartFile.getContentType());
            file.setSize(multipartFile.getSize());
            file.setStatus(FileStatus.PENDING);
            file.setCreatedAt(LocalDateTime.now());
            file.setUpdatedAt(LocalDateTime.now());
            
            return fileRepository.save(file);
        } catch (Exception e) {
            throw new RuntimeException("Error saving file metadata: " + e.getMessage(), e);
        }
    }
}
