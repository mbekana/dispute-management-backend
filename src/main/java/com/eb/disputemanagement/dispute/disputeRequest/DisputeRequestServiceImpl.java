package com.eb.disputemanagement.dispute.disputeRequest;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.config.EmployeeClient;
import com.eb.disputemanagement.dispute.notification.Notification;
import com.eb.disputemanagement.dispute.notification.NotificationService;
import com.eb.disputemanagement.dispute.notification.NotificationStatus;
import com.eb.disputemanagement.dispute.notification.NotificationType;
import com.eb.disputemanagement.dispute.utils.Util;
import com.eb.disputemanagement.dispute.common.DRequestType;
import com.eb.disputemanagement.dispute.common.RequestStatus;
import com.eb.disputemanagement.dispute.dto.EmployeeMapper;
import com.enatbanksc.disputemanagement.dispute.notification.*;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class DisputeRequestServiceImpl implements DisputeRequestService {
    private final DisputeRequestRepository disputeRequestRepository;
    private final NotificationService notificationService;
    private final EmployeeMapper employeeMapper;
    private final EmployeeClient employeeClient;

//    private final RedisTemplate redisTemplate;
//
//    private static final String KEY="DISPUTE";

    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

    @Override
    public boolean pendingRequest(String cardNumber, LocalDateTime transactionDate, BigDecimal transactionAmount, String requesterName, RequestStatus status) {
        return disputeRequestRepository.existsByCardNumberAndTransactionDateAndTransactionAmountAndCardHolderNameAndStatus(cardNumber, transactionDate, transactionAmount, requesterName, status);
    }

    @Override
    public DisputeRequest createDisputeRequest(JwtAuthenticationToken token, DisputeRequest disputeRequest) throws IllegalAccessException {
        var employeedID = Util.getEmployeeID(token);
        var employee = getEmployee(employeedID);
        if (pendingRequest(disputeRequest.getCardNumber(), disputeRequest.getTransactionDate(), disputeRequest.getTransactionAmount(), disputeRequest.getCardHolderName(), RequestStatus.INITIATION)) {
            throw new IllegalAccessException("The request you are trying to submit already exists!");
        }
        if (disputeRequest.getBankTerminal().getSwiftCode().equals("ENATETAA")) {
            /**
             * create and save dispute
             */
            disputeRequest.setStatus(RequestStatus.INITIATION);
            disputeRequest.setMaker(employee);
            disputeRequest.setRequestType(DRequestType.ENAT_ENAT);
            disputeRequest.setNewRequest(true);
            disputeRequestRepository.save(disputeRequest);

//            redisTemplate.opsForHash().put(KEY, disputeRequest.getId().toString(), disputeRequest);
            /**
             * create notification
             */
            Notification notification = new Notification();
            String content = "You have received a new request from " + disputeRequest.getCardHolderName() + " on date " + disputeRequest.getTransactionDate().format(formatter) + " to look up to!";
            notification.setDisputeRequest(disputeRequest);
            notification.setNotificationType(NotificationType.NEW);
            notification.setStatus(NotificationStatus.unread);
            notification.setContent(content);
            notificationService.createNotification(disputeRequest.getId(), token, notification);
        } else {
            /**
             * create and save notification
             */
            disputeRequest.setRequestType(DRequestType.ENAT_ON_OTHER_BANK);
            disputeRequest.setStatus(RequestStatus.INITIATION);
            disputeRequest.setMaker(employee);
            disputeRequest.setNewRequest(true);
            disputeRequestRepository.save(disputeRequest);
//            redisTemplate.opsForHash().put(KEY, disputeRequest.getId().toString(), disputeRequest);
            /**
             * create and save dispute
             */
            String content = "You have received a new request from " + disputeRequest.getCardHolderName() + " on date " + disputeRequest.getTransactionDate().format(formatter) + " to look up to!";
            Notification notification = new Notification();
            notification.setDisputeRequest(disputeRequest);
            notification.setNotificationType(NotificationType.NEW);
            notification.setStatus(NotificationStatus.unread);
            notification.setContent(content);
            notificationService.createNotification(disputeRequest.getId(), token, notification);
        }
        return disputeRequest;


    }


    @Override
    public Page<DisputeRequest> getDisputeRequests(Pageable pageable) {
//        List<DisputeRequest> fromRedis = redisTemplate.opsForHash().values(KEY);
//        var pagedFromRedis = findPaginated(1, 10, fromRedis);
//        if(fromRedis == null){
            var dr =  disputeRequestRepository.findAll(pageable);
//            redisTemplate.opsForHash().put(KEY, dr.get(), dr.get());
//            redisTemplate.expire(KEY, 10, TimeUnit.MINUTES);
//            return dr;
//        }
        return dr;
    }

    @Override
    public DisputeRequest getDisputeRequest(long id) {
//        var fromRedis = (DisputeRequest) redisTemplate.opsForHash().get(KEY, id);
//        if(fromRedis == null){
            var disputeRequest = disputeRequestRepository.findById(id).orElseThrow(() -> new OpenApiResourceNotFoundException("Dispute Request with an Id " + id + " was not found"));
//            if(disputeRequest.isPresent()){
//                redisTemplate.opsForHash().put(KEY, disputeRequest.get().getId(), disputeRequest.get());
//                return disputeRequest.get();
//            }
            return disputeRequest;
//        }
//        return fromRedis;
    }

    @Override
    public DisputeRequest updateDisputeRequest(long id, DisputeRequest disputeRequest, JwtAuthenticationToken token) throws IllegalAccessException {
        var dr = disputeRequestRepository.findById(id).get();
        if (!dr.getMaker().getEmployeeId().equals(Util.getEmployeeID(token))) {
            throw new IllegalAccessException("You are not allowed to edit this request!");
        }
        BeanUtils.copyProperties(disputeRequest, dr, Util.getNullPropertyNames(disputeRequest));
        disputeRequest.setNewRequest(true);
        disputeRequest.setStatus(RequestStatus.INITIATION);
        return disputeRequestRepository.save(dr);
    }

    @Override
    public void deleteDisputeRequest(long id, JwtAuthenticationToken token) {
        var employee = getEmployee(Util.getEmployeeID(token));
        var dr = disputeRequestRepository.findById(id).get();
        dr.setDeletedBy(employee.getFullName());
        dr.setNewRequest(false);
        dr.setDeleted(true);
        disputeRequestRepository.save(dr);
        disputeRequestRepository.deleteById(id);

    }


    //    @Transactional
    @Override
    public DisputeRequest receiveDisputeRequest(long id, JwtAuthenticationToken token, DisputeRequest disputeRequest) throws IllegalAccessException {
        var employeedID = Util.getEmployeeID(token);
        var employee = getEmployee(employeedID);
        var dr = getDisputeRequest(id);
        System.out.print(disputeRequest.getFeNumber() + disputeRequest.getDisputeId());
        if (dr == null) {
            throw new IllegalAccessException("Dispute request with that " + id + "is not found!");
        }
        if (disputeRequest.getDisputeId() != null && disputeRequest.getFeNumber() != null && existsByFeNumberAndDisputeId(disputeRequest.getFeNumber(), disputeRequest.getDisputeId())) {
            throw new IllegalAccessException("Fe Number and Dispute Id should be unique");
        }

        if (dr.getRequestType().equals(DRequestType.ENAT_ENAT) || dr.getRequestType().equals(DRequestType.NOT_FOUND_ON_SETTLEMENT)) {
            String content = "The dispute request you submitted on " + dr.getTransactionDate().format(formatter) + " with amount of " + dr.getTransactionAmount() + " is received!";
            dr.setStatus(RequestStatus.PROCESSING);
            dr.setNewRequest(false);
            dr.setReceived(true);
            dr.setReceivedBy(employee);
            dr.setReceivedDate(LocalDate.from(LocalDateTime.now()));
            disputeRequestRepository.save(dr);

            Notification rec_not = new Notification();
            rec_not.setContent(content);
            rec_not.setDisputeRequest(dr);
            rec_not.setStatus(NotificationStatus.unread);
            rec_not.setNotificationType(NotificationType.RECEIVE);
            rec_not.setRequestInitiatorBranch(dr.getMaker().getBranch().getCode());
            notificationService.createNotification(id, token, rec_not);
        } else if (dr.getRequestType().equals(DRequestType.ENAT_ON_OTHER_BANK)) {
            String content = "The dispute request you submitted on " + dr.getTransactionDate().format(formatter) + " with amount of " + dr.getTransactionAmount() + " is received!";
            dr.setStatus(RequestStatus.PROCESSING);
            dr.setNewRequest(false);
            dr.setReceived(true);
            dr.setFeNumber(disputeRequest.getFeNumber());
            dr.setDisputeId(disputeRequest.getDisputeId());
            dr.setReceivedBy(employee);
            dr.setReceivedDate(LocalDate.from(LocalDateTime.now()));
            disputeRequestRepository.save(dr);

            Notification rec_not = new Notification();
            rec_not.setContent(content);
            rec_not.setDisputeRequest(dr);
            rec_not.setStatus(NotificationStatus.unread);
            rec_not.setNotificationType(NotificationType.RECEIVE);
            rec_not.setRequestInitiatorBranch(dr.getMaker().getBranch().getCode());
            notificationService.createNotification(id, token, rec_not);
        }
        return dr;
    }

    @Override
    public DisputeRequest approveRequest(long id, JwtAuthenticationToken token, DisputeRequest disputeRequest) throws IllegalAccessException {
        var dr = getDisputeRequest(id);
        var employeedID = Util.getEmployeeID(token);
        var employee = getEmployee(employeedID);
        String content = "The dispute request you submitted on " + dr.getTransactionDate().format(formatter) + " with amount of " + dr.getTransactionAmount() + " is approved!";
        if (!dr.isAcknowledged()) {
            throw new IllegalAccessException("You can not approve request at this stage!");
        }

        dr.setStatus(RequestStatus.APPROVED);
        dr.setApproved(true);
        dr.setApprovedBy(employee.getFullName());
        dr.setApprovedDate(LocalDateTime.now());
        disputeRequestRepository.save(dr);
        Notification not = new Notification();
        not.setContent(content);
        not.setDisputeRequest(dr);
        not.setRequestInitiatorBranch(dr.getMaker().getBranch().getCode());
        not.setTargetBranch(dr.getTerminalOwnerBranch());
        not.setStatus(NotificationStatus.unread);
        not.setNotificationType(NotificationType.APPROVE);
        notificationService.createNotification(id, token, not);
        return disputeRequest;
    }

    @Override
    public DisputeRequest settleRequest(long id, JwtAuthenticationToken token, DisputeRequest disputeRequest) throws IllegalAccessException {
        var dr = getDisputeRequest(id);
        var employeeId = Util.getEmployeeID(token);
        var employee = getEmployee(employeeId);
        dr.setStatus(RequestStatus.SETTLED);
        dr.setSettledBy(employee.getFullName());
        dr.setSettlementDate(LocalDateTime.now());
        disputeRequestRepository.save(dr);
        return dr;
    }

    @Override
    public DisputeRequest markAsNotFound(long id, JwtAuthenticationToken token, DisputeRequest disputeRequest) {
        var dr = getDisputeRequest(id);
        dr.setRequestType(DRequestType.NOT_FOUND_ON_SETTLEMENT);
        BeanUtils.copyProperties(disputeRequest, dr, Util.getNullPropertyNames(disputeRequest));
        return disputeRequestRepository.save(dr);
    }

    @Override
    public DisputeRequest acknowledgeRequest(long id, JwtAuthenticationToken token, DisputeRequest disputeRequest) throws IllegalAccessException {
        var dr = disputeRequestRepository.findById(id).orElseThrow(() -> new OpenApiResourceNotFoundException("Dispute Request with an Id " + id + " was not found"));
        var employeedID = Util.getEmployeeID(token);
        var employee = getEmployee(employeedID);
        if (dr.getRequestType().equals(DRequestType.ENAT_ENAT) && (dr.getTerminalOwnerBranch() == null && dr.getAtmElectronicJournal() == null)) {
            throw new IllegalAccessException("Please add payer branch before acknowledging this dispute request.");
        }
        dr.setAcknowledged(true);
        dr.setSettlementDate(disputeRequest.getSettlementDate());
        dr.setAcknowledgedBy(employee.getFullName());
        dr.setSettledBy(employee.getFullName());
        return disputeRequestRepository.save(dr);
    }

    @Override
    public Page<DisputeRequest> getByRequestInitiator(JwtAuthenticationToken token, Pageable pageable) {
        return disputeRequestRepository.findByMaker_EmployeeIdOrderByUpdatedAtDesc(Util.getEmployeeID(token), pageable);
    }

    @Override
    public Page<DisputeRequest> getDisputeByRequestTypeAndStatus(DRequestType requestType, RequestStatus status, Pageable pageable) {
        return disputeRequestRepository.findByRequestTypeAndStatus(requestType, status, pageable);
    }

    @Override
    public Page<DisputeRequest> getByRequestInitiatorBranchAndRequestType(JwtAuthenticationToken token, DRequestType requestType, Pageable pageable) {
        var employeeId = Util.getEmployeeID(token);
        var employee = getEmployee(employeeId);
        return disputeRequestRepository.findByMaker_BranchCodeAndRequestType(employee.getBranch().getCode(), requestType, pageable);
    }

    @Override
    public Page<DisputeRequest> getLastFiveNewDisputeRequests(JwtAuthenticationToken token, boolean newRequest, Pageable pageable) {
        var employeeId = Util.getEmployeeID(token);
        var employee = getEmployee(employeeId);
        return disputeRequestRepository.findFirst5ByMaker_BranchCodeAndNewRequest(employee.getBranch().getCode(), true, pageable);
    }

    @Override
    public Page<DisputeRequest> getByPayerBranch(JwtAuthenticationToken token, Pageable pageable) {
        var employeeId = Util.getEmployeeID(token);
        var employee = getEmployee(employeeId);
        return disputeRequestRepository.findByTerminalOwnerBranch(employee.getBranch().getCode(), pageable);
    }

    @Override
    public Page<DisputeRequest> getByBranchCode(JwtAuthenticationToken token, Pageable pageable) {
        var employeeId = Util.getEmployeeID(token);
        var employee = getEmployee(employeeId);
        return disputeRequestRepository.findByMaker_BranchCode(employee.getBranch().getCode(), pageable);
    }

    @Override
    public Page<DisputeRequest> getExpiringRequests(boolean received, DRequestType requestType, RequestStatus status, LocalDate receivedDate, Pageable pageable) {
        return disputeRequestRepository.findByReceivedAndRequestTypeAndStatusAndReceivedDate(received, requestType, status, receivedDate, pageable);
    }

    @Override
    public Page<DisputeRequest> getByAcknowledgedAndRequestTypeAndStatus(boolean acknowledged, DRequestType requestType, RequestStatus status, Pageable pageable) {
        return disputeRequestRepository.findByAcknowledgedAndRequestTypeAndStatus(true, requestType, status, pageable);
    }

    @Override
    public DisputeRequest declineDisputeRequest(long id, JwtAuthenticationToken token, DisputeRequest disputeRequest) {
        var dr = getDisputeRequest(id);
        var employeeID = Util.getEmployeeID(token);
        var employee = getEmployee(employeeID);

        if (disputeRequest.getStatus().equals(RequestStatus.REVERSED)) {
            String content = "The request you submitted on date " + dr.getTransactionDate().format(formatter) + " with amount " + dr.getTransactionAmount() + " is declined because it is a reversed transaction!";
            dr.setStatus(RequestStatus.REVERSED);
            dr.setDeclined(true);
            dr.setDeclinedBy(employee.getFullName());
            dr.setDeclineReason(disputeRequest.getDeclineReason());
            disputeRequestRepository.save(dr);

            Notification dn = new Notification();
            dn.setContent(content);
            dn.setDisputeRequest(dr);
            dn.setRequestInitiatorBranch(dr.getTerminalOwnerBranch());
            dn.setTargetBranch(dr.getTerminalOwnerBranch());
            dn.setStatus(NotificationStatus.unread);
            notificationService.createNotification(id, token, dn);
        } else if (disputeRequest.getStatus().equals(RequestStatus.DECLINED)) {
            String content = "The request you submitted on date " + dr.getTransactionDate().format(formatter) + " with amount " + dr.getTransactionAmount() + " is declined! Please check the request according to the decline reason";
            dr.setStatus(RequestStatus.DECLINED);
            dr.setDeclined(true);
            dr.setDeclinedBy(employee.getFullName());
            dr.setDeclineReason(disputeRequest.getDeclineReason());
            disputeRequestRepository.save(dr);

            Notification dn = new Notification();
            dn.setContent(content);
            dn.setDisputeRequest(dr);
            dn.setRequestInitiatorBranch(dr.getTerminalOwnerBranch());
            dn.setTargetBranch(dr.getTerminalOwnerBranch());
            dn.setStatus(NotificationStatus.unread);
            notificationService.createNotification(id, token, dn);
        }
        return dr;
    }

    @Override
    public boolean existsByFeNumberAndDisputeId(@NotNull String feNumber, @NotNull String disputeId) {
        return disputeRequestRepository.existsByFeNumberAndDisputeId(feNumber, disputeId);
    }

    @Override
    public Page<DisputeRequest> searchDisputeRequest(Specification<DisputeRequest> search, Pageable pageable) {
        return disputeRequestRepository.findAll(where(search), pageable);
    }

    @Override
    public List<DisputeRequest> findByReceivedAndRequestTypeAndStatusAndMailSent(boolean received, DRequestType requestType, RequestStatus status, boolean mailSent) {
        return disputeRequestRepository.findByReceivedAndRequestTypeAndStatusAndMailSent(received, requestType, status, mailSent);
    }

    @Override
    public Page<DisputeRequest> getFirst5ByNewRequest(boolean newRequest, Pageable pageable) {
        return disputeRequestRepository.findFirst5ByNewRequest(newRequest, pageable);
    }

    @Override
    public DisputeRequest confirmSettlement(long id, JwtAuthenticationToken token) {
        var dr = getDisputeRequest(id);
        var employeeId = Util.getEmployeeID(token);
        var employee = getEmployee(employeeId);
        dr.setSettledBy(employee.getFullName());
        dr.setSettlementDate(LocalDateTime.now());
        dr.setStatus(RequestStatus.SETTLED);
        disputeRequestRepository.save(dr);
        return dr;
    }

    @Override
    public Page<DisputeRequest> getAcknowledgedDisputeRequests(boolean acknowledged, Pageable pageable) {
        return disputeRequestRepository.findByAcknowledged(acknowledged, pageable);
    }

    private Employee getEmployee(String employeeId) {
        return employeeMapper.employeeDtoToEmployee(employeeClient.getEmployeeById(employeeId));
    }


}
