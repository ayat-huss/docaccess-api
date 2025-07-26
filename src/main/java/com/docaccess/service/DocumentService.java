package com.docaccess.service;

import com.docaccess.dto.*;

import java.util.List;

public interface DocumentService {

    public DocumentResponse  createDocument(String username, CreateDocumentRequest request);
    public List<DocumentResponse> getAccessibleDocuments(String username );
    public DocumentResponse getDocumentById(String username, Long id);
    public void deleteDocument(String username, Long id);
    public void grantPermission(String username, Long documentId, GrantPermissionRequest request);
    public AccessCheckResponse batchAccessCheck(String username, AccessCheckRequest request);
}
