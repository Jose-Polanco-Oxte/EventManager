package jpolanco.springbootapp.shared.utils.cases;

public interface BaseCase<E, P, R> {
    /**
     * Executes the case with the provided entity and parameters.
     *
     * @param entity the entity required to execute the case
     * @param request the parameters required to execute the case
     * @return a result containing the output of the case or an error if it fails
     */
    R execute(E entity, P request);
}
