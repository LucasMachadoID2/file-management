package br.com.fiap.file_management.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientWithAcessEnumTest {

    @Test
    void shouldValidateEnumValues() {

        ClientWithAcessEnum client = ClientWithAcessEnum.FILE_PROCESS_INTEGRATION;

        assertEquals("/v1/files/post-process-file", client.getPath());
        assertEquals("fc5aeb01-3309-4279-92af-b7f685655927", client.getToken());
    }

    @Test
    void shouldCoverValuesMethod() {

        ClientWithAcessEnum[] values = ClientWithAcessEnum.values();

        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals(ClientWithAcessEnum.FILE_PROCESS_INTEGRATION, values[0]);
    }

    @Test
    void shouldCoverValueOfMethod() {

        ClientWithAcessEnum client =
                ClientWithAcessEnum.valueOf("FILE_PROCESS_INTEGRATION");

        assertNotNull(client);
        assertEquals("/v1/files/post-process-file", client.getPath());
    }
}