package jpolanco.springbootapp.user.infrastructure.adapters.output.context;

import jpolanco.springbootapp.shared.infrastructure.context.ThreadLocalAggregateContext;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserContext extends ThreadLocalAggregateContext<User, UserEntity> {
}
