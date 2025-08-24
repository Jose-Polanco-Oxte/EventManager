package jpolanco.applicationcore.user.infrastructure.adapters.mappers;

import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.domain.model.UserRoles;
import jpolanco.applicationcore.user.infrastructure.exceptions.UserDataConflictException;
import jpolanco.domainmodel.user.RoleEntity;
import jpolanco.domainmodel.user.UserEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserEntity fromDomain(User user, Set<RoleEntity> roles) {
        return new UserEntity(
                user.getUserId().getId(),
                user.getUserId().getUUID(),
                user.getName().getFirstName(),
                user.getName().getLastName(),
                user.getEmail().getValue(),
                user.getEncodedPassword().getValue(),
                roles,
                EnumStruct.map(user.getStatus()),
                user.getCreatedAt(),
                user.getQR()
        );
    }

    public static User fromPersistence(UserEntity userEntity) {
        return User.fromPersisted(
                userEntity.getId(),
                userEntity.getUuid(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRoles()
                        .stream()
                        .map(r -> UserRoles.fromString(r.getName()).orElseThrow(
                                () -> new UserDataConflictException("Role not found: " + r.getName())
                        ))
                        .collect(Collectors.toSet()),
                EnumStruct.map(userEntity.getStatus()),
                userEntity.getCreatedAt()
        );
    }
}
