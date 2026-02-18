package br.com.fiap.file_management.controller;

import br.com.fiap.file_management.controller.dto.file.FileResponse;
import br.com.fiap.file_management.converter.FileResponseConverter;
import br.com.fiap.file_management.domain.File;
import br.com.fiap.file_management.domain.FileStatus;
import br.com.fiap.file_management.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static br.com.fiap.file_management.converter.FileResponseConverter.toResponse;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/v1/files")
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
                .collect(toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update-status/{id}")
    public ResponseEntity<FileResponse> updateStatus(@PathVariable("id") Long id,
                                                     @RequestParam("status") String status,
                                                     @RequestParam(value = "url", required = false) String url) {

        FileStatus statusEnum = FileStatus.valueOf(status.toUpperCase());

        File updatedFile = fileService.updateFileStatus(id, statusEnum, url);
        return ResponseEntity.ok(toResponse(updatedFile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        byte[] file = fileService.downloadFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=file_processed.zip")
                .body(file);
    }
}
