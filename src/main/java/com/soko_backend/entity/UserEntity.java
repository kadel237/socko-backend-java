package com.soko_backend.entity;

import com.soko_backend.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "login", length = 50, nullable = false)
    private String login;

    private String name;
    private String surname;
    private String username;

    @Column(nullable = false)
    private boolean verified = false;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles", // nom de la table de jointure
            joinColumns = @JoinColumn(name = "user_id"),  // clé étrangère vers UserEntity
            inverseJoinColumns = @JoinColumn(name = "role_name") // clé étrangère vers RoleEntity (name)
    )
    private Set<RoleEntity> roles = new HashSet<>();

    // Relations avec Shop, Order, Notification

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role.name());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }

}
