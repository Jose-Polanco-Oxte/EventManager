package jpolanco.springbootapp.shared.infrastructure.mappers;

import jpolanco.springbootapp.user.domain.model.value_objects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserThread extends ThreadLocalAggregateContext<User, UserEntity> {
}
