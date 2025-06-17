package jpolanco.springbootapp.shared.infrastructure.dto;

public interface DtoCreator<P, D extends Dto> {
    D create(P payload);
}
