package jpolanco.applicationcore.user.application.services.implementation.queries;

import jpolanco.applicationcore.user.application.mappers.UserDto;
import jpolanco.applicationcore.user.application.ports.output.UserQueryRepository;
import jpolanco.applicationcore.user.application.services.interfaces.queries.SearchUserService;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.SearchQueryRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchUserServiceAdmin implements SearchUserService {

    private final UserQueryRepository userQueryRepository;

    @Override
    public List<UserResponse> searchByEmail(SearchQueryRequest request) {
        return userQueryRepository.searchByEmail(request.query(), request.size().orElse(10))
                .stream()
                .map(UserDto::toResponse)
                .toList();
    }

    @Override
    public List<UserResponse> searchByName(SearchQueryRequest request) {
        return userQueryRepository.searchByName(request.query(), request.size().orElse(10))
                .stream()
                .map(UserDto::toResponse)
                .toList();
    }
}
