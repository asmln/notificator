package sag.example.notificator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMessageTest {
    @Test
    void notificationMessageCreation() {
        var email = "aaa@bbb.cc";
        var phone = "+79999999999";

        var messageEmail = NotificationMessage.email(
                new EmailRecipient(email),
                "subj",
                "test"
        );
        assertNotNull(messageEmail.recipient());
        assertFalse(messageEmail.subject().isBlank());
        assertThrows(
                IllegalArgumentException.class,
                () -> NotificationMessage.email(
                        new EmailRecipient(email),
                        "",
                        ""
                )
        );

        var messagePhone = NotificationMessage.sms(
                new PhoneRecipient(phone),
                "test"
        );
        assertNotNull(messagePhone.recipient());
        assertTrue(messagePhone.subject().isBlank());
        assertThrows(
                IllegalArgumentException.class,
                () -> NotificationMessage.sms(
                        new PhoneRecipient(phone),
                        ""
                )
        );
    }
}