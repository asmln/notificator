package sag.example.notificator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PhoneRecipientTest {
    @Test
    void emailRecipientCreation() {
        var phone = "+79999999999";
        var recipient = new PhoneRecipient(phone);
        assertEquals(phone, recipient.value());
        assertThrows(IllegalArgumentException.class, () -> new PhoneRecipient("123"));
    }
}