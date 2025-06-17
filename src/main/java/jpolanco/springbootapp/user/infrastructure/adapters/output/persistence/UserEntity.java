package jpolanco.springbootapp.user.infrastructure.adapters.output.persistence;


import jakarta.persistence.*;
import jpolanco.springbootapp.user.domain.model.valueobjects.UserStatus;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
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
    private UserStatus status;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private String qrFileName;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TokenEntity> tokenEntities;

    public UserEntity(UUID id, String firstName, String lastName, String email, String password, Set<RoleEntity> roles, UserStatus status, Instant createdAt, String qrFileName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.status = status;
        this.createdAt = createdAt;
        this.qrFileName = qrFileName;
    }
}
