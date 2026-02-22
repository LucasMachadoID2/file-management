package br.com.fiap.file_management.config;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import static org.junit.jupiter.api.Assertions.*;

class S3ConfigTest {

    @Test
    void shouldCreateS3ClientWithCorrectRegion() {

        S3Config config = new S3Config();

        S3Client client = config.s3Client();

        assertNotNull(client);

        // valida região configurada
        assertEquals(Region.US_EAST_1, client.serviceClientConfiguration().region());
    }
}