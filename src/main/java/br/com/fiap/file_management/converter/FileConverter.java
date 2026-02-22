package br.com.fiap.file_management.converter;

import br.com.fiap.file_management.domain.File;
import br.com.fiap.file_management.entity.FileEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileConverter {

    public static File toDomain(FileEntity fileEntity) {
        return File.builder()
                .id(fileEntity.getId())
                .user(UserConverter.toDomain(fileEntity.getUser()))
                .name(fileEntity.getName())
                .status(fileEntity.getStatus())
                .createdAt(fileEntity.getCreatedAt())
                .updatedAt(fileEntity.getUpdatedAt())
                .downloadUrl(fileEntity.getDownloadUrl())
                .build();
    }
}