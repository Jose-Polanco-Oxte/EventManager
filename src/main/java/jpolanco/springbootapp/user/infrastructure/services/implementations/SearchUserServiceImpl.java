package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.user.application.uc.unique.search.SearchUserByEmailUC;
import jpolanco.springbootapp.user.application.uc.unique.search.SearchUserByNameUC;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponse;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.UserDtoCreator;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.SearchUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchUserServiceImpl implements SearchUserService {
    private final SearchUserByEmailUC searchUserByEmailUC;
    private final SearchUserByNameUC searchUserByNameUC;
    private final UserDtoCreator userDtoCreator;

    @Override
    public List<UserResponse> searchByName(String name, int size) {
        return searchUserByNameUC.search(name, size)
                .stream()
                .map(userDtoCreator::create)
                .toList();
    }

    @Override
    public List<UserResponse> searchByEmail(String email, int size) {
        return searchUserByEmailUC.search(email, size)
                .stream()
                .map(userDtoCreator::create)
                .toList();
    }
}
