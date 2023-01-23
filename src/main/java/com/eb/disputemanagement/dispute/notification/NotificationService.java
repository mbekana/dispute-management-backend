package com.eb.disputemanagement.dispute.notification;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    Notification createNotification(long id, JwtAuthenticationToken token, Notification notification);
    Page<Notification> getNotifications(Pageable pageable);
    Notification getNotification(long id);
    Notification updateNotification(long id, JwtAuthenticationToken token, Notification notification) throws IllegalAccessException;

    Page<Notification> getNotificationByStatusAndNotificationType(NotificationStatus status, NotificationType notificationType, Pageable pageable);
    Page<Notification> getByNotificationTypeIn(JwtAuthenticationToken token, Pageable pageable);
    void deleteNotification(long id);
}
