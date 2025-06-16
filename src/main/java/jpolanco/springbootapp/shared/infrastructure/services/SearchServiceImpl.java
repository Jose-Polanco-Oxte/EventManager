package jpolanco.springbootapp.shared.infrastructure.services;

import jpolanco.springbootapp.event.application.uc.SearchCategoriesUC;
import jpolanco.springbootapp.event.application.uc.SearchEventByNameUC;
import jpolanco.springbootapp.event.application.uc.SearchMyEventByNameUC;
import jpolanco.springbootapp.event.application.uc.SearchStaffRolesUC;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.CategoriesResponseDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponseDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.StaffRolesResponseDto;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto.EventDtoCreator;
import jpolanco.springbootapp.user.application.uc.SearchUserByEmailUC;
import jpolanco.springbootapp.user.application.uc.SearchUserByNameUC;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponseDto;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.UserDtoCreator;
import jpolanco.springbootapp.shared.infrastructure.services.interfaces.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {
    private final SearchUserByNameUC searchUserByNameUC;
    private final SearchUserByEmailUC searchUserByEmailUC;
    private final SearchEventByNameUC searchEventByNameUC;
    private final SearchMyEventByNameUC searchMyEventByNameUC;
    private final SearchCategoriesUC searchCategoriesUC;
    private final SearchStaffRolesUC searchStaffRolesUC;
    private final UserDtoCreator userDtoCreator;
    private final EventDtoCreator eventDtoCreator;
    @Override
    public List<UserResponseDto> searchUsersByName(String name, int size) {
        return searchUserByNameUC.searchByName(name, size)
                .stream()
                .map(userDtoCreator::create)
                .toList();
    }

    @Override
    public List<UserResponseDto> searchUsersByEmail(String email, int size) {
        return searchUserByEmailUC.searchByEmail(email, size)
                .stream()
                .map(userDtoCreator::create)
                .toList();
    }

    @Override
    public List<EventResponseDto> searchEventsByName(String name, int size) {
        return searchEventByNameUC.searchByName(name, size)
                .stream()
                .map(eventDtoCreator::create)
                .toList();
    }

    @Override
    public List<EventResponseDto> searchMyEventsByName(String name, String creatorId, int size) {
        return searchMyEventByNameUC.searchByName(name, creatorId, size)
                .stream()
                .map(eventDtoCreator::create)
                .toList();
    }

    @Override
    public List<CategoriesResponseDto> searchCategoriesByName(String name, int size) {
        return searchCategoriesUC.search(name, size)
                .stream()
                .map(CategoriesResponseDto::new)
                .toList();
    }

    @Override
    public List<StaffRolesResponseDto> searchStaffRolesByName(String name, int size) {
        return searchStaffRolesUC.search(name, size)
                .stream()
                .map(StaffRolesResponseDto::new)
                .toList();
    }
}
