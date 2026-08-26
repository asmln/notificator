package sag.example.notificator.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import sag.example.notificator.common.model.NotificationMessage;
import sag.example.notificator.notification.config.KafkaConfig;

@Service
public class NotificationPublisher {
    private static final Logger log = LoggerFactory.getLogger(NotificationPublisher.class);

    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    public NotificationPublisher(KafkaTemplate<String, NotificationMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToQueue(String key, NotificationMessage message) {
        kafkaTemplate.send(KafkaConfig.NOTIFICATIONS_TOPIC, key, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Сообщение успешно отправлено в топик [{}]. Партиция: {}, Смещение (Offset): {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Ошибка при отправке сообщения в Kafka", ex);
                    }
                });
    }
}
