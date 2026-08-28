package sag.example.notificator.gateway.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import sag.example.notificator.common.model.EmailRecipient;
import sag.example.notificator.common.model.NotificationMessage;
import sag.example.notificator.common.model.PhoneRecipient;
import sag.example.notificator.gateway.sender.NotificationSender;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, controlledShutdown = true)
@ExtendWith(MockitoExtension.class)
class NotificationConsumerIT {
    @Value("${app.kafka.topic.notification}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    @MockitoBean
    private NotificationSender messageSender;

    @Test
    void shouldReceiveAndProcessEmailMessage() {
        UUID userId = UUID.randomUUID();
        String email = "test@test.com";
        String subject = "Тест";
        String content = "Проверка обработки Email";
        NotificationMessage emailMessage = NotificationMessage.email(
                userId,
                new EmailRecipient(email),
                subject,
                content
        );

        kafkaTemplate.send(topicName, userId.toString(), emailMessage);
        ArgumentCaptor<NotificationMessage> messageCaptor = ArgumentCaptor.forClass(NotificationMessage.class);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(
                () -> verify(messageSender).send(messageCaptor.capture())
        );

        NotificationMessage processedMessage = messageCaptor.getValue();
        assertEquals(userId, processedMessage.userId());
        assertInstanceOf(EmailRecipient.class, processedMessage.recipient());
        assertEquals(email, processedMessage.recipient().value());
        assertEquals(subject, processedMessage.subject());
        assertEquals(content, processedMessage.content());
    }

    @Test
    void shouldReceiveAndProcessSmsMessage() {
        UUID userId = UUID.randomUUID();
        String phoneNumber = "+79990000000";
        String content = "Проверка обработки Phone";
        NotificationMessage smsMessage = NotificationMessage.sms(
                userId,
                new PhoneRecipient(phoneNumber),
                content
        );

        kafkaTemplate.send(topicName, userId.toString(), smsMessage);
        ArgumentCaptor<NotificationMessage> messageCaptor = ArgumentCaptor.forClass(NotificationMessage.class);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(
                () -> verify(messageSender).send(messageCaptor.capture())
        );

        NotificationMessage processedMessage = messageCaptor.getValue();
        assertEquals(userId, processedMessage.userId());
        assertInstanceOf(PhoneRecipient.class, processedMessage.recipient());
        assertEquals(phoneNumber, processedMessage.recipient().value());
        assertTrue(processedMessage.subject().isBlank());
        assertEquals(content, processedMessage.content());
    }
}