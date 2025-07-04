package jpolanco.springbootapp.shared.utils.cases;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;

import java.util.List;

public interface NotificationCase<P> extends UseCase<P, Result<List<EventNotification>>> {
}
