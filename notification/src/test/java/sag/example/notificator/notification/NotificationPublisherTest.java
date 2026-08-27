package sag.example.notificator.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import sag.example.notificator.common.model.EmailRecipient;
import sag.example.notificator.common.model.NotificationMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import sag.example.notificator.notification.publisher.NotificationPublisher;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(
        partitions = 1,
        controlledShutdown = true
)
class NotificationPublisherTest {

    @Value("${app.kafka.topic.notification}")
    private String topicName;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;
    @Autowired
    private NotificationPublisher notificationPublisher;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Consumer<String, String> testConsumer;

    @BeforeEach
    void setUp() {
        // Тестовый Consumer, который будет читать "сырые" строки (JSON) из встроенной Kafka
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(
                embeddedKafkaBroker,
                "test-group",
                true
        );
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        DefaultKafkaConsumerFactory<String, String> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProps);

        testConsumer = consumerFactory.createConsumer();
        // Подписываемся на наш топик уведомлений
        testConsumer.subscribe(Collections.singleton(topicName));
    }

    @AfterEach
    void tearDown() {
        if (testConsumer != null) {
            testConsumer.close();
        }
    }

    @Test
    void shouldSerializeAndSendEmailNotification() throws JsonProcessingException {
        var userId = UUID.randomUUID();
        var emailAddress = "aaa@bbb.com";
        var subject = "Тестовая тема";
        var content = "Проверка интеграции";
        var notification = NotificationMessage.email(
                userId,
                new EmailRecipient(emailAddress),
                subject,
                content
        );
        notificationPublisher.sendToQueue(
                notification.userId().toString(),
                notification
        );
        var receivedRecord = KafkaTestUtils.getSingleRecord(
                testConsumer,
                topicName,
                Duration.ofSeconds(5)
        );
        assertNotNull(receivedRecord);
        assertEquals(receivedRecord.key(), userId.toString());

        String rawJson = receivedRecord.value();
        NotificationMessage deserializedMessage = objectMapper.readValue(rawJson, NotificationMessage.class);

        assertInstanceOf(EmailRecipient.class, deserializedMessage.recipient());
        assertEquals(emailAddress, deserializedMessage.recipient().value());
        assertEquals(subject, deserializedMessage.subject());
        assertEquals(content, deserializedMessage.content());
    }
}