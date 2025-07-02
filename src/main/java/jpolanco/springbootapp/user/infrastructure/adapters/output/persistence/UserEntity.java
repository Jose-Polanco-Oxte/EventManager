package jpolanco.springbootapp.user.infrastructure.adapters.output.persistence;


import jakarta.persistence.*;
import jpolanco.springbootapp.user.domain.model.value_objects.UserStatus;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_uuid", columnList = "uuid", unique = true),
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_email_lower", columnList = "email_lower"),
        @Index(name = "idx_user_first_name", columnList = "first_name"),
        @Index(name = "idx_user_first_name_lower", columnList = "first_name_lower"),
        @Index(name = "idx_user_last_name", columnList = "last_name"),
        @Index(name = "idx_user_last_name_lower", columnList = "last_name_lower"),
        @Index(name = "idx_user_created_at", columnList = "created_at"),
        @Index(name = "idx_user_full_name_lower", columnList = "full_name_lower")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "first_name_lower", nullable = false)
    private String firstNameLower;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "last_name_lower", nullable = false)
    private String lastNameLower;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "full_name_lower", nullable = false, unique = true)
    private String fullNameLower;

    @Column(name = "email_lower", nullable = false, unique = true)
    private String emailLower;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private String qrFileName;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TokenEntity> tokenEntities;

    public UserEntity(Long userId, UUID uuid, String firstName, String lastName, String email, String password, Set<RoleEntity> roles, UserStatus status, Instant createdAt, String qrFileName) {
        this.id = userId;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.status = status;
        this.createdAt = createdAt;
        this.qrFileName = qrFileName;
    }

    @PrePersist
    @PreUpdate
    private void updateFullNameAndEmailLower() {
        if (firstName != null) {
            firstNameLower = firstName.toLowerCase();
        }
        if (lastName != null) {
            lastNameLower = lastName.toLowerCase();
        }
        if (email != null) {
            emailLower = email.toLowerCase();
        }
        fullNameLower = (firstNameLower + " " + lastNameLower).trim().toLowerCase();
    }
}
