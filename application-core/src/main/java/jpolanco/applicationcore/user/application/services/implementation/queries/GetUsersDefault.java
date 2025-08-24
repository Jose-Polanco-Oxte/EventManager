package jpolanco.applicationcore.user.application.services.implementation.queries;


import jpolanco.applicationcore.shared.application.pageable.Cursored;
import jpolanco.applicationcore.shared.application.pageable.Paged;
import jpolanco.applicationcore.shared.infrastructure.utils.PageableRequest;
import jpolanco.applicationcore.user.application.mappers.UserDto;
import jpolanco.applicationcore.user.application.ports.output.UserPageableRepository;
import jpolanco.applicationcore.user.application.services.interfaces.queries.GetUsers;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.DefaultUserFilterRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUsersDefault implements GetUsers {

    private final UserPageableRepository userPageableRepository;

    @Override
    public Paged<UserResponse> getByPages(PageableRequest request, DefaultUserFilterRequest filters) {
        return userPageableRepository.findAllLimited(request, filters)
                .map(UserDto::toResponse);
    }

    @Override
    public Cursored<UserResponse> getByCursor(UUID cursor, PageableRequest request, DefaultUserFilterRequest filters) {
        return userPageableRepository.findAllCursoredLimited(cursor, request, filters)
                .map(UserDto::toResponse);
    }
}
