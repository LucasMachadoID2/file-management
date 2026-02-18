package br.com.fiap.file_management.converter;

import br.com.fiap.file_management.domain.File;
import br.com.fiap.file_management.entity.FileEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileEntityConverter {

    public static FileEntity toEntity(File file) {
        return FileEntity.builder()
                .name(file.getName())
                .status(file.getStatus())
                .createdAt(file.getCreatedAt())
                .updatedAt(file.getUpdatedAt())
                .downloadUrl(file.getDownloadUrl())
                .build();
    }
}
