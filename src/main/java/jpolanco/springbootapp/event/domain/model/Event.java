package jpolanco.springbootapp.event.domain.model;

import jpolanco.springbootapp.event.application.ports.input.request.StaffRequest;
import jpolanco.springbootapp.event.domain.model.domain_events.*;
import jpolanco.springbootapp.event.domain.model.valueobjects.*;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.value_objects.UserId;

import java.time.Instant;
import java.util.*;

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
    private Set<Staff> staff;
    private PictureFileName pictureFileName;
    private UserId creatorId;
    private final Instant createdAt;
    private List<EventNotification> events = new ArrayList<>();
    private Attendees attendees;

    protected Event(EventId eventId, Header header, Schedule schedule, Duration duration, EventStatus status,
                    Location location, Categories category, boolean isPublic, boolean enableComments,
                    Modality modality, Set<Staff> staff, PictureFileName pictureFileName, UserId creatorId,
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
        return UUID.randomUUID().toString(); // Its a placeholder, should be replaced with actual eventId
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
        recordEvent(
                new EventScheduleChanged(
                        getEventId(),
                        getSchedule(),
                        getTitle()
                )
        );
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
        recordEvent(
                new EventDurationChanged(
                        getEventId(),
                        getTitle(),
                        getDurationInSeconds()
                )
        );
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

    public void cancel(String reason) {
        this.status = EventStatus.CANCELLED;
        recordEvent(new EventCancelled(getEventId(), getTitle(), reason));
    }

    public void restore(String messageToAttendees) {
        this.status = EventStatus.SCHEDULED;
        recordEvent(new EventRestored(getEventId(), getTitle(), messageToAttendees));
    }

    public void complete() {
        this.status = EventStatus.COMPLETED;
        recordEvent(new EventCompleted(getEventId(), getTitle()));
    }

    public void start() {
        this.status = EventStatus.IN_PROGRESS;
        recordEvent(new EventStarted(getEventId(), getTitle(), getSchedule()));
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
        recordEvent(
                new EventLocationChanged(
                        getEventId(),
                        getTitle(),
                        latitude,
                        longitude,
                        locationName,
                        locationCity,
                        locationCountry
                )
        );
        return Result.success();
    }

    // Categories methods

    public List<String> getCategories() {
        return category.getValues();
    }

    public void removeCategories(List<String> categories) {
        this.category.removeCategories(categories);
    }

    public void addCategories(List<String> categories) {
        this.category.addCategories(categories);
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
        recordEvent(new EventPrivacyChanged(getEventId(), getTitle(), true));
    }

    public void makePrivate() {
        this.isPublic = false;
        recordEvent(new EventPrivacyChanged(getEventId(), getTitle(), false));
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
        recordEvent(new EventModalityChanged(getEventId(), getTitle(), modality));
        return Result.success();
    }

    public void makeVirtual() {
        this.modality = Modality.VIRTUAL;
        recordEvent(new EventModalityChanged(getEventId(), getTitle(), Modality.VIRTUAL));
    }

    public void makeInPerson() {
        this.modality = Modality.IN_PERSON;
        recordEvent(new EventModalityChanged(getEventId(), getTitle(), Modality.IN_PERSON));
    }

    public void makeHybrid() {
        this.modality = Modality.HYBRID;
        recordEvent(new EventModalityChanged(getEventId(), getTitle(), Modality.HYBRID));
    }

    public void addStaffs(List<StaffRequest> staffs) {
        // add new staff members to the event and record only new staff members
        var newStaff = staffs.stream()
                .map(this::toValidStaff)
                .flatMap(Optional::stream)
                .filter(staff::add)
                .toList();
        if (!newStaff.isEmpty()) {
            var staffRequests = newStaff.stream()
                    .map(staffMember -> new StaffRequest(
                            staffMember.getUserId().getUUID().toString(), // Its a placeholder, should be replaced with actual userId
                            staffMember.getRole(),
                            staffMember.isAssistanceClerk()))
                    .toList();
            recordEvent(new EventStaffAdded(getEventId(), getTitle(), staffRequests));
        }
    }

    public void removeStaffs(List<String> staffIds) {
        // remove staff members from the event and record only removed staff members
        var removedStaff = staffIds.stream()
                .map(userId -> this.staff.stream()
                        .filter(staffMember -> staffMember.getUserId().getUUID().toString().equals(userId)) // Its a placeholder, should be replaced with actual userId
                        .findFirst())
                .flatMap(Optional::stream)
                .filter(staff::remove)
                .toList();
        if (!removedStaff.isEmpty()) {
            var staffRequests = removedStaff.stream()
                    .map(staffMember -> new StaffRequest(
                            staffMember.getUserId().getUUID().toString(), // Its a placeholder, should be replaced with actual userId
                            staffMember.getRole(),
                            staffMember.isAssistanceClerk()))
                    .toList();
            recordEvent(new EventStaffRemoved(getEventId(), getTitle(), staffRequests));
        }
    }

    private Optional<Staff> toValidStaff(StaffRequest request) {
        if (request == null) return Optional.empty();
        var maybeStaff = Staff.create(request.staffId(), request.role(), request.isAssistanceClerk());
        if (maybeStaff.isFailure()) {
            return Optional.empty(); // skip invalid staff
        }
        return Optional.of(maybeStaff.getValue());
    }

    public List<Staff> getStaff() {
        return staff.stream().toList();
    }

    public void clearStaff() {
        var staffCleared = this.staff.stream()
                .map(staffMember -> new StaffRequest(
                        staffMember.getUserId().getUUID().toString(),
                        staffMember.getRole(),
                        staffMember.isAssistanceClerk()))
                .toList();
        this.staff.clear();
        recordEvent(
                new EventStaffCleared(
                        getEventId(),
                        getTitle(),
                        staffCleared
                )
        );
    }

    // Creator methods
    public String getCreatorId() {
        return creatorId.getUUID().toString();
    } // Its a placeholder, should be replaced with actual creatorId

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
        recordEvent(
                new EventMaxAttendeesChanged(
                        getEventId(),
                        getTitle(),
                        getMaxAttendees()
                )
        );
        return Result.success();
    }

    // Domain events
    public List<EventNotification> pullEvents() {
        return this.events;
    }

    public void recordEvent(EventNotification event) {
        if (event != null) {
            this.events.add(event);
        }
    }

    public void clearEvents() {
        this.events.clear();
    }

}
