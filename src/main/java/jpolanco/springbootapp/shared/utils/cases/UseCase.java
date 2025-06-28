package jpolanco.springbootapp.shared.utils.cases;

public interface UseCase<P, R> {
    /**
     * Executes the use case with the provided parameters.
     *
     * @param request the parameters required to execute the use case
     * @return a Result containing the output of the use case or an error if it fails
     */
    R execute(P request);
}
