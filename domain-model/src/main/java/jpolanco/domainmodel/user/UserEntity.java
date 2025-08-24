package jpolanco.domainmodel.user;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_uuid", columnList = "uuid", unique = true),
        @Index(name = "idx_user_first_name", columnList = "first_name"),
        @Index(name = "idx_user_first_name_lower", columnList = "first_name_lower"),
        @Index(name = "idx_user_last_name", columnList = "last_name"),
        @Index(name = "idx_user_last_name_lower", columnList = "last_name_lower"),
        @Index(name = "idx_user_created_at", columnList = "created_at"),
        @Index(name = "idx_user_full_name_lower", columnList = "full_name_lower")
})
@Data
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

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
    private UserStatusE status;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private String qrFileName;

    @Column(name = "first_name_lower", nullable = false)
    private String firstNameLower;

    @Column(name = "last_name_lower", nullable = false)
    private String lastNameLower;

    @Column(name = "full_name_lower", nullable = false)
    private String fullNameLower;

    @Column(nullable = false)
    private boolean deleted = false;

    public UserEntity(Long id, UUID uuid, String firstName, String lastName, String email, String password, Set<RoleEntity> roles, UserStatusE status, Instant createdAt, String qrFileName, String firstNameLower, String lastNameLower, String fullNameLower, boolean deleted) {
        this.id = id;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.status = status;
        this.createdAt = createdAt;
        this.qrFileName = qrFileName;
        this.firstNameLower = firstNameLower;
        this.lastNameLower = lastNameLower;
        this.fullNameLower = fullNameLower;
        this.deleted = deleted;
    }

    public UserEntity(Long id, UUID uuid, String firstName, String lastName, String email, String password, Set<RoleEntity> roles, UserStatusE status, Instant createdAt, String qrFileName) {
        this.id = id;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.status = status;
        this.createdAt = createdAt;
        this.qrFileName = qrFileName;
        updateFullNameAndEmailLower();
    }

    public UserEntity withId(Long userId) {
        return new UserEntity(
                userId,
                this.uuid,
                this.firstName,
                this.lastName,
                this.email,
                this.password,
                this.roles,
                this.status,
                this.createdAt,
                this.qrFileName,
                this.firstNameLower,
                this.lastNameLower,
                this.fullNameLower,
                this.deleted
        );
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
        fullNameLower = (firstNameLower + " " + lastNameLower).trim().toLowerCase();
    }
}
