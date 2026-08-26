package sag.example.notificator.model;

public record EmailRecipient(String value) implements Recipient {
    public EmailRecipient {
        // Примитивная проверка на первое время
        if (value == null || !value.contains("@")) {
            throw new IllegalArgumentException("Некорректный формат Email: " + value);
        }
    }
}
