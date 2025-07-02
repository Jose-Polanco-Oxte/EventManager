package jpolanco.springbootapp.user.application.services.unique;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.shared.application.CursorPageResult;
import jpolanco.springbootapp.shared.application.PageResult;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.unique.GetUsersUC;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUsers implements GetUsersUC {
    private final UserQueryRepository queryRepository;

    @Override
    public PageResult<User> getByPages(PagePaginationRequest request) {
        return queryRepository.findAll(
                request.page(),
                request.size(),
                request.sortBy(),
                request.orderBy()
        );
    }

    @Override
    public CursorPageResult<User, UUID> getByCursor(CursorPaginationRequest<UUID> request) {
        return queryRepository.findAll(
                request.cursor(),
                request.size(),
                request.sortBy(),
                request.orderBy()
        );
    }
}
