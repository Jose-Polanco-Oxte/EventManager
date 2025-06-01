package jpolanco.springbootapp.event.infrastructure.adapters.output.repository;

import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.StaffRoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface JpaStaffRoleRepository extends CrudRepository<StaffRoleEntity, String> {
}
