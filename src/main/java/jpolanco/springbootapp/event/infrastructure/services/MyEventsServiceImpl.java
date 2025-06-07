package jpolanco.springbootapp.event.infrastructure.services;

import jpolanco.springbootapp.event.application.uc.DeleteMyEventByIdUC;
import jpolanco.springbootapp.event.application.uc.GetMyEventsUC;
import jpolanco.springbootapp.event.application.uc.UpdateMyEventUC;
import jpolanco.springbootapp.event.domain.model.Modality;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventDto;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto.EventDtoCreator;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.MyEventsService;
import jpolanco.springbootapp.shared.application.Dto;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MyEventsServiceImpl implements MyEventsService {

    private final DeleteMyEventByIdUC deleteMyEventByIdUC;
    private final GetMyEventsUC getMyEventsUC;
    private final UpdateMyEventUC updateMyEventUC;
    private final EventDtoCreator eventDtoCreator;
    private final DomainEventsPublisher publisher;

    @Override
    @Transactional
    public Result<Void> deleteMyEvent(String userId, String eventId) {
        var result = deleteMyEventByIdUC.delete(userId, eventId);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success();
    }

    @Override
    @Transactional
    public Result<Dto> updateMyEvent(String userId, String eventId, String imageFileName, UpdateEventDto eventDto) {
        var eventUpdated = updateMyEventUC.setChanges(eventId, userId)
                .title(eventDto.title())
                .description(eventDto.description())
                .schedule(eventDto.schedule())
                .duration(eventDto.durationInSeconds())
                .location(
                        eventDto.locationName(),
                        eventDto.locationCity(),
                        eventDto.locationCountry(),
                        eventDto.latitude(),
                        eventDto.longitude()
                )
                .categories(eventDto.categories())
                .isPublic(eventDto.isPublic())
                .enableComments(eventDto.enableComments())
                .modality(Modality.create(eventDto.modality()))
                .staff(eventDto.staffs())
                .pictureFileName(imageFileName)
                .setMaxAttendees(eventDto.maxAttendees())
                .update();
        if (eventUpdated.isFailure()) {
            return Result.failure(eventUpdated.getError());
        }
        var event = eventUpdated.getValue();
        event.pullEvents().forEach(publisher::publish);
        return Result.success(eventDtoCreator.create(event));
    }

    @Override
@Transactional
    public Result<Dto> updateMyEventClearStaff(String userId, String eventId, String imageFileName, UpdateEventDto eventDto) {
        var eventUpdated = updateMyEventUC.setChanges(eventId, userId)
                .title(eventDto.title())
                .description(eventDto.description())
                .schedule(eventDto.schedule())
                .duration(eventDto.durationInSeconds())
                .location(
                        eventDto.locationName(),
                        eventDto.locationCity(),
                        eventDto.locationCountry(),
                        eventDto.latitude(),
                        eventDto.longitude()
                )
                .categories(eventDto.categories())
                .isPublic(eventDto.isPublic())
                .enableComments(eventDto.enableComments())
                .modality(Modality.create(eventDto.modality()))
                .clearStaff()
                .pictureFileName(imageFileName)
                .setMaxAttendees(eventDto.maxAttendees())
                .update();
        if (eventUpdated.isFailure()) {
            return Result.failure(eventUpdated.getError());
        }
        var event = eventUpdated.getValue();
        event.pullEvents().forEach(publisher::publish);
        return Result.success(eventDtoCreator.create(event));
    }

    @Override
@Transactional
    public Result<Dto> updateMyEventAddStaff(String userId, String eventId, String imageFileName, UpdateEventDto eventDto) {
        var eventUpdated = updateMyEventUC.setChanges(eventId, userId)
                .title(eventDto.title())
                .description(eventDto.description())
                .schedule(eventDto.schedule())
                .duration(eventDto.durationInSeconds())
                .location(
                        eventDto.locationName(),
                        eventDto.locationCity(),
                        eventDto.locationCountry(),
                        eventDto.latitude(),
                        eventDto.longitude()
                )
                .categories(eventDto.categories())
                .isPublic(eventDto.isPublic())
                .enableComments(eventDto.enableComments())
                .modality(Modality.create(eventDto.modality()))
                .addStaff(eventDto.staffs())
                .pictureFileName(imageFileName)
                .setMaxAttendees(eventDto.maxAttendees())
                .update();
        if (eventUpdated.isFailure()) {
            return Result.failure(eventUpdated.getError());
        }
        var event = eventUpdated.getValue();
        event.pullEvents().forEach(publisher::publish);
        return Result.success(eventDtoCreator.create(event));
    }

    @Override
    public Result<List<Dto>> getMyEvents(String userId, int page, int size) {
        var result = getMyEventsUC.getMyEvents(userId);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(
                result.getValue().stream()
                        .map(eventDtoCreator::create)
                        .toList()
        );
    }

    @Override
    public Result<List<Dto>> getMyEventsByCategory(String userId, String category, String modality, int page, int size) {
        return null;
    }

    @Override
    public Result<List<Dto>> getMyEventsByTitle(String userId, String title, String modality, int page, int size) {
        return null;
    }

    @Override
    public Result<List<Dto>> getMyEventsByLocation(String userId, String locationName, String modality, int page, int size) {
        return null;
    }
}
