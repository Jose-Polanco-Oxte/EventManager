package jpolanco.springbootapp.shared.infrastructure.controllers;

import jpolanco.springbootapp.config.auth.MyUserDetails;
import jpolanco.springbootapp.shared.infrastructure.services.interfaces.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/users/by-name")
    public ResponseEntity<Object> searchByName(
            @RequestParam() String name,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        size = Math.max(size, 1);
        var users = searchService.searchUsersByName(name, size);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/by-email")
    public ResponseEntity<Object> searchByEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        size = Math.max(size, 1);
        var users = searchService.searchUsersByEmail(email, size);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/events/by-name")
    public ResponseEntity<Object> searchEventsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        size = Math.max(size, 1);
        var events = searchService.searchEventsByName(name, size);
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("/my-events/by-name")
    public ResponseEntity<Object> searchMyEventsByName(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @RequestParam String name,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        size = Math.max(size, 1);
        var myEvents = searchService.searchMyEventsByName(name, myUserDetails.getId(), size);
        if (myEvents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(myEvents);
    }

    @GetMapping("/categories")
    public ResponseEntity<Object> searchCategoriesByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        size = Math.max(size, 1);
        var maybeCategories = searchService.searchCategoriesByName(name, size);
        if (maybeCategories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(maybeCategories);
    }

    @GetMapping("/staff-roles")
    public ResponseEntity<Object> searchStaffRolesByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        size = Math.max(size, 1);
        var maybeStaffRoles = searchService.searchStaffRolesByName(name, size);
        if (maybeStaffRoles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(maybeStaffRoles);
    }
}
