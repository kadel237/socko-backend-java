package com.soko_backend.entity.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {

    @Id
    @Column(name = "name", length = 50, nullable = false)
    private String name;


}