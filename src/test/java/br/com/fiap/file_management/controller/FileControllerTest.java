package br.com.fiap.file_management.controller;

import br.com.fiap.file_management.domain.File;
import br.com.fiap.file_management.domain.FileStatus;
import br.com.fiap.file_management.service.FileService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    // ------------------------------------------------
    // UPLOAD
    // ------------------------------------------------
    @Test
    void shouldUploadFileSuccessfully() throws Exception {

        File file = File.builder()
                .id(1L)
                .name("video.mp4")
                .status(FileStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .downloadUrl("http://fake-url")
                .build();

        when(fileService.uploadFile(any())).thenReturn(file);

        MockMultipartFile multipartFile =
                new MockMultipartFile(
                        "file",
                        "video.mp4",
                        MediaType.APPLICATION_OCTET_STREAM_VALUE,
                        "dummy content".getBytes()
                );

        mockMvc.perform(multipart("/v1/files/upload")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("video.mp4"))
                .andExpect(jsonPath("$.downloadUrl").doesNotExist());
    }

    // ------------------------------------------------
    // LIST
    // ------------------------------------------------
    @Test
    void shouldListFilesSuccessfully() throws Exception {

        File file = File.builder()
                .id(1L)
                .name("video.mp4")
                .status(FileStatus.FINISHED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .downloadUrl("http://fake-url")
                .build();

        when(fileService.listFiles(any(), any()))
                .thenReturn(List.of(file));

        mockMvc.perform(get("/v1/files/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].downloadUrl").value("http://fake-url"));
    }

    // ------------------------------------------------
    // UPDATE STATUS
    // ------------------------------------------------
    @Test
    void shouldUpdateStatusSuccessfully() throws Exception {

        File file = File.builder()
                .id(1L)
                .name("video.mp4")
                .status(FileStatus.FINISHED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .downloadUrl("http://processed")
                .build();

        when(fileService.updateFileStatus(eq(1L), eq(FileStatus.FINISHED), any()))
                .thenReturn(file);

        mockMvc.perform(patch("/v1/files/update-status/1")
                        .param("status", "FINISHED")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"http://processed\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FINISHED"))
                .andExpect(jsonPath("$.downloadUrl").value("http://processed"));
    }

    // ------------------------------------------------
    // DOWNLOAD
    // ------------------------------------------------
    @Test
    void shouldDownloadFileSuccessfully() throws Exception {

        byte[] bytes = "dummy zip".getBytes();

        when(fileService.downloadFile(1L)).thenReturn(bytes);

        mockMvc.perform(get("/v1/files/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=file_processed.zip"))
                .andExpect(content().bytes(bytes));
    }
}