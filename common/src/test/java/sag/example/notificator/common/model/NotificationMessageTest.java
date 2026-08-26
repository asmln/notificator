package sag.example.notificator.common.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMessageTest {
    @Test
    void notificationMessageCreation() {
        var email = "aaa@bbb.cc";
        var phone = "+79999999999";

        var messageEmail = NotificationMessage.email(
                UUID.randomUUID(),
                new EmailRecipient(email),
                "subj",
                "test"
        );
        assertNotNull(messageEmail.recipient());
        assertFalse(messageEmail.subject().isBlank());
        assertThrows(
                IllegalArgumentException.class,
                () -> NotificationMessage.email(
                        UUID.randomUUID(),
                        new EmailRecipient(email),
                        "",
                        ""
                )
        );

        var messagePhone = NotificationMessage.sms(
                UUID.randomUUID(),
                new PhoneRecipient(phone),
                "test"
        );
        assertNotNull(messagePhone.recipient());
        assertTrue(messagePhone.subject().isBlank());
        assertThrows(
                IllegalArgumentException.class,
                () -> NotificationMessage.sms(
                        UUID.randomUUID(),
                        new PhoneRecipient(phone),
                        ""
                )
        );
    }
}