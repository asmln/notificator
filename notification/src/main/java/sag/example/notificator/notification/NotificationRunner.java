package sag.example.notificator.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import sag.example.notificator.common.model.EmailRecipient;
import sag.example.notificator.common.model.NotificationMessage;
import sag.example.notificator.common.model.PhoneRecipient;
import sag.example.notificator.notification.publisher.NotificationPublisher;

import java.util.UUID;

@Service
@Profile("!test")
public class NotificationRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(NotificationRunner.class);

    private final NotificationPublisher notificationPublisher;

    public NotificationRunner(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }

    @Override
    public void run(String... args) {
        log.info("Подготовка тестовых сообщений для отправки в Kafka...");

        UUID userId = UUID.randomUUID();

        NotificationMessage emailMsg = NotificationMessage.email(
                userId,
                new EmailRecipient("java-developer@example.com"),
                "Дело житейское",
                "Привет!\nКак дела? Чем занят на выходных?\nКто-то дропнул базу заказчика..."
        );

        NotificationMessage smsMsg = NotificationMessage.sms(
                userId,
                new PhoneRecipient("+79991234567"),
                "Возьми трубку \uD83D\uDE4F"
        );

        // Отправляем в Publisher. В качестве ключа передаем uuid пользователя.
        notificationPublisher.sendToQueue(emailMsg.userId().toString(), emailMsg);
        notificationPublisher.sendToQueue(emailMsg.userId().toString(), smsMsg);
    }
}
