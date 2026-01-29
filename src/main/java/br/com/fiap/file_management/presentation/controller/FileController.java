package br.com.fiap.file_management.presentation.controller;

import br.com.fiap.file_management.application.usecase.ListFilesUseCase;
import br.com.fiap.file_management.application.usecase.UpdateFileStatusUseCase;
import br.com.fiap.file_management.application.usecase.UploadFileUseCase;
import br.com.fiap.file_management.domain.entity.File;
import br.com.fiap.file_management.domain.enums.FileStatus;
import br.com.fiap.file_management.presentation.dto.FileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class FileController {
    
    private final ListFilesUseCase listFilesUseCase;
    private final UpdateFileStatusUseCase updateStatusUseCase;
    private final UploadFileUseCase uploadFileUseCase;
    
    public FileController(ListFilesUseCase listFilesUseCase, 
                         UpdateFileStatusUseCase updateStatusUseCase,
                         UploadFileUseCase uploadFileUseCase) {
        this.listFilesUseCase = listFilesUseCase;
        this.updateStatusUseCase = updateStatusUseCase;
        this.uploadFileUseCase = uploadFileUseCase;
    }
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        uploadFileUseCase.execute();
        return ResponseEntity.ok("File upload initiated. It will be processed asynchronously.");
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<FileResponse>> listFiles(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String status) {
        List<File> files = listFilesUseCase.execute(email, status);
        List<FileResponse> response = files.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/update-status")
    public ResponseEntity<FileResponse> updateStatus(
            @RequestParam("email") String email,
            @RequestParam("status") String statusValue,
            @RequestParam("fileName") String fileName) {
        
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (statusValue == null || statusValue.isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }
        
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("FileName is required");
        }
        
        FileStatus status = FileStatus.valueOf(statusValue.toUpperCase());
        
        File updatedFile = updateStatusUseCase.execute(email, fileName, status);
        return ResponseEntity.ok(toResponse(updatedFile));
    }
    
    private FileResponse toResponse(File file) {
        FileResponse response = new FileResponse();
        response.setId(file.getId());
        response.setUserId(file.getUserId());
        response.setEmail(file.getEmail());
        response.setOriginalFileName(file.getOriginalFileName());
        response.setContentType(file.getContentType());
        response.setSize(file.getSize());
        response.setStatus(file.getStatus());
        response.setCreatedAt(file.getCreatedAt());
        response.setUpdatedAt(file.getUpdatedAt());
        return response;
    }
}
