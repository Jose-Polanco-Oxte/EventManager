package jpolanco.springbootapp.shared.infrastructure.dto.response;

import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;

public record SimpleResponse(String message) implements Dto {
}
