package com.eb.disputemanagement.dispute.notification;


import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.config.EmployeeClient;
import com.eb.disputemanagement.dispute.dto.EmployeeMapper;
import com.eb.disputemanagement.dispute.utils.Util;
import com.eb.disputemanagement.dispute.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements  NotificationService{
    private final NotificationRepository notificationRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeClient employeeClient;
    @Override
    public Notification createNotification(long id, JwtAuthenticationToken token, Notification notification) {
        notificationRepository.save(notification);
        return notification;
    }

    @Override
    public Page<Notification> getNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    @Override
    public Notification getNotification(long id) {
        return notificationRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(Notification.class, "Id", String.valueOf(id)));
    }

    @Override
    public Notification updateNotification(long id, JwtAuthenticationToken token, Notification notification) throws IllegalAccessException {
        var not = getNotification(id);
        var employeeId = Util.getEmployeeID(token);
        var employee = getEmployee(employeeId);
        if(not.getNotificationType().equals(NotificationType.APPROVE) && !employee.getBranch().getCode().equals(not.getTargetBranch())){
            throw new IllegalAccessException("You can not read this notification");
        }
        not.setStatus(NotificationStatus.read);
        not.setReadBy(employee);
        return notificationRepository.save(not);
    }

    @Override
    public Page<Notification> getNotificationByStatusAndNotificationType(NotificationStatus status, NotificationType notificationType, Pageable pageable) {
        return notificationRepository.findByStatusAndNotificationType(status, notificationType, pageable);
    }

    @Override
    public Page<Notification> getByNotificationTypeIn(JwtAuthenticationToken token, Pageable pageable) {
        var employeeId = Util.getEmployeeID(token);
        var employee = getEmployee(employeeId);
        Collection<NotificationType> not = new ArrayList<>();
        not.add(NotificationType.ALREADY_PAID);
        not.add(NotificationType.APPROVE);
        not.add(NotificationType.RECEIVE);
        not.add(NotificationType.DECLINE);
        return notificationRepository.findByStatusAndRequestInitiatorBranchAndNotificationTypeIn(NotificationStatus.unread, employee.getBranch().getCode(), not, pageable);
    }

    @Override
    public void deleteNotification(long id) {
        notificationRepository.deleteById(id);
    }

    private Employee getEmployee(String employeeId) {
        return employeeMapper.employeeDtoToEmployee(employeeClient.getEmployeeById(employeeId));
    }
}
