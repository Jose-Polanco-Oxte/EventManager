package jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request;

import jpolanco.springbootapp.event.application.ports.input.request.StaffRequest;

import java.util.List;

public record StaffChangeRequest(
        List<StaffRequest> add,
        List<String> remove,
        boolean clear
) {
}
