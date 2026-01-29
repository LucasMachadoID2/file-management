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
        
        // Criar nome do arquivo: email_UUID (UUID será gerado pelo banco)
        
        // Criar entidade File sem setar o ID (deixar o banco gerar)
        File file = new File();
        file.setUserId(userId != null ? userId : "system");
        file.setEmail(email);
        file.setOriginalFileName(originalFileName); // Vamos ajustar depois que tivermos o ID
        file.setContentType(contentType);
        file.setSize((long) fileData.length);
        file.setFileData(fileData);
        file.setStatus(status);
        file.setCreatedAt(LocalDateTime.now());
        file.setUpdatedAt(LocalDateTime.now());
        
        // Salvar para gerar o ID
        File savedFile = repository.save(file);
        
        // Agora atualizar o nome do arquivo com email_UUID
        String storedFileName = email + "_" + savedFile.getId().toString();
        savedFile.setOriginalFileName(storedFileName);
        
        return repository.save(savedFile);
    }
}
