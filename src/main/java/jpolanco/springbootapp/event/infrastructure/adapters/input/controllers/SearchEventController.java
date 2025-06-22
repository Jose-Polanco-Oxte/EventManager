package jpolanco.springbootapp.event.infrastructure.adapters.input.controllers;

import jakarta.validation.constraints.Min;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.SearchEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/name")
    public ResponseEntity<Object> searchEventsByName(
            @RequestParam String query,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size
    ) {
        var result = searchEventService.searchEventsByName(query, size);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    @GetMapping("/categories")
    public ResponseEntity<Object> searchCategoriesByName(
            @RequestParam String query,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size
    ) {
        var result = searchEventService.searchCategoriesByName(query, size);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    @GetMapping("/staff-roles")
    public ResponseEntity<Object> searchStaffRolesByName(
            @RequestParam String query,
            @Min(1) @RequestParam(defaultValue = "10", required = false) int size
    ) {
        var result = searchEventService.searchStaffRolesByName(query, size);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }
}
