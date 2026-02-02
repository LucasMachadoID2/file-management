package br.com.fiap.file_management.service;

import br.com.fiap.file_management.converter.FileConverter;
import br.com.fiap.file_management.converter.FileEntityConverter;
import br.com.fiap.file_management.domain.File;
import br.com.fiap.file_management.domain.FileStatus;
import br.com.fiap.file_management.domain.User;
import br.com.fiap.file_management.entity.FileEntity;
import br.com.fiap.file_management.entity.UserEntity;
import br.com.fiap.file_management.repository.FileRepository;
import br.com.fiap.file_management.usecase.FilesFilterUseCase;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository repository;
    private final UserService userService;
    private final EntityManager entityManager;

    @Override
    public List<File> listFiles(String email, String status) {
        return FilesFilterUseCase.findFiltered(email, status, entityManager);
    }

    @Override
    public File uploadFile(MultipartFile multipartFile) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userService.findByEmail(auth.getName());
        User user = new User(userEntity.getId(), userEntity.getEmail());

        try {
            File file = File.builder()
                    .name(multipartFile.getName())
                    .status(FileStatus.PENDING)
                    .user(user)
                    .fileData(multipartFile.getBytes())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            FileEntity entityToSave = FileEntityConverter.toEntity(file);
            entityToSave.setUser(userEntity);

            FileEntity fileSaved = repository.save(entityToSave);

            return FileConverter.toDomain(fileSaved);
        } catch (Exception e) {
            throw new RuntimeException("Error saving file metadata: " + e.getMessage(), e);
        }
    }

    @Override
    public File updateFileStatus(String email, String fileName, FileStatus status) {
//        FileEntity file = repository.findByEmailAndOriginalFileName(email, fileName)
//                .orElseThrow(() -> new IllegalArgumentException(
//                        "File not found with email: " + email + " and fileName: " + fileName));
//
//        file.setStatus(status);
//        file.setUpdatedAt(LocalDateTime.now());
//
//        return FileConverter.toDomain(repository.save(file));

        return File.builder().build();
    }
}
