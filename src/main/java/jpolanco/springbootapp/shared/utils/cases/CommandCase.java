package jpolanco.springbootapp.shared.utils.cases;

import jpolanco.springbootapp.shared.domain.Result;

public interface CommandCase<P, E> extends UseCase<P, Result<E>> {
}