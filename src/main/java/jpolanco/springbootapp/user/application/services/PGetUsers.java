package jpolanco.springbootapp.user.application.services;

import jpolanco.springbootapp.shared.application.PageResult;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.PGetUsersUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PGetUsers implements PGetUsersUC {
    private final UserRepository userRepository;
    @Override
    public PageResult<User> getUsers(int page, int size, String sortBy, String sortOrder) {
        return userRepository.getUsers(page, size, sortBy, sortOrder);
    }
}
