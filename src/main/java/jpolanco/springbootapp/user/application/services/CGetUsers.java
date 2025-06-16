package jpolanco.springbootapp.user.application.services;

import jpolanco.springbootapp.shared.application.CursorPageResult;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.CGetUsersUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CGetUsers implements CGetUsersUC {
    private final UserRepository userRepository;
    @Override
    public CursorPageResult<User, String> getUsers(String cursor, int size, String sortBy, String sortOrder) {
        return userRepository.getUsers(cursor, size, sortBy, sortOrder);
    }
}
