package sag.example.notificator.model;

public record NotificationMessage(
        Recipient recipient,
        String subject,
        String content
) {
    public NotificationMessage {
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

    public static NotificationMessage email(EmailRecipient emailRecipient, String subject, String content) {
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Для Email уведомлений тема (subject) обязательна");
        }
        return new NotificationMessage(emailRecipient, subject, content);
    }

    public static NotificationMessage sms(PhoneRecipient phoneRecipient, String content) {
        return new NotificationMessage(phoneRecipient, "", content);
    }
}
