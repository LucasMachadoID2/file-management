package br.com.fiap.file_management.service;

import br.com.fiap.file_management.domain.File;
import br.com.fiap.file_management.domain.FileStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<File> listFiles(String email, String status);

    File uploadFile(MultipartFile multipartFile);

    File updateFileStatus(Long fileId, FileStatus status, String url);

    byte[] downloadFile(Long id);
}
