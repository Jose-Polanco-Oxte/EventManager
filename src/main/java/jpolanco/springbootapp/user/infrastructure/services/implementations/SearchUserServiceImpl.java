package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.user.application.uc.SearchUserByEmailUC;
import jpolanco.springbootapp.user.application.uc.SearchUserByNameUC;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponseDto;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.UserDtoCreator;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.SearchUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchUserServiceImpl implements SearchUserService {
    private final SearchUserByEmailUC searchUserByEmailUC;
    private final SearchUserByNameUC searchUserByNameUC;
    private final UserDtoCreator userDtoCreator;

    @Override
    public List<UserResponseDto> searchUsersByName(String name, int size) {
        return searchUserByNameUC.search(name, size)
                .stream()
                .map(userDtoCreator::create)
                .toList();
    }

    @Override
    public List<UserResponseDto> searchUsersByEmail(String email, int size) {
        return searchUserByEmailUC.search(email, size)
                .stream()
                .map(userDtoCreator::create)
                .toList();
    }
}
