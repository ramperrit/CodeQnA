package com.codeqna.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFileDto {
    private String originalFileName;
    private String savedFileName;
}