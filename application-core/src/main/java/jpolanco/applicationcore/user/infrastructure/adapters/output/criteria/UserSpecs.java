package jpolanco.applicationcore.user.infrastructure.adapters.output.criteria;

import jpolanco.domainmodel.user.UserEntity;
import jpolanco.domainmodel.user.UserEntity_;
import jpolanco.domainmodel.user.UserStatusE;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecs {

    public static Specification<UserEntity> isNotDeleted() {
        return SpecBuilder.<UserEntity>init().isFalse(UserEntity_.deleted).build();
    }

    public static Specification<UserEntity> isActive() {
        return SpecBuilder.<UserEntity>init().eq(UserEntity_.status, UserStatusE.ACTIVE).build();
    }

    public static Specification<UserEntity> userQueryFilter() {
        return SpecBuilder.<UserEntity>init().and(isNotDeleted()).and(isActive()).build();
    }
}