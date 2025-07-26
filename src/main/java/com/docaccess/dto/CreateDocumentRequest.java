package com.docaccess.dto;


import lombok.Data;

@Data
public class CreateDocumentRequest {
    private String name;
    private String content;
    private String fileType;
}
