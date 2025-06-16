package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.ports.output.StaffRolesRepository;
import jpolanco.springbootapp.event.application.uc.SearchStaffRolesUC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchStaffRoles implements SearchStaffRolesUC {
    private final StaffRolesRepository staffRolesRepository;
    @Override
    public List<String> search(String name, int size) {
        return staffRolesRepository.search(name, size);
    }
}
