package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.List;

public interface SearchUserService {
    List<UserResponse> searchByName(String name, int size);
    List<UserResponse> searchByEmail(String email, int size);
}
