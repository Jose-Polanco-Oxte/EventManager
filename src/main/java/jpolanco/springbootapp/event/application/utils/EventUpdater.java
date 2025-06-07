package jpolanco.springbootapp.event.application.utils;

import jpolanco.springbootapp.event.application.ports.input.StaffHolder;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.domain.model.EventStatus;
import jpolanco.springbootapp.event.domain.model.Modality;
import jpolanco.springbootapp.event.domain.model.domainevents.EventUpdate;
import jpolanco.springbootapp.shared.domain.DomainEvent;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class EventUpdater {

    private final Event event;
    private final EventValidation eventValidation;
    private Result<?> result = Result.success();

    private void check(Result<?> result) {
        if (result.isFailure() && this.result.isSuccess()) {
            this.result = result;
        }
    }

    private int nullable(String value) {
        if (value == null) {
            return 1;
        }
        if (value.isBlank()) {
            return 2;
        }
        return 0;
    }

    public EventUpdater title(String title) {
        if (nullable(title) > 0) return this;
        if (!event.getTitle().equals(title)) {
            var result = event.changeTitle(title);
            check(result);
        }
        return this;
    }

    public EventUpdater description(String description) {
        if (nullable(description) > 0) return this;
        if (!event.getDescription().equals(description)) {
            var result = event.changeDescription(description);
            check(result);
        }
        return this;
    }

    public EventUpdater schedule(String schedule) {
        if (nullable(schedule) > 0) return this;
        var date = Instant.parse(schedule);
        if (!event.getSchedule().equals(date)) {
            var result = eventValidation.validate(event.getCreatorId(), date, event.getDurationInSeconds(), event.getLatitude(), event.getLongitude());
            check(result);
            if (result.isSuccess()) {
                var changeResult = event.changeSchedule(schedule);
                check(changeResult);
            }
        }
        return this;
    }

    public EventUpdater duration(long durationInSeconds) {
        if (durationInSeconds <= 0) return this;
        if (event.getDurationInSeconds() != durationInSeconds) {
            var result = eventValidation.validate(event.getCreatorId(), event.getSchedule(), durationInSeconds, event.getLatitude(), event.getLongitude());
            check(result);
            if (result.isSuccess()) {
                var changeResult = event.changeDuration(durationInSeconds);
                check(changeResult);
            }
        }
        return this;
    }

    public EventUpdater status(EventStatus status) {
        if (status == null || event.getStatus() == status) return this;
        event.changeStatus(status);
        return this;
    }

    public EventUpdater location(String locationName, String locationCity, String locationCountry, double latitude, double longitude) {
        if (latitude == event.getLatitude() && longitude == event.getLongitude()) {
            // If latitude and longitude are the same, we can skip validation
            if (event.getLatitude() == latitude && event.getLongitude() == longitude) {
                return this;
            }
        } else {
            var result = eventValidation.validate(event.getCreatorId(), event.getSchedule(), event.getDurationInSeconds(), latitude, longitude);
            check(result);
            if (result.isFailure()) {
                return this; // Validation failed, do not proceed with location change
            }
        }
        var result = event.changeLocation(
                latitude,
                longitude,
                nullable(locationName) > 0 ? event.getLocationName() : locationName,
                nullable(locationCity) > 0 ? event.getLocationCity() : locationCity,
                nullable(locationCountry) > 0 ? event.getLocationCountry() : locationCountry);
        check(result);
        return this;
    }

    public EventUpdater categories(List<String> categories) {
        if (categories == null || categories.isEmpty()) return this;
        var result = event.changeCategories(categories);
        check(result);
        return this;
    }

    public EventUpdater addCategories(List<String> categories) {
        if (categories == null || categories.isEmpty()) return this;
        var result = event.addCategories(categories);
        check(result);
        return this;
    }

    public EventUpdater isPublic(boolean isPublic) {
        if (event.isPublic() == isPublic) return this;
        if (isPublic) {event.makePublic();} else { event.makePrivate();}
        return this;
    }

    public EventUpdater enableComments(boolean enableComments) {
        if (event.isEnableComments() == enableComments) return this;
        if (enableComments) {event.enableComments();} else {event.disableComments();}
        return this;
    }

    public EventUpdater modality(Modality modality) {
        if (modality == null || modality.equals(event.getModality())) return this;
        if (!event.getModality().equals(modality)) {
            var result = event.changeModality(modality);
            check(result);
        }
        return this;
    }

    public EventUpdater staff(List<StaffHolder> staffs) {
        if (staffs == null || staffs.isEmpty()) return this;
        event.setStaff(staffs);
        return this;
    }

    public EventUpdater addStaff(List<StaffHolder> staffs) {
        if (staffs == null || staffs.isEmpty()) return this;
        for (var staff : staffs) {
            var result = event.addStaff(staff.staffId(), staff.role(), staff.isAssistanceClerk());
            check(result);
        }
        return this;
    }

    public EventUpdater removeStaff(List<String> staffIds) {
        if (staffIds == null || staffIds.isEmpty()) return this;
        for (String staffId : staffIds) {
            var result = event.removeStaff(staffId);
            check(result);
        }
        return this;
    }

    public EventUpdater clearStaff() {
        var result = event.clearStaff();
        check(result);
        return this;
    }

    public EventUpdater pictureFileName(String pictureFileName) {
        if (nullable(pictureFileName) > 0) return this;
        if (!event.getPictureFileName().equals(pictureFileName)) {
            var result = event.changePictureFileName(pictureFileName);
            check(result);
        }
        return this;
    }

    public EventUpdater addDomainEvent(DomainEvent domainEvent) {
        if (domainEvent == null) return this;
        event.recordEvent(domainEvent);
        return this;
    }

    public EventUpdater setMaxAttendees(int maxInvitees) {
        if (maxInvitees < 0) return this;
        if (event.getMaxAssistanceCount() != maxInvitees) {
            // Validate that the new max invitees is not less than the number of accepted invitations
            eventValidation.validateEventMaxInviteesOnChange(maxInvitees, event.getEventId());
            var result = event.setMaxAttendees(maxInvitees);
            check(result);
        }
        return this;
    }

    public Result<Event> update() {
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        // Publish the event update domain event
        event.recordEvent(new EventUpdate());
        return Result.success(event);
    }
}
