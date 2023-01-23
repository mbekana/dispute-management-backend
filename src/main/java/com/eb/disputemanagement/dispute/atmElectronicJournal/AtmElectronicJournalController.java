package com.eb.disputemanagement.dispute.atmElectronicJournal;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/upload-ej-file")
@RequiredArgsConstructor
public class AtmElectronicJournalController implements AtmElectronicJournalApi{
    private final AtmElectronicJournalService atmElectronicJournalService;

    @Override
    public String uploadAtmElectronicJournal(JwtAuthenticationToken token, MultipartFile file, long requestId, String documentType) throws IllegalAccessException, IOException {
        return atmElectronicJournalService.uploadAtmElectronicJournal(token, file, requestId, documentType);
    }
    @Override
    public ResponseEntity<Resource> downloadEJ(long requestId, HttpServletRequest request) {
        String fileName = atmElectronicJournalService.getEjByFileName(requestId);
        Resource resource = null;
        if (fileName != null && !fileName.isEmpty()) {
            try {
                resource = atmElectronicJournalService.downloadAtmElectronicJournal(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Try to determine file's content type
            String contentType = null;

            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                //logger.info("Could not determine file type.");
            }
            // Fallback to the default content type if type could not be determined
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
