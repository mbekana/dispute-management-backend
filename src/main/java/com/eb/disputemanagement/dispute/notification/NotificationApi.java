package com.eb.disputemanagement.dispute.notification;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

public interface NotificationApi {

    @PostMapping("/request/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    NotificationDto createNotification(long id, JwtAuthenticationToken token, @RequestBody @Valid NotificationDto notificationDto);

    @GetMapping("/unread")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<PagedModel<NotificationDto>> getAllUnreadNotifications(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
              @Valid Pageable pageable,
              NotificationStatus status,
              @RequestParam("notificationType") NotificationType notificationType
            , PagedResourcesAssembler assembler
            , UriComponentsBuilder uriBuilder

            , final HttpServletResponse response);

    @GetMapping("/unread/branch")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<PagedModel<NotificationDto>> getUnreadNotificationsByBranch(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
              @Valid Pageable pageable,
              JwtAuthenticationToken token
            , PagedResourcesAssembler assembler
            , UriComponentsBuilder uriBuilder
            , final HttpServletResponse response);


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    NotificationDto getNotification(@PathVariable("id") long id);

    @PutMapping("/{id}")
    NotificationDto updateNotification(@PathVariable("id") long id, JwtAuthenticationToken token, NotificationDto notificationDto) throws IllegalAccessException;

    @GetMapping()
    ResponseEntity<PagedModel<NotificationDto>> getNotifications(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                 @Valid Pageable pageable
            , PagedResourcesAssembler assembler
            , UriComponentsBuilder uriBuilder

            , final HttpServletResponse response);


    @DeleteMapping("/{id}")
    void deleteNotification(@PathVariable("id") long id);
}
