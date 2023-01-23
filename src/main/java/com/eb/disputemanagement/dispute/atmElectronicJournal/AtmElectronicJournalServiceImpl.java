package com.eb.disputemanagement.dispute.atmElectronicJournal;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.common.RequestStatus;
import com.eb.disputemanagement.dispute.config.EmployeeClient;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequestRepository;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequestService;
import com.eb.disputemanagement.dispute.dto.EmployeeMapper;
import com.eb.disputemanagement.dispute.exceptions.AtmElectronicJournalStorageException;
import com.eb.disputemanagement.dispute.notification.Notification;
import com.eb.disputemanagement.dispute.notification.NotificationService;
import com.eb.disputemanagement.dispute.notification.NotificationStatus;
import com.eb.disputemanagement.dispute.notification.NotificationType;
import com.eb.disputemanagement.dispute.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class AtmElectronicJournalServiceImpl implements AtmElectronicJournalService {
    private final AtmElectronicJournalRepository atmElectronicJournalRepository;
    private final DisputeRequestRepository disputeRequestRepository;
    private final DisputeRequestService disputeRequestService;
    private final NotificationService notificationService;
    private final EmployeeMapper employeeMapper;
    private final EmployeeClient employeeClient;

    private  Path fileStorageLocation;


    @Autowired
    public void AtmElectronicJournalServiceImpl(AtmElectronicJournal fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new AtmElectronicJournalStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }

    }


    private Employee getEmployee(String employeeId) {
        return employeeMapper.employeeDtoToEmployee(employeeClient.getEmployeeById(employeeId));
    }

    @Override
    public String uploadAtmElectronicJournal(JwtAuthenticationToken token, MultipartFile file, long requestId, String documentType){
        var dr = disputeRequestRepository.findById(requestId).get();
        System.out.print(dr);
        var employeeId = Util.getEmployeeID(token);
        var employee = getEmployee(employeeId);
        var content = "As per the dispute request cash was taken, please refer the attached ej report and inform to your customer.";
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = "";

        try {
        if (originalFileName.contains("..")) {
            throw new AtmElectronicJournalStorageException("Sorry! Filename contains invalid path sequence " + originalFileName);
        }

        String fileExtension = "";
        try {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        } catch (Exception e) {
            fileExtension = "";
        }
        fileName = requestId + "_" + dr.getCardHolderName() + "_" + documentType + fileExtension;
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        AtmElectronicJournal atmElectronicJournal = atmElectronicJournalRepository.findAtmElectronicJournalByDisputeRequestAndDocumentType(dr, documentType);
        if(atmElectronicJournal != null) {
            /**
             * create n save notification
             * **/
            Notification notification = new Notification();
            notification.setNotificationType(NotificationType.ALREADY_PAID);
            notification.setStatus(NotificationStatus.unread);
            notification.setContent(content);
            notification.setDisputeRequest(dr);
            notification.setRequestInitiatorBranch(dr.getMaker().getBranch().getCode());
            notificationService.createNotification(dr.getId(), token, notification);
            /**
             * Save uploaded file methadata
             * **/
            atmElectronicJournal.setDocumentFormat(file.getContentType());
            atmElectronicJournal.setFileName(fileName);
            atmElectronicJournal.setDocumentType(documentType);
            atmElectronicJournal.setDisputeRequest(dr);
            atmElectronicJournal.setUploadedBy(employee);
            atmElectronicJournalRepository.save(atmElectronicJournal);
            /**
             * update dr
             * **/
            dr.setAcknowledged(true);
            dr.setAtmElectronicJournal(atmElectronicJournal);
            dr.setStatus(RequestStatus.REVERSED);
            disputeRequestRepository.save(dr);
        } else {

            Notification notification = new Notification();
            notification.setNotificationType(NotificationType.ALREADY_PAID);
            notification.setStatus(NotificationStatus.unread);
            notification.setContent(content);
            notification.setDisputeRequest(dr);
            notification.setRequestInitiatorBranch(dr.getMaker().getBranch().getCode());
            notificationService.createNotification(dr.getId(), token, notification);
            AtmElectronicJournal newAej = new AtmElectronicJournal();
            newAej.setDisputeRequest(dr);
            newAej.setDocumentFormat(file.getContentType());
            newAej.setFileName(fileName);
            newAej.setDocumentType(documentType);
            newAej.setDisputeRequest(dr);
            newAej.setUploadedBy(employee);
            atmElectronicJournalRepository.save(newAej);

            dr.setAcknowledged(true);
            dr.setAtmElectronicJournal(newAej);
            dr.setStatus(RequestStatus.REVERSED);
            dr.setNewRequest(false);
            disputeRequestRepository.save(dr);
        }
        return fileName;
        } catch (IOException ex) {
            throw new AtmElectronicJournalStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }


    @Override
    public Resource downloadAtmElectronicJournal(String fileName) throws Exception{
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            }
            else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }

    @Override
    public String getEjByFileName(Long disputeRequest_id) {
        return atmElectronicJournalRepository.getUploadDocumnetPath(disputeRequest_id);
    }

}
