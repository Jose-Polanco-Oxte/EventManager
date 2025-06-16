package jpolanco.springbootapp.shared.infrastructure.services.interfaces;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.CategoriesResponseDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponseDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.StaffRolesResponseDto;
import jpolanco.springbootapp.shared.application.Dto;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserResponseDto;

import java.util.List;

public interface SearchService {

    List<UserResponseDto> searchUsersByName(String name, int size);

    List<UserResponseDto> searchUsersByEmail(String email, int size);

    List<EventResponseDto> searchEventsByName(String name, int size);

    List<EventResponseDto> searchMyEventsByName(String name, String creatorId, int size);

    List<CategoriesResponseDto> searchCategoriesByName(String name, int size);

    List<StaffRolesResponseDto> searchStaffRolesByName(String name, int size);
}