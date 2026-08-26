package sag.example.notificator.common.model;

public sealed interface Recipient permits EmailRecipient, PhoneRecipient {
    String value();
}
