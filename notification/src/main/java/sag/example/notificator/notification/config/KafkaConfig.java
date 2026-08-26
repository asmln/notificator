package sag.example.notificator.notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String NOTIFICATIONS_TOPIC = "notifications-topic";

    @Bean
    public NewTopic notificationsTopic() {
        return TopicBuilder.name(NOTIFICATIONS_TOPIC)
                .partitions(3) // 3 партиции для возможности масштабирования
                .replicas(1)   // 1 реплика
                .build();
    }
}
