package jpolanco.springbootapp.event.infrastructure.adapters.output.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "staff_roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffRoleEntity {
    @Id
    private String name;
}
