package jpolanco.springbootapp.unit.infrastructure.http;

import jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto.EventDtoCreator;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.UserDtoCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

@SuppressWarnings("unchecked")
public class HandleResponseTest {
    private UserDtoCreator userDtoCreator;
    private EventDtoCreator eventDtoCreator;

    private Result<User> userResult;


    @Nested
    @DisplayName("Optional to Response Tests")
    class OptionalToResponseTests {

    }
}
