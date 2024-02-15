package org.development.authms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@SQLDelete(sql= "UPDATE users SET deleted = true WHERE id=?")
@SQLRestriction("deleted <> true")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "email",  unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birthDate",  nullable = false)
    private LocalDate birthDate;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @Transient
    private String message;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "reference_key", nullable = false)
    private String referenceKey;
}
