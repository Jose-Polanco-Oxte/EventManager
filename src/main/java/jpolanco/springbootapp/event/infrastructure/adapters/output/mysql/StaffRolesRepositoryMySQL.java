package jpolanco.springbootapp.event.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.event.application.ports.output.StaffRolesRepository;
import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.StaffRoleEntity;
import jpolanco.springbootapp.event.infrastructure.adapters.output.repository.JpaStaffRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class StaffRolesRepositoryMySQL implements StaffRolesRepository {
    private final JpaStaffRoleRepository jpaStaffRoleRepository;

    @Override
    public List<String> search(String name, int size) {
        return jpaStaffRoleRepository.searchByName(name, PageRequest.of(0, size))
                .map(StaffRoleEntity::getName)
                .toList();
    }

    @Override
    public String save(String entity) {
        StaffRoleEntity staffRoleEntity = new StaffRoleEntity();
        staffRoleEntity.setName(entity);
        jpaStaffRoleRepository.save(staffRoleEntity);
        return staffRoleEntity.getName();
    }

    @Override
    public Optional<String> findById(String s) {
        return jpaStaffRoleRepository.findById(s)
                .map(StaffRoleEntity::getName);
    }

    @Override
    public void deleteById(String s) {
        jpaStaffRoleRepository.deleteById(s);
    }
}
