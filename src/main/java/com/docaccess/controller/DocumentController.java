package com.docaccess.controller;


import com.docaccess.dto.*;
import com.docaccess.security.UserPrincipal;
import com.docaccess.service.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {
    private static final Logger logger =  LoggerFactory.getLogger(DocumentController.class);

    private final DocumentService documentService;

    // Create Document - only admin
    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateDocumentRequest request) {
        logger.info("User '{}' is creating a new document with name '{}'", userPrincipal.getUsername(), request.getName());

        DocumentResponse created = documentService.createDocument(userPrincipal.getUsername(), request);
        return ResponseEntity.ok(created);
    }

    // List accessible documents (READ permission)
    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getAccessibleDocuments(
            @AuthenticationPrincipal UserPrincipal userPrincipal   ) {
        logger.info("User '{}' is requesting accessible documents", userPrincipal.getUsername());

        List<DocumentResponse> documents = documentService.getAccessibleDocuments(userPrincipal.getUsername());
        return ResponseEntity.ok(documents);
    }

    // Get document by id
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocumentById(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id) {
        logger.info("User '{}' is fetching document with ID {}", userPrincipal.getUsername(), id);

        DocumentResponse document = documentService.getDocumentById(userPrincipal.getUsername(), id);
        return ResponseEntity.ok(document);
    }

    // Delete document
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id) {
        logger.info("User '{}' is deleting document with ID {}", userPrincipal.getUsername(), id);

        documentService.deleteDocument(userPrincipal.getUsername(), id);
        ApiResponse<Void> response = new ApiResponse<>("Document deleted successfully", null);
        return ResponseEntity.ok(response);

    }

    // Grant permission
    @PostMapping("/{id}/grant")
    public ResponseEntity<?> grantPermission(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @Valid @RequestBody GrantPermissionRequest request) {

        documentService.grantPermission(userPrincipal.getUsername(), id, request);
        ApiResponse<Void> response = new ApiResponse<>("Permission granted successfully", null);
        return ResponseEntity.ok(response);
    }

    // Batch permission check
    @PostMapping("/access-check")
    public ResponseEntity<AccessCheckResponse> batchAccessCheck(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody AccessCheckRequest request) {
        logger.info("User '{}' is performing batch access check for permission '{}' on documents {}",
                userPrincipal.getUsername(), request.getPermission(), request.getDocumentIds());
        AccessCheckResponse  response = documentService.batchAccessCheck(userPrincipal.getUsername(), request);
        return ResponseEntity.ok(response);
    }
}
