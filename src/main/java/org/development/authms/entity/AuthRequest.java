package org.development.authms.entity;


import lombok.Data;

@Data
public class AuthRequest {

    private String email;
    private String password;
}
