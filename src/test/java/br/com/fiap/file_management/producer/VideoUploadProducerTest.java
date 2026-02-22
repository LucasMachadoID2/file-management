package br.com.fiap.file_management.producer;

import br.com.fiap.file_management.producer.dto.VideoUploadMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoUploadProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private VideoUploadProducer producer;

    @Test
    void shouldSerializeAndSendMessage() throws Exception {
        VideoUploadMessage message = VideoUploadMessage.builder()
                .videoId("123")
                .email("test@email.com")
                .videoBase64("base64")
                .build();

        ReflectionTestUtils.setField(producer, "queue", "upload-queue");

        when(objectMapper.writeValueAsString(message)).thenReturn("json-event");

        producer.sendVideoUploadMessage(message);

        verify(objectMapper).writeValueAsString(message);
        verify(rabbitTemplate).convertAndSend("upload-queue", "json-event");
    }

   @Test
    void shouldThrowExceptionWhenSerializationFails() throws Exception {

        VideoUploadMessage message = VideoUploadMessage.builder()
                .videoId("1")
                .email("test@test.com")
                .videoBase64("abc")
                .build();

        when(objectMapper.writeValueAsString(any()))
                .thenThrow(new JsonProcessingException("Serialization error") {});

        assertThrows(JsonProcessingException.class, () ->
                producer.sendVideoUploadMessage(message)
        );

        verify(rabbitTemplate, never())
                .convertAndSend(anyString(), any(Object.class));
    }

    @Test
    void shouldThrowExceptionWhenRabbitFails() throws Exception {

        VideoUploadMessage message = VideoUploadMessage.builder()
                .videoId("1")
                .email("test@test.com")
                .videoBase64("abc")
                .build();

        when(objectMapper.writeValueAsString(any()))
                .thenReturn("json-event");

        doThrow(new RuntimeException("Rabbit error"))
                .when(rabbitTemplate)
                .convertAndSend(anyString(), any(Object.class));

        assertThrows(RuntimeException.class, () ->
                producer.sendVideoUploadMessage(message)
        );

        verify(objectMapper).writeValueAsString(message);
    }
}