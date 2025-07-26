package com.docaccess.dto;


import com.docaccess.enums.PermissionType;
import lombok.Data;

import java.util.List;

@Data
public class AccessCheckRequest {
    private PermissionType permission;
    private List<Long> documentIds;
}