package sag.example.notificator.common.model;

public record PhoneRecipient(String value) implements Recipient {
    public PhoneRecipient {
        // Примитивная проверка на первое время
        if (value == null || !value.contains("+")) {
            throw new IllegalArgumentException("Некорректный формат номера телефона: " + value);
        }
    }
}
