package com.eb.disputemanagement.dispute.atmElectronicJournal;

import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Service
public interface AtmElectronicJournalService {
    String uploadAtmElectronicJournal(JwtAuthenticationToken token,MultipartFile file, long requestId, String documentType);
    Resource downloadAtmElectronicJournal(String fileName) throws Exception;
    String  getEjByFileName(Long disputeRequest_id);

}
