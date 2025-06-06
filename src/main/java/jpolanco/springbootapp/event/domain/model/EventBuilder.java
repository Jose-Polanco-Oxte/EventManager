package jpolanco.springbootapp.event.domain.model;

import jpolanco.springbootapp.event.application.ports.input.StaffHolder;
import jpolanco.springbootapp.event.domain.model.valueobjects.*;
import jpolanco.springbootapp.shared.domain.DomainEvent;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.domain.ResultBuilder;
import jpolanco.springbootapp.user.domain.model.valueobjects.UserId;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class EventBuilder {
    private EventId eventId;
    private Header header;
    private Schedule schedule;
    private long duration;
    private EventStatus status;
    private Location location;
    private Categories categories;
    private boolean isPublic = true;
    private boolean isEnableComments = true;
    private Modality modality;
    private List<Staff> staff = new ArrayList<>();
    private PictureFileName pictureFileName;
    private UserId creatorId;
    private Instant createdAt;
    private DomainEvent domainEvent;
    private Result<?> isValid = Result.success();

    private <T> T checker(Result<T> result) {
        if (result.isFailure() && !isValid.isFailure()) {
            isValid = Result.failure(result.getError());
            return null;
        } else if (result.isFailure()){
            return null;
        } else {
            return result.getValue();
        }
    }

    public EventBuilder eventId(String eventId) {
        var maybeId = EventId.create(eventId);
        this.eventId = checker(maybeId);
        return this;
    }

    public EventBuilder header(String title, String description) {
        var maybeHeader = Header.create(title, description);
        this.header = checker(maybeHeader);
        return this;
    }

    public EventBuilder schedule(String date) {
        var maybeSchedule = Schedule.create(date);
        this.schedule = checker(maybeSchedule);
        return this;
    }

    public EventBuilder duration(long durationInSeconds) {
        this.duration = durationInSeconds;
        return this;
    }

    public EventBuilder status(EventStatus status) {
        this.status = status;
        return this;
    }

    public EventBuilder location(double latitude, double longitude, String name, String city, String country) {
        var maybeLocation = Location.create(latitude, longitude, name, city, country);
        this.location = checker(maybeLocation);
        return this;
    }

    public EventBuilder categories(List<String> categories) {
        var maybeCategories = Categories.create(categories);
        this.categories = checker(maybeCategories);
        return this;
    }

    public EventBuilder isPublic(boolean isPublic) {
        this.isPublic = isPublic;
        return this;
    }

    public EventBuilder isEnableComments(boolean isEnableComments) {
        this.isEnableComments = isEnableComments;
        return this;
    }

    public EventBuilder modality(String modality) {
        this.modality = Modality.create(modality);
        return this;
    }

    public EventBuilder staffs(List<StaffHolder> staffs) {
        if (staffs == null || staffs.isEmpty()) {
            return this; // No staff to add, return early
        }
        System.out.println("Adding staffs: " + staffs.size());
        for (StaffHolder staffHolder : staffs) {
            if (staffHolder == null || staffHolder.staffId() == null) {
                continue; // Skip null staff holders
            }
            var maybeStaff = Staff.create(
                    staffHolder.staffId(),
                    staffHolder.role(),
                    staffHolder.isAssistanceClerk());
            var result = checker(maybeStaff);
            if (result != null) {
                this.staff.add(result);
            }
        }
        return this;
    }

    public EventBuilder pictureFileName(String fileName) {
        var maybePictureFileName = PictureFileName.create(fileName);
        this.pictureFileName = checker(maybePictureFileName);
        return this;
    }

    public EventBuilder creatorId(String creatorId) {
        var maybeId = UserId.create(creatorId);
        this.creatorId = checker(maybeId);
        return this;
    }

    public EventBuilder createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public EventBuilder addDomainEvent(DomainEvent domainEvent) {
        this.domainEvent = domainEvent;
        return this;
    }

    public Result<Event> build() {
        if (isValid.isFailure()) {
            return Result.failure(isValid.getError());
        }
        var event = new Event(
                eventId,
                header,
                schedule,
                duration,
                status,
                location,
                categories,
                isPublic,
                isEnableComments,
                modality,
                staff.stream().toList(),
                pictureFileName,
                creatorId,
                createdAt
        );
        if (domainEvent != null) {
            event.recordEvent(domainEvent);
        }
        return Result.success(event);
    }
}
