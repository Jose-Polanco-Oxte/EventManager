package jpolanco.springbootapp.event.infrastructure.adapters.output.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
