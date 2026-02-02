package br.com.fiap.file_management.controller;

import br.com.fiap.file_management.controller.dto.file.FileResponse;
import br.com.fiap.file_management.converter.FileResponseConverter;
import br.com.fiap.file_management.domain.File;
import br.com.fiap.file_management.domain.FileStatus;
import br.com.fiap.file_management.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.fiap.file_management.converter.FileResponseConverter.toResponse;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        File savedFile = fileService.uploadFile(file);
        return ResponseEntity.ok(toResponse(savedFile));
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileResponse>> listFiles(@RequestParam(required = false) String email,
                                                        @RequestParam(required = false) String status) {
        List<File> files = fileService.listFiles(email, status);
        List<FileResponse> response = files.stream()
                .map(FileResponseConverter::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update-status")
    public ResponseEntity<FileResponse> updateStatus(@RequestParam("id") Long id,
                                                     @RequestParam("status") String status) {

        FileStatus statusEnum = FileStatus.valueOf(status.toUpperCase());

        File updatedFile = fileService.updateFileStatus(id, statusEnum);
        return ResponseEntity.ok(toResponse(updatedFile));
    }
}
