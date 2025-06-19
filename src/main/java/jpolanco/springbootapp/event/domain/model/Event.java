package jpolanco.springbootapp.event.domain.model;

import jpolanco.springbootapp.event.application.ports.input.request.StaffRequest;
import jpolanco.springbootapp.event.domain.errors.EventDomainError;
import jpolanco.springbootapp.event.domain.model.domainevents.EventCreated;
import jpolanco.springbootapp.event.domain.model.valueobjects.*;
import jpolanco.springbootapp.shared.domain.DomainEvent;
import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.valueobjects.UserId;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Event {
    private final EventId eventId;
    private Header header;
    private Schedule schedule;
    private Duration duration;
    private EventStatus status;
    private Location location;
    private Categories category;
    private boolean isPublic;
    private final boolean enableComments;
    private Modality modality;
    private List<Staff> staff;
    private PictureFileName pictureFileName;
    private UserId creatorId;
    private final Instant createdAt;
    private List<DomainEvent> events = new ArrayList<>();
    private Attendees attendees;

    protected Event(EventId eventId, Header header, Schedule schedule, Duration duration, EventStatus status,
                    Location location, Categories category, boolean isPublic, boolean enableComments,
                    Modality modality, List<Staff> staff, PictureFileName pictureFileName, UserId creatorId,
                    Instant createdAt, Attendees attendees) {
        this.eventId = eventId;
        this.header = header;
        this.schedule = schedule;
        this.duration = duration;
        this.status = status;
        this.location = location;
        this.category = category;
        this.isPublic = isPublic;
        this.enableComments = enableComments;
        this.modality = modality;
        this.staff = staff;
        this.pictureFileName = pictureFileName;
        this.creatorId = creatorId;
        this.createdAt = createdAt;
        this.attendees = attendees;
    }

    public static Result<Event> create(
            String title,
            String description,
            Instant schedule,
            long durationInSeconds,
            String locationName,
            String locationCity,
            String locationCountry,
            double latitude,
            double longitude,
            List<String> categories,
            boolean isPublic,
            boolean enableComments,
            Modality modality,
            String pictureFileName,
            List<StaffRequest> staffs,
            String creatorId,
            long maxAttendees
                                       ) {
        var id = UUID.randomUUID().toString();
        var maybeEventId = builder()
                .eventId(id)
                .header(title, description)
                .schedule(schedule)
                .duration(durationInSeconds)
                .status(EventStatus.SCHEDULED)
                .location(latitude, longitude, locationName, locationCity, locationCountry)
                .categories(categories)
                .isPublic(isPublic)
                .isEnableComments(enableComments)
                .modality(modality)
                .staffs(staffs)
                .pictureFileName(pictureFileName)
                .creatorId(creatorId)
                .createdAt(Instant.now())
                .attendees(maxAttendees, 0)
                .addDomainEvent(new EventCreated(
                        id,title,
                        description,
                        durationInSeconds,
                        locationName + ", "
                                + locationCity + ", "
                                + locationCountry,
                        creatorId))
                .build();
        if (maybeEventId.isFailure()) {
            return Result.failure(maybeEventId.getError());
        }
        var event = maybeEventId.getValue();
        return Result.success(event);
    }

    public static Result<Event> load(
            String eventId,
            String title,
            String description,
            Instant schedule,
            long durationInSeconds,
            EventStatus status,
            String locationName,
            String locationCity,
            String locationCountry,
            double latitude,
            double longitude,
            List<String> categories,
            boolean isPublic,
            boolean enableComments,
            Modality modality,
            List<StaffRequest> staffs,
            String pictureFileName,
            String creatorId,
            Instant createdAt,
            long maxAttendees,
            long currentAttendees
    ) {
        var maybeEvent = builder()
                .eventId(eventId)
                .header(title, description)
                .schedule(schedule)
                .duration(durationInSeconds)
                .status(status)
                .location(latitude, longitude, locationName, locationCity, locationCountry)
                .categories(categories)
                .isPublic(isPublic)
                .isEnableComments(enableComments)
                .modality(modality)
                .staffs(staffs)
                .pictureFileName(pictureFileName)
                .creatorId(creatorId)
                .createdAt(createdAt)
                .attendees(maxAttendees, currentAttendees)
                .build();
        if (maybeEvent.isFailure()) {
            return Result.failure(maybeEvent.getError());
        }
        return Result.success(maybeEvent.getValue());
    }


    private static EventBuilder builder() {
        return new EventBuilder();
    }

    public String getEventId() {
        return eventId.getValue();
    }

    // Header methods
    public String getTitle() {
        return header.getTitle();
    }

    public String getDescription() {
        return header.getDescription();
    }

    public Result<Void> changeTitle(String title) {
        var maybeTitle = Header.create(title, this.header.getDescription());
        if (maybeTitle.isFailure()) {
            return Result.failure(maybeTitle.getError());
        }
        return Result.success();
    }

    public Result<Void> changeDescription(String description) {
        var maybeDescription = Header.create(this.header.getTitle(), description);
        if (maybeDescription.isFailure()) {
            return Result.failure(maybeDescription.getError());
        }
        this.header = maybeDescription.getValue();
        return Result.success();
    }

    // Schedule methods
    public Instant getSchedule() {
        return schedule.getValue();
    }

    public Result<Void> changeSchedule(Instant date) {
        var maybeSchedule = Schedule.create(date);
        if (maybeSchedule.isFailure()) {
            return Result.failure(maybeSchedule.getError());
        }
        this.schedule = maybeSchedule.getValue();
        return Result.success();
    }

    // Duration methods
    public long getDurationInSeconds() {
        return duration.getValue();
    }

    public long getDurationInMinutes() {
        return duration.getValue() / 60;
    }

    public long getDurationInHours() {
        return duration.getValue() / 3600;
    }

    public Result<Void> changeDuration(long durationInSeconds) {
        var maybeDuration = Duration.create(durationInSeconds);
        if (maybeDuration.isFailure()) {
            return Result.failure(maybeDuration.getError());
        }
        this.duration = maybeDuration.getValue();
        return Result.success();
    }

    // Status methods
    public EventStatus getStatus() {
        return status;
    }

    public boolean isScheduled() {
        return status.equals(EventStatus.SCHEDULED);
    }

    public boolean isCancelled() {
        return status.equals(EventStatus.CANCELLED);
    }

    public boolean isCompleted() {
        return status.equals(EventStatus.COMPLETED);
    }

    public boolean isInProgress() {
        return status.equals(EventStatus.IN_PROGRESS);
    }

    public void schedule() {
        this.status = EventStatus.SCHEDULED;
    }

    public void cancel() {
        this.status = EventStatus.CANCELLED;
    }

    public void complete() {
        this.status = EventStatus.COMPLETED;
    }

    public void changeStatus(EventStatus status) {
        this.status = status;
    }


    // Location methods
    public String getLocationName() {
        return location.getName();
    }

    public String getLocationCity() {
        return location.getCity();
    }

    public String getLocationCountry() {
        return location.getCountry();
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    public Result<Void> changeLocationName(String locationName) {
        var maybeLocation = Location.create(this.getLatitude(), this.getLongitude(), locationName, this.getLocationCity(), this.getLocationCountry());
        if (maybeLocation.isFailure()) {
            return Result.failure(maybeLocation.getError());
        }
        this.location = maybeLocation.getValue();
        return Result.success();
    }

    public Result<Void> changeLocationCity(String locationCity) {
        var maybeLocation = Location.create(this.getLatitude(), this.getLongitude(), this.getLocationName(), locationCity, this.getLocationCountry());
        if (maybeLocation.isFailure()) {
            return Result.failure(maybeLocation.getError());
        }
        this.location = maybeLocation.getValue();
        return Result.success();
    }

    public Result<Void> changeLocationCountry(String locationCountry) {
        var maybeLocation = Location.create(this.getLatitude(), this.getLongitude(), this.getLocationName(), this.getLocationCity(), locationCountry);
        if (maybeLocation.isFailure()) {
            return Result.failure(maybeLocation.getError());
        }
        this.location = maybeLocation.getValue();
        return Result.success();
    }

    public Result<Void> changeLatitude(double latitude) {
        var maybeLocation = Location.create(latitude, this.getLongitude(), this.getLocationName(), this.getLocationCity(), this.getLocationCountry());
        if (maybeLocation.isFailure()) {
            return Result.failure(maybeLocation.getError());
        }
        this.location = maybeLocation.getValue();
        return Result.success();
    }

    public Result<Void> changeLongitude(double longitude) {
        var maybeLocation = Location.create(this.getLatitude(), longitude, this.getLocationName(), this.getLocationCity(), this.getLocationCountry());
        if (maybeLocation.isFailure()) {
            return Result.failure(maybeLocation.getError());
        }
        this.location = maybeLocation.getValue();
        return Result.success();
    }

    public Result<Void> changeLocation(double latitude, double longitude, String locationName, String locationCity, String locationCountry) {
        var maybeLocation = Location.create(latitude, longitude, locationName, locationCity, locationCountry);
        if (maybeLocation.isFailure()) {
            return Result.failure(maybeLocation.getError());
        }
        this.location = maybeLocation.getValue();
        return Result.success();
    }

    // Categories methods

    public Categories getCategories() {
        return category;
    }

    public Result<Void> changeAllCategories(List<String> categories) {
        var maybeCategories = Categories.create(categories);
        if (maybeCategories.isFailure()) {
            return Result.failure(maybeCategories.getError());
        }
        this.category.addCategories(categories);
        return Result.success();
    }

    public void removeCategory(String category) {
        this.category.removeCategory(category);
    }

    public Result<Void> removeCategories(List<String> categories) {
        for (String cat : categories) {
            this.category.removeCategory(cat);
        }
        if (this.category.isEmpty()) {
            return Result.failure(EventDomainError.CATEGORIES_EMPTY);
        }
        return Result.success();
    }

    public void addCategory(String category) {
        this.category.addCategory(category);
    }

    public void addCategories(List<String> categories) {
        this.category.addCategories(categories);
    }

    public Result<Void> changeCategories(List<String> categories) {
        var maybeCategories = Categories.create(categories);
        if (maybeCategories.isFailure()) {
            return Result.failure(maybeCategories.getError());
        }
        this.category = maybeCategories.getValue();
        return Result.success();
    }

    // Preference methods
    public boolean isPublic() {
        return isPublic;
    }

    public boolean isPrivate() {
        return !isPublic;
    }


    public void makePublic() {
        this.isPublic = true;
    }

    public void makePrivate() {
        this.isPublic = false;
    }

    public void enableComments() {
        this.isPublic = true;
    }

    public void disableComments() {
        this.isPublic = false;
    }

    public boolean isEnableComments() {
        return enableComments;
    }

    public Modality getModality() {
        return modality;
    }

    public Result<Void> changeModality(Modality modality) {
        this.modality = modality;
        return Result.success();
    }

    public void makeVirtual() {
        this.modality = Modality.VIRTUAL;
    }

    public void makeInPerson() {
        this.modality = Modality.IN_PERSON;
    }

    public void makeHybrid() {
        this.modality = Modality.HYBRID;
    }

    // Staff methods
    public List<String> getStaffIds() {
        return staff.stream()
                .map(staffMember -> staffMember.getUserId().getValue())
                .toList();
    }

    public List<String> getStaffRoles() {
        return staff.stream()
                .map(Staff::getRole)
                .toList();
    }

    public List<String> getStaffAssistanceClerkIds() {
        return staff.stream()
                .filter(Staff::isAssistanceClerk)
                .map(staffMember -> staffMember.getUserId().getValue())
                .toList();
    }

    public void setStaff(List<StaffRequest> staff) {
        this.staff = staff.stream()
                .map(staffHolder -> Staff.create(staffHolder.staffId(), staffHolder.role(), staffHolder.isAssistanceClerk()))
                .filter(Result::isSuccess)
                .map(Result::getValue)
                .toList();
    }

    public List<Staff> getStaff() {
        return staff;
    }

    public Result<Void> addStaff(String userId, String role, boolean assistance_clerk) {
        var maybeStaff = Staff.create(userId, role, assistance_clerk);
        if (maybeStaff.isFailure()) {
            return Result.failure(maybeStaff.getError());
        }
        if (this.staff.stream().noneMatch(staffMember -> staffMember.getUserId().equals(maybeStaff.getValue().getUserId()))) {
            this.staff.add(maybeStaff.getValue());
        }
        return Result.success();
    }

    public Result<Void> removeStaff(String userId) {
        var staffToRemove = this.staff.stream()
                .filter(staffMember -> staffMember.getUserId().getValue().equals(userId))
                .findFirst();

        if (staffToRemove.isPresent()) {
            this.staff.remove(staffToRemove.get());
            return Result.success();
        } else {
            return Result.failure(EventDomainError.STAFF_NOT_FOUND);
        }
    }

    public Result<Void> clearStaff() {
        this.staff.clear();
        return Result.success();
    }

    // Creator methods
    public String getCreatorId() {
        return creatorId.getValue();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // Picture methods
    public String getPictureFileName() {
        return pictureFileName.getValue();
    }


    public Result<Void> changePictureFileName(String fileName) {
        var maybePictureFileName = PictureFileName.create(fileName);
        if (maybePictureFileName.isFailure()) {
            return Result.failure(maybePictureFileName.getError());
        }
        this.pictureFileName = maybePictureFileName.getValue();
        return Result.success();
    }

    // Atendees methods
    public long getMaxAttendees() {
        return this.attendees.getMaxAttendees();
    }

    public long getCurrentAttendees() {
        return this.attendees.getCurrentAttendees();
    }

    public Result<Void> addAttendee() {
        var result = this.attendees.addAttendee();
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success();
    }

    public Result<Void> changeMaxAttendees(int maxInvitees) {
        var maybeAttendees = Attendees.create(maxInvitees, this.attendees.getCurrentAttendees());
        if (maybeAttendees.isFailure()) {
            return Result.failure(maybeAttendees.getError());
        }
        this.attendees = maybeAttendees.getValue();
        return Result.success();
    }

    // Domain events
    public List<DomainEvent> pullEvents() {
        return this.events;
    }

    public void recordEvent(DomainEvent event) {
        if (event != null) {
            this.events.add(event);
        }
    }

    public void clearEvents() {
        this.events.clear();
    }

}
