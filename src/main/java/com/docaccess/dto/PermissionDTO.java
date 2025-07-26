package com.docaccess.dto;


import com.docaccess.enums.PermissionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {
    private String username;
    private PermissionType permission;
}