package br.com.fiap.file_management.controller;

import br.com.fiap.file_management.controller.dto.file.FileResponse;
import br.com.fiap.file_management.converter.FileResponseConverter;
import br.com.fiap.file_management.domain.File;
import br.com.fiap.file_management.domain.FileStatus;
import br.com.fiap.file_management.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Gerenciamento de Arquivos", description = "Endpoints para upload, download e gerenciamento de arquivos")
@SecurityRequirement(name = "Bearer Authentication")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "Upload de arquivo", description = "Faz o upload de um arquivo para processamento")
    public ResponseEntity<FileResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        File savedFile = fileService.uploadFile(file);
        return ResponseEntity.ok(toResponse(savedFile));
    }

    @GetMapping("/list")
    @Operation(summary = "Listar arquivos", description = "Lista todos os arquivos com filtros opcionais")
    public ResponseEntity<List<FileResponse>> listFiles(@RequestParam(required = false) String email,
                                                        @RequestParam(required = false) String status) {
        List<File> files = fileService.listFiles(email, status);
        List<FileResponse> response = files.stream()
                .map(FileResponseConverter::toResponse)
                .collect(toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update-status/{id}")
    @Operation(summary = "Atualizar status do arquivo", description = "Atualiza o status de processamento de um arquivo")
    public ResponseEntity<FileResponse> updateStatus(@PathVariable("id") Long id,
                                                     @RequestParam("status") String status) {

        FileStatus statusEnum = FileStatus.valueOf(status.toUpperCase());

        File updatedFile = fileService.updateFileStatus(id, statusEnum);
        return ResponseEntity.ok(toResponse(updatedFile));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Download de arquivo", description = "Faz o download de um arquivo processado")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        byte[] file = fileService.downloadFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=file_processed.zip")
                .body(file);
    }
}
