package jpolanco.applicationcore.user.infrastructure.adapters.output.mysql;

import io.github.josepolanco.filterable.api.FilterableApi;
import jpolanco.applicationcore.shared.application.pageable.Cursored;
import jpolanco.applicationcore.shared.application.pageable.PageMapper;
import jpolanco.applicationcore.shared.application.pageable.Paged;
import jpolanco.applicationcore.shared.infrastructure.context.PageAux;
import jpolanco.applicationcore.shared.infrastructure.utils.PageableRequest;
import jpolanco.applicationcore.user.application.ports.output.UserPageableRepository;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.AdminUserFilterRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.DefaultUserFilterRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.mappers.UserMapper;
import jpolanco.applicationcore.user.infrastructure.adapters.output.criteria.SpecBuilder;
import jpolanco.applicationcore.user.infrastructure.adapters.output.criteria.UserSpecs;
import jpolanco.applicationcore.user.infrastructure.adapters.output.repositories.JpaUserRepository;
import jpolanco.domainmodel.user.RoleEntity_;
import jpolanco.domainmodel.user.UserEntity;
import jpolanco.domainmodel.user.UserEntity_;
import jpolanco.domainmodel.user.UserStatusE;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static io.github.josepolanco.filterable.spring.Wrapper.from;

@Repository
@RequiredArgsConstructor
public class UserPageableRepositoryMySQL implements UserPageableRepository {

    private final JpaUserRepository jpaUserRepository;

    private Cursored<User> getUserCursored(UUID cursor, org.springframework.data.domain.Pageable pageable, Specification<UserEntity> exclusiveSpecs) {
        if (cursor != null) {
            Optional<Long> id = jpaUserRepository.findIdByUuid(cursor);
            if (id.isEmpty()) {
                return Cursored.empty();
            }
            Long cursorId = id.get();
            exclusiveSpecs = exclusiveSpecs.and(SpecBuilder.<UserEntity>init().greaterThan(UserEntity_.id, cursorId).build());
            return PageMapper.toCursor(
                    jpaUserRepository.findAll(exclusiveSpecs, pageable).map(UserMapper::fromPersistence)
            );
        } else {
            return PageMapper.toCursor(
                    jpaUserRepository.findAll(exclusiveSpecs, pageable)
            ).map(UserMapper::fromPersistence);
        }
    }

    private Specification<UserEntity> buildDefaultFilters(DefaultUserFilterRequest filter) {
        return from(FilterableApi.<UserEntity>create()
                .<Instant>comparable().configure()
                .filter(filter.creation(), UserEntity_.createdAt)
                .filterIn(filter.creationList(), UserEntity_.createdAt)
                .let().text().configure()
                .filter(filter.name(), UserEntity_.fullNameLower)
                .filter(filter.email(), UserEntity_.email)
                .filterIn(filter.nameList(), UserEntity_.fullNameLower)
                .filterIn(filter.emailList(), UserEntity_.email)
                .let().relational().join(UserEntity_.roles).buildPath()
                .text().configure(cfg -> cfg.disableStartsWith().disableEndsWith())
                .filterIn(filter.roleList(), RoleEntity_.name)
                .let().backToFilterableApi().build());
    }

    @Override
    public Paged<User> findAllLimited(PageableRequest pageableRequestFilters, DefaultUserFilterRequest filterable) {
        org.springframework.data.domain.Pageable pageable = PageAux.resolvePageRequest(pageableRequestFilters);
        Specification<UserEntity> filters = buildDefaultFilters(filterable);
        Specification<UserEntity> exclusiveSpecs = SpecBuilder.<UserEntity>init()
                .and(UserSpecs.userQueryFilter())
                .and(filters)
                .build();
        return PageMapper.from(
                jpaUserRepository.findAll(exclusiveSpecs, pageable)
                        .map(UserMapper::fromPersistence)
        );
    }

    @Override
    public Cursored<User> findAllCursoredLimited(UUID cursor, PageableRequest pageableRequestFilters, DefaultUserFilterRequest filterable) {
        org.springframework.data.domain.Pageable pageable = PageAux.resolvePageRequest(pageableRequestFilters);
        Specification<UserEntity> filters = buildDefaultFilters(filterable);
        Specification<UserEntity> exclusiveSpecs = SpecBuilder.<UserEntity>init()
                .and(UserSpecs.userQueryFilter())
                .and(filters)
                .build();
        return getUserCursored(cursor, pageable, exclusiveSpecs);
    }

    private Specification<UserEntity> buildAdminFilters(AdminUserFilterRequest filter) {
        return from(FilterableApi.<UserEntity>create()
                .<Instant>comparable().configure()
                .filter(filter.creation(), UserEntity_.createdAt)
                .filterIn(filter.creationList(), UserEntity_.createdAt)
                .let().<UserStatusE>comparable().configure()
                .filter(filter.status(), UserEntity_.status)
                .filterIn(filter.statusList(), UserEntity_.status)
                .let().<Boolean>comparable().configure()
                .custom((root, criteriaQuery, criteriaBuilder) ->
                        filter.deleted().map(del -> criteriaBuilder.equal(root.get(UserEntity_.deleted), del))
                                .orElse(criteriaBuilder.conjunction()))
                .let().text().configure()
                .filter(filter.name(), UserEntity_.fullNameLower)
                .filter(filter.email(), UserEntity_.email)
                .filterIn(filter.nameList(), UserEntity_.fullNameLower)
                .filterIn(filter.emailList(), UserEntity_.email)
                .let().relational().join(UserEntity_.roles).buildPath()
                .text().configure(cfg -> cfg.disableStartsWith().disableEndsWith())
                .filter(filter.role(), RoleEntity_.name)
                .filterIn(filter.roleList(), RoleEntity_.name)
                .let().backToFilterableApi().build()
        );
    }

    @Override
    public Paged<User> findAll(PageableRequest pageableRequestFilters, AdminUserFilterRequest filter) {
        org.springframework.data.domain.Pageable pageable = PageAux.resolvePageRequest(pageableRequestFilters);
        Specification<UserEntity> filters = buildAdminFilters(filter);
        return PageMapper.from(
                jpaUserRepository.findAll(filters, pageable)
                        .map(UserMapper::fromPersistence)
        );
    }

    @Override
    public Cursored<User> findAllCursored(UUID cursor, PageableRequest pageableRequestFilters, AdminUserFilterRequest filter) {
        org.springframework.data.domain.Pageable pageable = PageAux.resolvePageRequest(pageableRequestFilters);
        Specification<UserEntity> filters = buildAdminFilters(filter);
        return getUserCursored(cursor, pageable, filters);
    }
}
