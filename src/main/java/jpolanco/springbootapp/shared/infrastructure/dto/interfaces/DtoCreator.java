package jpolanco.springbootapp.shared.infrastructure.dto.interfaces;

public interface DtoCreator<P, D extends Dto> {
    D create(P payload);
}