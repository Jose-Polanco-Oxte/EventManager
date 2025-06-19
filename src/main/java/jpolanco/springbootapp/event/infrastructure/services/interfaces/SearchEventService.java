package jpolanco.springbootapp.event.infrastructure.services.interfaces;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.CategoriesResponse;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponse;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.StaffRolesResponse;

import java.util.List;

public interface SearchEventService {
    List<EventResponse> searchEventsByName(String name, int size);
    List<EventResponse> searchMyEventsByName(String name, String creatorId, int size);
    List<CategoriesResponse> searchCategoriesByName(String name, int size);
    List<StaffRolesResponse> searchStaffRolesByName(String name, int size);
    List<EventResponse> searchPublicEventsByName(String name, int size);
    List<EventResponse> searchPublicEventsByCategory(String category, int size);
}