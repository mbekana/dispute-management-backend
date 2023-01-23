package com.eb.disputemanagement.dispute.notification;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toNotification(NotificationDto notificationDto);
    NotificationDto toNotificationDto(Notification notification);
}
