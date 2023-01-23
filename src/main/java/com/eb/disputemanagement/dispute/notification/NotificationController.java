package com.eb.disputemanagement.dispute.notification;



import com.eb.disputemanagement.dispute.utils.PaginatedResultsRetrievedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController implements  NotificationApi{
    private final NotificationMapper notificationMapper;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public NotificationDto createNotification(long id, JwtAuthenticationToken token, NotificationDto notificationDto) {
        return notificationMapper.toNotificationDto(notificationService.createNotification(id, token, notificationMapper.toNotification(notificationDto)));
    }

    @Override
    public ResponseEntity<PagedModel<NotificationDto>> getAllUnreadNotifications(Pageable pageable, NotificationStatus status, NotificationType notificationType, PagedResourcesAssembler assembler, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                NotificationDto.class, uriBuilder, response, pageable.getPageNumber(),  notificationService.getNotificationByStatusAndNotificationType(NotificationStatus.unread, notificationType, pageable).getTotalPages(), pageable.getPageSize()
        ));
        return new ResponseEntity<PagedModel<NotificationDto>>(assembler.toModel(notificationService.getNotificationByStatusAndNotificationType(NotificationStatus.unread, notificationType, pageable).map(notificationMapper::toNotificationDto)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PagedModel<NotificationDto>> getUnreadNotificationsByBranch(Pageable pageable,  JwtAuthenticationToken token, PagedResourcesAssembler assembler, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                NotificationDto.class, uriBuilder, response, pageable.getPageNumber(),  notificationService.getByNotificationTypeIn( token, pageable).getTotalPages(), pageable.getPageSize()
        ));
        return new ResponseEntity<PagedModel<NotificationDto>>(assembler.toModel(notificationService.getByNotificationTypeIn(token, pageable).map(notificationMapper::toNotificationDto)), HttpStatus.OK);
    }


    @Override
    public NotificationDto getNotification(long id) {
        return notificationMapper.toNotificationDto(notificationService.getNotification(id));
    }

    @Override
    public NotificationDto updateNotification(long id, JwtAuthenticationToken token, NotificationDto notificationDto) throws IllegalAccessException {
        return notificationMapper.toNotificationDto(notificationService.updateNotification(id, token, notificationMapper.toNotification(notificationDto)));
    }

    @Override
    public ResponseEntity<PagedModel<NotificationDto>> getNotifications(Pageable pageable, PagedResourcesAssembler assembler, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                NotificationDto.class, uriBuilder, response, pageable.getPageNumber(),  notificationService.getNotifications(pageable).getTotalPages(), pageable.getPageSize()
        ));
        return new ResponseEntity<PagedModel<NotificationDto>>(assembler.toModel(notificationService.getNotifications(pageable).map(notificationMapper::toNotificationDto)), HttpStatus.OK);
    }

    @Override
    public void deleteNotification(long id) {
        notificationService.deleteNotification(id);
    }
}
