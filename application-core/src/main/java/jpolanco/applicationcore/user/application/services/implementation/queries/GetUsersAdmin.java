package jpolanco.applicationcore.user.application.services.implementation.queries;

import jpolanco.applicationcore.shared.application.pageable.Cursored;
import jpolanco.applicationcore.shared.application.pageable.Paged;
import jpolanco.applicationcore.shared.infrastructure.utils.PageableRequest;
import jpolanco.applicationcore.user.application.mappers.UserDto;
import jpolanco.applicationcore.user.application.ports.output.UserPageableRepository;
import jpolanco.applicationcore.user.application.services.interfaces.queries.GetUsersByAdmin;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.AdminUserFilterRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUsersAdmin implements GetUsersByAdmin {

    private final UserPageableRepository userPageableRepository;

    @Override
    public Paged<UserResponse> getByPages(PageableRequest request, AdminUserFilterRequest filters) {
        return userPageableRepository.findAll(request, filters)
                .map(UserDto::toResponse);
    }

    @Override
    public Cursored<UserResponse> getByCursor(UUID cursor, PageableRequest request, AdminUserFilterRequest filters) {
        return userPageableRepository.findAllCursored(cursor, request, filters)
                .map(UserDto::toResponse);
    }
}
