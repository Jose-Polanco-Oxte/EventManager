package jpolanco.springbootapp.event.infrastructure.adapters.input.controllers;

import jakarta.validation.constraints.Min;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.SearchEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/events/search")
public class SearchEventController {

    private final SearchEventService searchEventService;

    @GetMapping("/categories")
    public ResponseEntity<Object> searchCategoriesByName(
            @RequestParam String query,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size
    ) {
        var categories = searchEventService.searchCategoriesByName(query, size);
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/staff-roles")
    public ResponseEntity<Object> searchStaffRolesByName(
            @RequestParam String query,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size
    ) {
        var roles = searchEventService.searchStaffRolesByName(query, size);
        if (roles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roles);
    }
}
