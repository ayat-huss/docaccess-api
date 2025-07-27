package com.docaccess.service.impl;

import com.docaccess.dto.*;
import com.docaccess.entity.Document;
import com.docaccess.entity.Permission;
import com.docaccess.entity.User;
import com.docaccess.enums.DocumentStatus;
import com.docaccess.enums.PermissionType;
import com.docaccess.exception.ResourceNotFoundException;
import com.docaccess.mapper.DocumentMapper;
import com.docaccess.repository.DocumentRepository;
import com.docaccess.repository.PermissionRepository;
import com.docaccess.repository.UserRepository;
import com.docaccess.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final AccessControlService accessControlService;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public DocumentResponse createDocument(String username, CreateDocumentRequest request) {
        if (!accessControlService.canCreateDocument(username)) {
            throw new AccessDeniedException("Only admin can create documents");
        }

        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Document document = new Document();
        document.setName(request.getName());
        document.setContent(request.getContent());
        document.setFileType(request.getFileType());
        document.setCreatedBy(creator);

        Document saved = documentRepository.save(document);

        return DocumentMapper.toResponse(saved);
    }


    public List<DocumentResponse> getAccessibleDocuments(String username) {
        if (accessControlService.isAdmin(username)) {
            return documentRepository.findAll().stream()
                    .map(DocumentMapper::toResponse)
                    .collect(Collectors.toList());
        }

        List<Document> docs = documentRepository.findDocumentsByUserWithPermission(username, PermissionType.READ);
        return docs.stream()
                .map(DocumentMapper::toResponse)
                .collect(Collectors.toList());
    }
@Override
public DocumentResponse getDocumentById(String username, Long id) {
    logger.info("User '{}' requested document with ID {}", username, id);

    Document document = documentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

    if (!accessControlService.hasPermission(username, id, PermissionType.READ)) {
        logger.warn("Access denied for user '{}' to READ document ID {}", username, id);

        throw new AccessDeniedException("No READ permission for this document");
    }
    logger.debug("User '{}' has READ access to document ID {}", username, id);

    return DocumentMapper.toResponse(document);
}


    @Override
    @Transactional

    public void deleteDocument(String username, Long id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        if (!accessControlService.hasPermission(username, id, PermissionType.DELETE)) {
            logger.warn("Access denied: User '{}' does not have DELETE permission on document ID {}", username, id);
            throw new AccessDeniedException("No DELETE permission for this document");
        }

        doc.setStatus(DocumentStatus.DELETED);
        documentRepository.save(doc); // Soft delete
        logger.info("Document ID {} marked as DELETED by user '{}'", id, username);
    }


    @Override
    @Transactional
    public void grantPermission(String username, Long documentId, GrantPermissionRequest request) {
        logger.info("User '{}' granting {} permission on document ID {} to user '{}'",
                username, request.getPermission(), documentId, request.getUsername());
        if (!accessControlService.canGrantPermission(username, documentId)) {
            throw new AccessDeniedException("No WRITE permission or not admin");
        }

        User targetUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> {
                    logger.warn("Document with ID {} not found", documentId);
                    return new ResourceNotFoundException("Document not found");
                });

        boolean exists = permissionRepository.existsPermission(request.getUsername(), document.getId(), request.getPermission());
        if (exists) {
            throw new IllegalStateException("Permission already granted");
        }

        Permission permission = new Permission();
        permission.setUser(targetUser);
        permission.setDocument(document);
        permission.setPermission(request.getPermission());

        permissionRepository.save(permission);
    }

    @Override
    public AccessCheckResponse batchAccessCheck(String username, AccessCheckRequest request) {
        Set<Long> accessibleIds;

        if (accessControlService.isAdmin(username)) {
            accessibleIds = new HashSet<>( request.getDocumentIds()); // Admin sees all
        } else {
            accessibleIds = permissionRepository.findDocumentIdsByUsernameAndPermissionInDocumentList(
                    username,
                    request.getPermission(),
                    request.getDocumentIds()
            );
        }

        return new AccessCheckResponse(accessibleIds);
    }

}