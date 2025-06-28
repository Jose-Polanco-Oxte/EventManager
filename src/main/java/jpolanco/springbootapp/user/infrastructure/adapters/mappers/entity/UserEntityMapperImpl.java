package jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.shared.domain.utils.Error;
import jpolanco.springbootapp.shared.utils.Either;
import jpolanco.springbootapp.user.domain.model.User;

import jpolanco.springbootapp.user.domain.model.value_objects.UserStatus;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.RoleEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import jpolanco.springbootapp.user.infrastructure.errors.UserIntegrity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserEntityMapperImpl implements UserEntityMapper {
    public User toDomain(UserEntity userEntity) {
        Either<User, List<Error>> result = User.load(
                userEntity.getId().toString(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRoles()
                        .stream()
                        .map(RoleEntity::getName)
                        .toList(),
                userEntity.getStatus(),
                userEntity.getQrFileName(),
                userEntity.getCreatedAt()
        );
        if (result.isRight()) {
            throw new UserIntegrity(
                    "User integrity error: " + result.getRight().stream()
                            .map(Error::getMessage)
                            .collect(Collectors.joining(", "))
            , 409);
        }
        return result.getLeft();
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                UUID.fromString(user.getId()),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getEncodedPassword(),
                user.getRoles()
                        .stream()
                        .map(RoleEntity::new)
                        .collect(Collectors.toSet()),
                UserStatus.valueOf(user.getStatus().getValue()),
                user.getCreatedAt(),
                user.getQRFileName()
        );
    }
}
