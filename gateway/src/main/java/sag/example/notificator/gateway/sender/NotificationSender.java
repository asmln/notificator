package sag.example.notificator.gateway.sender;

import sag.example.notificator.common.model.NotificationMessage;

public interface NotificationSender {
    void send(NotificationMessage message);
}
