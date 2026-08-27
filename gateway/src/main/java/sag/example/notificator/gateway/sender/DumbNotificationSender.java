package sag.example.notificator.gateway.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sag.example.notificator.common.model.EmailRecipient;
import sag.example.notificator.common.model.NotificationMessage;
import sag.example.notificator.common.model.PhoneRecipient;

@Service
public class DumbNotificationSender implements NotificationSender {
    private static final Logger log = LoggerFactory.getLogger(DumbNotificationSender.class);
    @Override
    public void send(NotificationMessage message) {
        switch (message.recipient()) {
            case EmailRecipient email -> log.info(
                    "[ОТПРАВКА EMAIL] пользователь = {}, кому = {}, тема = '{}', текст = '{}'",
                    message.userId(), email.value(), message.subject(), sanitize(message.content())
            );
            case PhoneRecipient phone -> log.info(
                    "[ОТПРАВКА SMS] пользователь = {}, на номер = {}, текст = '{}'",
                    message.userId(), phone.value(), sanitize(message.content())
            );
        }
    }

    private String sanitize(String str) {
        if (str == null) {
            return "";
        }
        return str.replaceAll("[\\r\\n]+", " ");
    }
}
