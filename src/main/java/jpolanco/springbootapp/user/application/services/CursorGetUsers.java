package jpolanco.springbootapp.user.application.services;

import jpolanco.springbootapp.shared.application.utils.CursorPageResult;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.CursorGetUsersUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CursorGetUsers implements CursorGetUsersUC {
    private final UserRepository userRepository;
    @Override
    public CursorPageResult<User, String> get(String cursor, int size, String sortBy, String sortOrder) {
        return userRepository.getUsers(cursor, size, sortBy, sortOrder);
    }
}
