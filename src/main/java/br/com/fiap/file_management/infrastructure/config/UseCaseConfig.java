package br.com.fiap.file_management.infrastructure.config;

import br.com.fiap.file_management.application.usecase.ListFilesUseCase;
import br.com.fiap.file_management.application.usecase.UpdateFileStatusUseCase;
import br.com.fiap.file_management.domain.repository.FileRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    
    @Bean
    public ListFilesUseCase listFilesUseCase(FileRepository repository) {
        return new ListFilesUseCase(repository);
    }
    
    @Bean
    public UpdateFileStatusUseCase updateFileStatusUseCase(FileRepository repository) {
        return new UpdateFileStatusUseCase(repository);
    }
}
