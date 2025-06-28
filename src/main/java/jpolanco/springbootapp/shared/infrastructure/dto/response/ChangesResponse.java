package jpolanco.springbootapp.shared.infrastructure.dto.response;

import jpolanco.springbootapp.event.application.utils.Changes;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;

import java.util.List;

public record ChangesResponse (
        String message,
        List<Changes> changes
) implements Dto
{}
