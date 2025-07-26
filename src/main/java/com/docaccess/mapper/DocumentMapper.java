package com.docaccess.mapper;





import com.docaccess.dto.CreateDocumentRequest;
import com.docaccess.dto.DocumentResponse;
import com.docaccess.dto.PermissionDTO;
import com.docaccess.entity.Document;
import com.docaccess.entity.Permission;

import java.util.List;
import java.util.stream.Collectors;

public class DocumentMapper {

    private DocumentMapper() {
        // Utility class – private constructor
    }

    public static Document toEntity(CreateDocumentRequest request) {
        Document document = new Document();
        document.setName(request.getName());
        document.setContent(request.getContent());
        document.setFileType(request.getFileType());
        return document;
    }

    public static DocumentResponse toResponse(Document document) {
        return DocumentResponse.builder()
                .id(document.getId())
                .name(document.getName())
                .content(document.getContent())
                .fileType(document.getFileType())
                .accessibleUsers(toUserPermissionList(document.getPermissions()))
                .build();
    }

    private static List<PermissionDTO> toUserPermissionList(List<Permission> permissions) {
        if (permissions == null) return List.of();
        return permissions.stream()
                .map(p -> new  PermissionDTO(p.getUser().getUsername(), p. getPermission() ))
                .collect(Collectors.toList());
    }
}
