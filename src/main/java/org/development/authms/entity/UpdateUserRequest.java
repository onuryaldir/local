package org.development.authms.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateUserRequest {

    private String name;

    private String surname;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)    @Column(name = "password", nullable = false)
    private String password;

    private LocalDate birthDate;

    private String phoneNumber;
}
