package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.user.domain.model.User;

import java.util.List;

public interface SearchUserService {
    List<User> searchByName(String name, int size);
    List<User> searchByEmail(String email, int size);
}
