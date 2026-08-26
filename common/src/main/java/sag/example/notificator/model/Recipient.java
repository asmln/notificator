package sag.example.notificator.model;

public sealed interface Recipient permits EmailRecipient, PhoneRecipient {
    String value();
}
