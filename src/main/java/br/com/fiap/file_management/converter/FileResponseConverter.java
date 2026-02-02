package br.com.fiap.file_management.converter;

import br.com.fiap.file_management.controller.dto.file.FileResponse;
import br.com.fiap.file_management.domain.File;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileResponseConverter {

    public static FileResponse toResponse(File file) {
        return FileResponse.builder()
                .id(file.getId())
                .name(file.getName())
                .status(file.getStatus())
                .createdAt(file.getCreatedAt())
                .updatedAt(file.getUpdatedAt())
                .build();
    }
}
