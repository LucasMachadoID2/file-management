package br.com.fiap.file_management.controller.dto.file;

import br.com.fiap.file_management.domain.FileStatus;
import lombok.Getter;

@Getter
public class FileUpdateRequest {

    private FileStatus status;

    private String link;
}
