package sag.example.notificator.common.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailRecipientTest {
    @Test
    void shouldCreateEmailRecipient() {
        var email = "aaa@bbb.cc";
        var recipient = new EmailRecipient(email);
        assertEquals(email, recipient.value());
        assertThrows(IllegalArgumentException.class, () -> new EmailRecipient("email"));
    }
}