package jpolanco.springbootapp.event.infrastructure.adapters.output.repository;

import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.StaffRoleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaStaffRoleRepository extends JpaRepository<StaffRoleEntity, String> {
    @Query("""
            SELECT s FROM StaffRoleEntity s
            WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))
            ORDER BY s.name
    """)
    Slice<StaffRoleEntity> searchByName(String name, Pageable pageable);
}
