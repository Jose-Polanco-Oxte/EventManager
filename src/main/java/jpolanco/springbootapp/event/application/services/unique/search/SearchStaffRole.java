package jpolanco.springbootapp.event.application.services.unique.search;

import jpolanco.springbootapp.event.application.ports.output.StaffRolesRepository;
import jpolanco.springbootapp.event.application.uc.unique.search.SearchStaffRoleUC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchStaffRole implements SearchStaffRoleUC {
    private final StaffRolesRepository staffRolesRepository;

    @Override
    public List<String> search(String name, int size) {
        return staffRolesRepository.search(name, size);
    }
}
