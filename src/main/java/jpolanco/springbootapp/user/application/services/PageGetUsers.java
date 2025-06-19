package jpolanco.springbootapp.user.application.services;

import jpolanco.springbootapp.shared.utils.PageResult;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.PageGetUsersUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PageGetUsers implements PageGetUsersUC {
    private final UserRepository userRepository;
    @Override
    public PageResult<User> get(int page, int size, String sortBy, String sortOrder) {
        return userRepository.getUsers(page, size, sortBy, sortOrder);
    }
}
