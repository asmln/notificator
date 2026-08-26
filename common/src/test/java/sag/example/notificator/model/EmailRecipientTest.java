package sag.example.notificator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailRecipientTest {
    @Test
    void emailRecipientCreation() {
        var email = "aaa@bbb.cc";
        var recipient = new EmailRecipient(email);
        assertEquals(email, recipient.value());
        assertThrows(IllegalArgumentException.class, () -> new EmailRecipient("email"));
    }
}