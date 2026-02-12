package br.com.fiap.file_management.producer.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VideoUploadMessage {

    private String videoId;
    private String email;
    private String videoBase64;
}
