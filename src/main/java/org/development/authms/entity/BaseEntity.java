package org.development.authms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class BaseEntity {

    @Column(name = "deleted")
    public boolean deleted = false;
}
