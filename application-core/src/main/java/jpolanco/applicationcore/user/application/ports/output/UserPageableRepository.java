package jpolanco.applicationcore.user.application.ports.output;

import jpolanco.applicationcore.shared.application.pageable.Cursored;
import jpolanco.applicationcore.shared.application.pageable.Paged;
import jpolanco.applicationcore.shared.infrastructure.utils.PageableRequest;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.AdminUserFilterRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.DefaultUserFilterRequest;

import java.util.UUID;

/**
 * Port interface for pageable user repository operations.
 * This interface defines methods for retrieving paged and cursored user entities with filtering options.
 */
public interface UserPageableRepository {
    Paged<User> findAllLimited(PageableRequest pageableRequest, DefaultUserFilterRequest filterable);

    Cursored<User> findAllCursoredLimited(UUID cursor, PageableRequest pageableRequest, DefaultUserFilterRequest filterable);

    Paged<User> findAll(PageableRequest pageableRequest, AdminUserFilterRequest filter);

    Cursored<User> findAllCursored(UUID cursor, PageableRequest pageableRequest, AdminUserFilterRequest filter);
}