package sag.example.notificator.gateway.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import sag.example.notificator.common.model.NotificationMessage;
import sag.example.notificator.gateway.sender.NotificationSender;

@Service
public class NotificationConsumer {
    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    private final NotificationSender notificationSender;

    public NotificationConsumer(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    @KafkaListener(topics = "${app.kafka.topic.notification}")
    public void consume(NotificationMessage message, Acknowledgment ack) {
        log.info("Получено новое сообщение из Kafka для пользователя: {}", message.userId());
        try {
            // Делегируем отправку сервису отправки
            notificationSender.send(message);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения из Kafka для пользователя: {}", message.userId(), e);
        }
    }
}
