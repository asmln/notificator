package sag.example.notificator.common.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // В JSON автоматически добавится поле type: email или sms
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmailRecipient.class, name = "email"),
        @JsonSubTypes.Type(value = PhoneRecipient.class, name = "sms")
})
public sealed interface Recipient permits EmailRecipient, PhoneRecipient {
    String value();
}
