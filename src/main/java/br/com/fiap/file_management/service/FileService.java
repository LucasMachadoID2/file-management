package br.com.fiap.file_management.service;

import br.com.fiap.file_management.controller.dto.file.FileUpdateRequest;
import br.com.fiap.file_management.domain.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<File> listFiles(String email, String status);

    File uploadFile(MultipartFile multipartFile);

    File updateFile(Long fileId, FileUpdateRequest fileUpdateRequest);

    byte[] downloadFile(Long id);
}
