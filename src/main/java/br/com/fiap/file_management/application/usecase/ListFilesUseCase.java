package br.com.fiap.file_management.application.usecase;

import br.com.fiap.file_management.domain.entity.File;
import br.com.fiap.file_management.domain.enums.FileStatus;
import br.com.fiap.file_management.domain.repository.FileRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ListFilesUseCase {
    
    private final FileRepository repository;
    
    public ListFilesUseCase(FileRepository repository) {
        this.repository = repository;
    }
    
    public List<File> execute(String email, String status) {
        if (email != null && !email.isEmpty()) {
            if (status != null && !status.isEmpty()) {
                FileStatus fileStatus = FileStatus.valueOf(status.toUpperCase());
                return repository.findByEmail(email).stream()
                    .filter(file -> file.getStatus() == fileStatus)
                    .collect(Collectors.toList());
            }
            return repository.findByEmail(email);
        }
        
        if (status != null && !status.isEmpty()) {
            FileStatus fileStatus = FileStatus.valueOf(status.toUpperCase());
            return repository.findByStatus(fileStatus);
        }
        
        return repository.findAll();
    }
}
