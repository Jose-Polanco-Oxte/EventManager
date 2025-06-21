package jpolanco.springbootapp.user.application.services.unique;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.shared.utils.PageResult;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.unique.GetUsersUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUsers implements GetUsersUC {
    private final UserQueryRepository queryRepository;

    @Override
    public PageResult<User> get(PagePaginationRequest request) {
        return queryRepository.findAll(
                request.page(),
                request.size(),
                request.sortBy(),
                request.orderBy()
        );
    }

    @Override
    public CursorPageResult<User, String> get(CursorPaginationRequest<String> request) {
        return queryRepository.findAll(
                request.cursor(),
                request.size(),
                request.sortBy(),
                request.orderBy()
        );
    }
}
