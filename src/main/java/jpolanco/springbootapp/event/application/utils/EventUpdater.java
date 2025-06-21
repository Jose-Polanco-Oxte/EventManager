package jpolanco.springbootapp.event.application.utils;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.input.providers.FileStorageProvider;
import jpolanco.springbootapp.event.application.ports.input.request.StaffRequest;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.domain.model.valueobjects.EventStatus;
import jpolanco.springbootapp.event.domain.model.valueobjects.Modality;
import jpolanco.springbootapp.event.domain.model.domain_events.EventUpdate;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.StaffChangeRequest;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class EventUpdater {

    private final Event event;
    private final FileStorageProvider fileStorage;
    private final EventValidation eventValidation;
    private Result<?> result = Result.success();
    private final List<Changes<?>> changes = new ArrayList<>();

    private void check(Result<?> result) {
        if (result.isFailure() && this.result.isSuccess()) {
            this.result = result;
        }
    }

    public static EventUpdater updater(Event event, FileStorageProvider fileStorageProvider, EventValidation eventValidation) {
        return new EventUpdater(event, fileStorageProvider, eventValidation);
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
            var oldTitle = event.getTitle();
            var result = event.changeTitle(title);
            check(result);
            changes.add(new Changes<>("title", oldTitle, title));
        }
        return this;
    }

    public EventUpdater description(String description) {
        if (nullable(description) > 0) return this;
        if (!event.getDescription().equals(description)) {
            var oldDescription = event.getDescription();
            var result = event.changeDescription(description);
            check(result);
            changes.add(new Changes<>("description", oldDescription, description));
        }
        return this;
    }

    public EventUpdater schedule(Instant schedule) {
        if (!event.getSchedule().equals(schedule)) {
            var result = eventValidation.validate(event.getCreatorId(), schedule, event.getDurationInSeconds(), event.getLatitude(), event.getLongitude());
            check(result);
            if (result.isSuccess()) {
                var oldSchedule = event.getSchedule();
                var changeResult = event.changeSchedule(schedule);
                check(changeResult);
                changes.add(new Changes<>("schedule", oldSchedule, schedule));
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
                var oldDuration = event.getDurationInSeconds();
                var changeResult = event.changeDuration(durationInSeconds);
                check(changeResult);
                changes.add(new Changes<>("duration", oldDuration, durationInSeconds));
            }
        }
        return this;
    }

    public EventUpdater status(EventStatus status) {
        if (status == null || event.getStatus() == status) return this;
        var oldStatus = event.getStatus();
        event.changeStatus(status);
        changes.add(new Changes<>("status", oldStatus.getValue(), status.getValue()));
        return this;
    }

    public EventUpdater location(String locationName, String locationCity, String locationCountry, double latitude, double longitude) {
        var oldLatitude = event.getLatitude();
        var oldLongitude = event.getLongitude();
        var oldLocationName = event.getLocationName();
        var oldLocationCity = event.getLocationCity();
        var oldLocationCountry = event.getLocationCountry();
        if (latitude == event.getLatitude() && longitude == event.getLongitude()) {
            // No change in location, return early
            return this;
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
        changes.add(new Changes<>("location", new location(oldLatitude, oldLongitude, oldLocationName, oldLocationCity, oldLocationCountry)
                , new location(latitude, longitude, locationName, locationCity, locationCountry)));
        return this;
    }

    private record location(
            double latitude,
            double longitude,
            String name,
            String city,
            String country
    ){}

    public EventUpdater categories(List<String> remove, List<String> add) {
        if (noChanges(remove) && noChanges(add)) return this;
        var originalCategories = event.getCategories();
        if (noChanges(remove)) {
            event.addCategories(add);
        } else if (noChanges(add)) {
            event.removeCategories(remove);
        } else {
            event.removeCategories(remove);
            event.addCategories(add);
        }
        changes.add(new Changes<>("categories", originalCategories, event.getCategories()));
        return this;
    }

    private boolean noChanges(List<?> list) {
        return list == null || list.isEmpty();
    }

    public EventUpdater isPublic(boolean isPublic) {
        if (event.isPublic() == isPublic) return this;
        var oldVisibility = event.isPublic();
        if (isPublic) {event.makePublic();} else { event.makePrivate();}
        changes.add(new Changes<>("visibility", oldVisibility, isPublic));
        return this;
    }

    public EventUpdater enableComments(boolean enableComments) {
        if (event.isEnableComments() == enableComments) return this;
        var oldEnableComments = event.isEnableComments();
        if (enableComments) {event.enableComments();} else {event.disableComments();}
        changes.add(new Changes<>("enableComments", oldEnableComments, enableComments));
        return this;
    }

    public EventUpdater modality(Modality modality) {
        if (modality == null || modality.equals(event.getModality())) return this;
        var oldModality = event.getModality();
        if (!event.getModality().equals(modality)) {
            var result = event.changeModality(modality);
            check(result);
        }
        changes.add(new Changes<>("modality", oldModality.getValue(), modality.getValue()));
        return this;
    }

    public EventUpdater staff(StaffChangeRequest staffs) {
        var oldStaff = event.getStaff().stream()
                .map(staff -> new StaffRequest(staff.getUserId().getValue(), staff.getRole(), staff.isAssistanceClerk()))
                .toList();
        if (staffs.clear()) {
            event.clearStaff();
        } else {
            if (noChanges(staffs.remove()) && noChanges(staffs.add())) return this;
            if (noChanges(staffs.remove())) {
                this.event.addStaffs(staffs.add());
            } else if (noChanges(staffs.add())) {
                this.event.removeStaffs(staffs.remove());
            } else {
                this.event.removeStaffs(staffs.remove());
                this.event.addStaffs(staffs.add());
            }
        }
        List<StaffRequest> newStaff;
        if (event.getStaff().isEmpty()) {
            newStaff = List.of();
        } else {
            newStaff = event.getStaff().stream()
                    .map(staff -> new StaffRequest(staff.getUserId().getValue(), staff.getRole(), staff.isAssistanceClerk()))
                    .toList();
        }
        changes.add(new Changes<>("staff", oldStaff, newStaff));
        return this;
    }

    public EventUpdater changePicture(InputStream imageStream) {
        if (imageStream == null) return this;
        var oldPictureFileName = event.getPictureFileName();
        var fileStored = fileStorage.storeImage(event.getPictureFileName(), imageStream);
        if (fileStored == null) {
            result = Result.failure(EventAppError.IMAGE_STORAGE_ERROR);
            return this;
        }
        changes.add(new Changes<>("picture", oldPictureFileName, event.getPictureFileName()));
        return this;
    }

    public EventUpdater setMaxAttendees(int maxAttendees) {
        if (maxAttendees < 0) return this;
        var oldMaxAttendees = event.getMaxAttendees();
        if (event.getMaxAttendees() != maxAttendees) {
            if (maxAttendees < event.getCurrentAttendees()) {
                check(Result.failure(EventAppError.MAX_ATTENDEES_LESS_THAN_CURRENT));
                return this;
            }
            var result = event.changeMaxAttendees(maxAttendees);
            check(result);
            changes.add(new Changes<>("maxAttendees", oldMaxAttendees, maxAttendees));
        }
        return this;
    }

    public Result<Event> update() {
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        // Publish the event update domain event
        if (!changes.isEmpty()) {
            event.recordEvent(new EventUpdate(
                    event.getEventId(),
                    event.getTitle(),
                    changes.isEmpty() ? List.of() : changes
            ));
        }
        return Result.success(event);
    }
}
