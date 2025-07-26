package com.docaccess.service.impl;

import com.docaccess.dto.RegisterRequest;
import com.docaccess.entity.Document;
import com.docaccess.entity.Permission;
import com.docaccess.entity.User;
import com.docaccess.enums.PermissionType;
import com.docaccess.enums.Role;
import com.docaccess.repository.DocumentRepository;
import com.docaccess.repository.PermissionRepository;
import com.docaccess.repository.UserRepository;
import com.docaccess.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void registerUser(RegisterRequest request) {
        if (!userRepository.findByUsername(request.getUsername()).isEmpty()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(request.getRoles());

        User savedUser = userRepository.save(user);

        if (request.getRoles().contains(Role.ADMIN)) {
            assignAllPermissionsToUser(savedUser);
        }
    }

    private void assignAllPermissionsToUser(User user) {
        List<Document> allDocs = documentRepository.findAll();
        List<Permission> permissions = new ArrayList<>();

        for (Document doc : allDocs) {
            for (PermissionType type : PermissionType.values()) {
                permissions.add(Permission.builder()
                        .user(user)
                        .document(doc)
                        .permission(type)
                        .build());
            }
        }

        permissionRepository.saveAll(permissions);
    }


}
