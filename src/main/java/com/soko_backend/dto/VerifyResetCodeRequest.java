package com.soko_backend.dto;

import lombok.Data;

@Data
public class VerifyResetCodeRequest {
    private String email;
    private String code;
}