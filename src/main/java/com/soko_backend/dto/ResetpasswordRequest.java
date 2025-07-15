package com.soko_backend.dto;

import lombok.Data;

@Data
public class ResetpasswordRequest {
    private String email;
    private String code;
    private String newPassword;
}
