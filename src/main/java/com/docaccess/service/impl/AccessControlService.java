package com.docaccess.service.impl;


import com.docaccess.enums.PermissionType;

import com.docaccess.enums.Role;
import com.docaccess.repository.PermissionRepository;
import com.docaccess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessControlService {

    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public boolean isAdmin(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRoles().contains(Role.ADMIN))
                .orElse(false);
    }

    public boolean hasPermission(String username, Long documentId, PermissionType permission) {
        if (isAdmin(username)) return true;
        return permissionRepository.existsPermission(username, documentId, permission);
    }

    public boolean canGrantPermission(String username, Long documentId) {
        if (isAdmin(username)) return true;
        return hasPermission(username, documentId, PermissionType.WRITE);
    }

    public boolean canCreateDocument(String username) {
        return isAdmin(username);
    }
}
