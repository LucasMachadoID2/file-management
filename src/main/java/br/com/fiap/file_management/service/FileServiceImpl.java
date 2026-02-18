package br.com.fiap.file_management.service;

import br.com.fiap.file_management.controller.dto.file.FileUpdateRequest;
import br.com.fiap.file_management.converter.FileConverter;
import br.com.fiap.file_management.converter.FileEntityConverter;
import br.com.fiap.file_management.domain.File;
import br.com.fiap.file_management.domain.FileStatus;
import br.com.fiap.file_management.domain.User;
import br.com.fiap.file_management.entity.FileEntity;
import br.com.fiap.file_management.entity.UserEntity;
import br.com.fiap.file_management.producer.VideoUploadProducer;
import br.com.fiap.file_management.producer.dto.VideoUploadMessage;
import br.com.fiap.file_management.repository.FileRepository;
import br.com.fiap.file_management.usecase.FilesFilterUseCase;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository repository;
    private final UserService userService;
    private final EntityManager entityManager;
    private final VideoUploadProducer videoUploadProducer;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public List<File> listFiles(String email, String status) {
        return FilesFilterUseCase.findFiltered(email, status, entityManager);
    }

    @Override
    @Transactional
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
            produceVideo(fileSaved, multipartFile);

            return FileConverter.toDomain(fileSaved);
        } catch (Exception e) {
            throw new RuntimeException("Error saving file metadata: " + e.getMessage(), e);
        }
    }

    private void produceVideo(FileEntity fileEntity, MultipartFile video) {
        try {
            VideoUploadMessage videoUploadMessage = VideoUploadMessage.builder()
                    .videoId(fileEntity.getId().toString())
                    .email(fileEntity.getUser().getEmail())
                    .videoBase64(Base64.getEncoder().encodeToString(video.getBytes()))
                    .build();
            videoUploadProducer.sendVideoUploadMessage(videoUploadMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File updateFile(Long fileId, FileUpdateRequest fileUpdateRequest) {
        FileEntity file = repository.findById(fileId).orElseThrow();

        file.setStatus(fileUpdateRequest.getStatus());
        file.setLink(fileUpdateRequest.getLink());
        file.setUpdatedAt(LocalDateTime.now());

        return FileConverter.toDomain(repository.save(file));
    }

    @Override
    public byte[] downloadFile(Long id) {
        try {
            FileEntity fileEntity = repository.findById(id).orElseThrow();

            String key = fileEntity.getUser().getEmail() + "/" + id + "/frames.zip";

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            ResponseInputStream<?> file = s3Client.getObject(getObjectRequest);

            return file.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
