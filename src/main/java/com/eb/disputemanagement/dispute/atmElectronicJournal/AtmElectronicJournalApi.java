package com.eb.disputemanagement.dispute.atmElectronicJournal;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.IOException;

public interface AtmElectronicJournalApi {

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadAtmElectronicJournal(JwtAuthenticationToken token,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("requestId") long requestId,
                                             @RequestParam("documentType") String documentType) throws IllegalAccessException, IOException;

    @GetMapping()
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Resource> downloadEJ(@RequestParam("requestId") long requestId, HttpServletRequest request);

}
