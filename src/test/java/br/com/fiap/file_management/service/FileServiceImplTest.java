package br.com.fiap.file_management.service;

import br.com.fiap.file_management.domain.FileStatus;
import br.com.fiap.file_management.entity.FileEntity;
import br.com.fiap.file_management.entity.UserEntity;
import br.com.fiap.file_management.producer.VideoUploadProducer;
import br.com.fiap.file_management.repository.FileRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class FileServiceImplTest {

    @Mock private FileRepository repository;
    @Mock private UserService userService;
    @Mock private EntityManager entityManager;
    @Mock private VideoUploadProducer producer;
    @Mock private S3Client s3Client;

    @InjectMocks private FileServiceImpl service;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
         ReflectionTestUtils.setField(service, "bucketName", "bucket-test");
    }

    @Test
    void shouldUploadFileSuccessfully() throws Exception {

        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getName()).thenReturn("video");
        when(multipartFile.getBytes()).thenReturn("data".getBytes());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("user@fiap.com", null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .email("user@fiap.com")
                .password("123")
                .build();

        when(userService.findByEmail("user@fiap.com")).thenReturn(userEntity);

        FileEntity savedEntity = FileEntity.builder()
                .id(10L)
                .name("video")
                .status(FileStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(userEntity)
                .build();

        when(repository.save(any())).thenReturn(savedEntity);

        var result = service.uploadFile(multipartFile);

        assertThat(result).isNotNull();
        verify(repository).save(any());
        verify(producer).sendVideoUploadMessage(any());
    }

    @Test
    void shouldUpdateFileStatus() {

        UserEntity user = UserEntity.builder()
                .id(99L)
                .email("user@fiap.com")
                .password("123")
                .build();

        FileEntity entity = FileEntity.builder()
                .id(1L)
                .status(FileStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user) // ✅ ESSENCIAL
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenAnswer(it -> it.getArgument(0));

        var result = service.updateFileStatus(1L, FileStatus.PROCESSING, "url");

        assertThat(result.getStatus()).isEqualTo(FileStatus.PROCESSING);

        verify(repository).save(entity);
    }

   @Test
    void shouldDownloadFileFromS3() throws Exception {

        UserEntity user = UserEntity.builder()
                .email("user@fiap.com")
                .build();

        FileEntity entity = FileEntity.builder()
                .id(5L)
                .user(user)
                .build();

        when(repository.findById(5L)).thenReturn(Optional.of(entity));

        byte[] expectedBytes = "zip-content".getBytes();

        ResponseInputStream<GetObjectResponse> responseStream =
                mock(ResponseInputStream.class);

        when(responseStream.readAllBytes()).thenReturn(expectedBytes);

        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenReturn(responseStream);

        byte[] result = service.downloadFile(5L);

        assertThat(result).isEqualTo(expectedBytes);

        verify(s3Client).getObject(any(GetObjectRequest.class));
    }
}