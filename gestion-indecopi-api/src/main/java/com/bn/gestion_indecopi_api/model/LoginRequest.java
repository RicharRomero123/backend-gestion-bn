package com.bn.gestion_indecopi_api.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}