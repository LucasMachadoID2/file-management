package br.com.fiap.file_management.config;

import lombok.Getter;

@Getter
public enum ClientWithAcessEnum {

    FILE_PROCESS_INTEGRATION("/v1/files/update-status", "fc5aeb01-3309-4279-92af-b7f685655927");

    private final String path;
    private final String token;

    ClientWithAcessEnum(String path, String token) {
        this.path = path;
        this.token = token;
    }
}
