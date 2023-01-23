package com.eb.disputemanagement.dispute.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
    Page<Notification> findByStatusAndNotificationType(NotificationStatus status, NotificationType notificationType, Pageable pageable);
    Page<Notification> findByStatusAndRequestInitiatorBranchAndNotificationTypeIn(NotificationStatus status, String branchCode, Collection<NotificationType> notificationType, Pageable pageable);

}
