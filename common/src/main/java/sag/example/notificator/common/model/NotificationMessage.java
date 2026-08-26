package sag.example.notificator.common.model;

import java.util.UUID;

public record NotificationMessage(
        UUID userId,
        Recipient recipient,
        String subject,
        String content
) {
    public NotificationMessage {
        if (userId == null) {
            throw new IllegalArgumentException("Идентификатор пользователя (userId) не может быть null");
        }
        if (recipient == null) {
            throw new IllegalArgumentException("Получатель не может быть null");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Текст сообщения не может быть пустым");
        }
        // Защита: если отправляем Email, требуем тему сообщения
        if (recipient instanceof EmailRecipient && (subject == null || subject.isBlank())) {
            throw new IllegalArgumentException("Для Email уведомлений тема (subject) обязательна");
        }
    }

    public static NotificationMessage email(UUID userId, EmailRecipient emailRecipient, String subject, String content) {
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Для Email уведомлений тема (subject) обязательна");
        }
        return new NotificationMessage(userId, emailRecipient, subject, content);
    }

    public static NotificationMessage sms(UUID userId, PhoneRecipient phoneRecipient, String content) {
        return new NotificationMessage(userId, phoneRecipient, "", content);
    }
}
