package org.development.authms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse{
    private Long id;
    private String name;
    private String surname;
    private String email;

    private LocalDate birthDate;
    private String phoneNumber;
    private String message;
    private boolean deleted = false;
    private String referenceKey;

    private String accessToken;
    private String refreshToken;
}
