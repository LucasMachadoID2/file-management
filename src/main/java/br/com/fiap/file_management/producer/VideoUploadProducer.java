package br.com.fiap.file_management.producer;

import br.com.fiap.file_management.producer.dto.VideoUploadMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VideoUploadProducer {

    @Value("${rabbitmq.queue.upload}")
    private String queue;

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    public void sendVideoUploadMessage(VideoUploadMessage videoUploadMessage) throws JsonProcessingException {
        try {
            String event = objectMapper.writeValueAsString(videoUploadMessage);
            rabbitTemplate.convertAndSend(queue, event);
        } catch (Exception e) {
            log.error("Error sending video upload message", e);
            throw e;
        }
    }
}
