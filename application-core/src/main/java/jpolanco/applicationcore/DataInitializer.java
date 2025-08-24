package jpolanco.applicationcore;

import jpolanco.applicationcore.user.infrastructure.adapters.output.repositories.JpaRoleRepository;
import jpolanco.domainmodel.user.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final JpaRoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (roleRepository.notExistByName(("ADMIN"))) {
            roleRepository.save(new RoleEntity("ADMIN"));
        }
        if (roleRepository.notExistByName("USER")) {
            roleRepository.save(new RoleEntity("USER"));
        }
        if (roleRepository.notExistByName("ORGANIZER")) {
            roleRepository.save(new RoleEntity("ORGANIZER"));
        }
    }
}
