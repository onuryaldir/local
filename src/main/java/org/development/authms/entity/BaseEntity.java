package org.development.authms.entity;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class BaseEntity {

    @Column(name = "deleted")
    public boolean deleted = false;
}
