package jpolanco.springbootapp;

import jpolanco.springbootapp.user.infrastructure.adapters.output.mysql.RoleRepositoryMySQL;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepositoryMySQL roleRepository;

    @Override
    public void run(String... args) {
        if (!roleRepository.existsByName("ADMIN")) {
            roleRepository.save("ADMIN");
        }
        if (!roleRepository.existsByName("USER")) {
            roleRepository.save("USER");
        }
        if (!roleRepository.existsByName("ORGANIZER")) {
            roleRepository.save("ORGANIZER");
        }
    }
}
