package jpolanco.springbootapp;

import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.CategoryEntity;
import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.StaffRoleEntity;
import jpolanco.springbootapp.event.infrastructure.adapters.output.repository.JpaCategoryRepository;
import jpolanco.springbootapp.event.infrastructure.adapters.output.repository.JpaStaffRoleRepository;
import jpolanco.springbootapp.user.infrastructure.adapters.output.mysql.RoleRepositoryMySQL;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepositoryMySQL roleRepository;
    private final JpaCategoryRepository categoryRepository;
    private final JpaStaffRoleRepository staffRoleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!roleRepository.existsByName("ADMIN")) {
            roleRepository.save("ADMIN");
        }
        if (!roleRepository.existsByName("USER")) {
            roleRepository.save("USER");
        }
        if (!roleRepository.existsByName("ORGANIZER")) {
            roleRepository.save("ORGANIZER");
        }
        if (!categoryRepository.existsById("CONCERT")) {
            categoryRepository.save(new CategoryEntity("CONCERT"));
        }
        if (!categoryRepository.existsById("SPORTS")) {
            categoryRepository.save(new CategoryEntity("SPORTS"));
        }
        if (!categoryRepository.existsById("THEATER")) {
            categoryRepository.save(new CategoryEntity("THEATER"));
        }
        if (!staffRoleRepository.existsById("PANELIST")) {
            staffRoleRepository.save(new StaffRoleEntity("PANELIST"));
        }
    }
}
