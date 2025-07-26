package com.docaccess.dto;

import com.docaccess.enums.PermissionType;
import lombok.Data;

@Data
public class GrantPermissionRequest {
    private String username;
    private PermissionType permission;
}