package sag.example.notificator.notification.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import sag.example.notificator.common.model.NotificationMessage;

@Service
public class NotificationPublisher {
    private static final Logger log = LoggerFactory.getLogger(NotificationPublisher.class);

    @Value("${app.kafka.topic.notification}")
    private String topicName;

    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    public NotificationPublisher(KafkaTemplate<String, NotificationMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToQueue(String key, NotificationMessage message) {
        kafkaTemplate.send(topicName, key, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Сообщение успешно отправлено в топик [{}]. Партиция: {}, смещение (Offset): {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Ошибка при отправке сообщения в Kafka", ex);
                    }
                });
    }
}
