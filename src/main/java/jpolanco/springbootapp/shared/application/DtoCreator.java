package jpolanco.springbootapp.shared.application;

public interface DtoCreator<P, D extends Dto> {
    D create(P payload);
}
