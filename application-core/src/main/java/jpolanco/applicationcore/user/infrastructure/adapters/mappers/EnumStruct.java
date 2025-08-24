package jpolanco.applicationcore.user.infrastructure.adapters.mappers;

import jpolanco.applicationcore.user.domain.model.UserStatus;
import jpolanco.domainmodel.user.UserStatusE;

public class EnumStruct {

    public static UserStatusE map(UserStatus userStatus) {
        if (userStatus == null) {
            return null;
        }
        return UserStatusE.fromString(userStatus.getValue());
    }

    public static UserStatus map(UserStatusE userStatusE) {
        if (userStatusE == null) {
            return null;
        }
        return UserStatus.fromString(userStatusE.getValue());
    }
}
