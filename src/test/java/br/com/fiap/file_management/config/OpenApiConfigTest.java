package br.com.fiap.file_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void shouldCreateCustomOpenAPI() {

        OpenApiConfig config = new OpenApiConfig();

        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());

        assertEquals("File Management API", openAPI.getInfo().getTitle());
        assertEquals("1.0", openAPI.getInfo().getVersion());
        assertEquals(
                "API para gerenciamento de arquivos com autenticação JWT",
                openAPI.getInfo().getDescription()
        );

        assertNotNull(openAPI.getComponents());
        assertTrue(openAPI.getComponents()
                .getSecuritySchemes()
                .containsKey("Bearer Authentication"));

        SecurityScheme scheme = openAPI.getComponents()
                .getSecuritySchemes()
                .get("Bearer Authentication");

        assertNotNull(scheme);
        assertEquals(SecurityScheme.Type.HTTP, scheme.getType());
        assertEquals("bearer", scheme.getScheme());
        assertEquals("JWT", scheme.getBearerFormat());
    }
}