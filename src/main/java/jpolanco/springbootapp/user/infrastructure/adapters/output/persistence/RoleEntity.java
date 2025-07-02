package jpolanco.springbootapp.user.infrastructure.adapters.output.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "roles", indexes = {
        @Index(name = "idx_role_name", columnList = "name", unique = true),
        @Index(name = "idx_role_name_lower", columnList = "name_lower", unique = true)
})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "description", unique = true)
    private String description;

    @Column(name = "name_lower", nullable = false, unique = true)
    private String nameLower;

    public RoleEntity(String name) {
        this.name = name;
    }

    @PrePersist
    @PreUpdate
    private void prePersistAndUpdate() {
        if (name != null) {
            nameLower = name.toLowerCase();
        }
    }
}
