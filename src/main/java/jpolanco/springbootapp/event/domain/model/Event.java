package jpolanco.springbootapp.event.domain.model;

import jpolanco.springbootapp.event.application.ports.input.StaffHolder;
import jpolanco.springbootapp.event.domain.model.valueobjects.*;
import jpolanco.springbootapp.shared.domain.DomainEvent;
import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.valueobjects.UserId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Event {
    private final EventId eventId;
    private Header header;
    private Schedule schedule;
    private long durationInSeconds;
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

    protected Event(EventId eventId, Header header, Schedule schedule, long durationInSeconds, EventStatus status,
                    Location location, Categories category, boolean isPublic, boolean enableComments,
                    Modality modality, List<Staff> staff, PictureFileName pictureFileName, UserId creatorId,
                    Instant createdAt) {
        this.eventId = eventId;
        this.header = header;
        this.schedule = schedule;
        this.durationInSeconds = durationInSeconds;
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
    }

    public static Result<Event> create(
            String title,
            String description,
            String schedule,
            long durationInSeconds,
            double latitude,
            double longitude,
            List<String> categories,
            boolean isPublic,
            boolean enableComments,
            String modality,
            String pictureFileName,
            List<StaffHolder> staffs,
            String creatorId
                                       ) {
        var maybeEventId = builder()
                .eventId(UUID.randomUUID().toString())
                .header(title, description)
                .schedule(schedule)
                .duration(durationInSeconds)
                .status(EventStatus.SCHEDULED)
                .location(latitude, longitude)
                .categories(categories)
                .isPublic(isPublic)
                .isEnableComments(enableComments)
                .modality(modality)
                .staffs(staffs)
                .pictureFileName(pictureFileName)
                .creatorId(creatorId)
                .createdAt(Instant.now())
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
            String schedule,
            long durationInSeconds,
            EventStatus status,
            double latitude,
            double longitude,
            List<String> categories,
            boolean isPublic,
            boolean enableComments,
            String modality,
            List<StaffHolder> staffs,
            String pictureFileName,
            String creatorId,
            Instant createdAt
    ) {
        var maybeEvent = builder()
                .eventId(eventId)
                .header(title, description)
                .schedule(schedule)
                .duration(durationInSeconds)
                .status(status)
                .location(latitude, longitude)
                .categories(categories)
                .isPublic(isPublic)
                .isEnableComments(enableComments)
                .modality(modality)
                .staffs(staffs)
                .pictureFileName(pictureFileName)
                .creatorId(creatorId)
                .createdAt(createdAt)
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

    public String getTitle() {
        return header.getTitle();
    }

    public String getDescription() {
        return header.getDescription();
    }

    public Instant getSchedule() {
        return Instant.parse(schedule.getValue());
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    public EventStatus getStatus() {
        return status;
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    public Categories getCategories() {
        return category;
    }

    public void removeCategory(String category) {
        this.category.removeCategory(category);
    }

    public void addCategory(String category) {
        this.category.addCategory(category);
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isEnableComments() {
        return enableComments;
    }

    public String getModality() {
        return modality.name();
    }

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

    public List<Staff> getStaff() {
        return staff;
    }

    public String getCreatorId() {
        return creatorId.getValue();
    }

    public String getCreatedAt() {
        return createdAt.toString();
    }

    public String getPictureFileName() {
        return pictureFileName.getValue();
    }


    // Domain methods

    public Result<Void> changeHeader(String title, String description) {
        var maybeHeader = Header.create(title, description);
        if (maybeHeader.isFailure()) {
            return Result.failure(maybeHeader.getError());
        }
        this.header = maybeHeader.getValue();
        return Result.success();
    }

    public Result<Void> changeSchedule(String date) {
        var maybeSchedule = Schedule.create(date);
        if (maybeSchedule.isFailure()) {
            return Result.failure(maybeSchedule.getError());
        }
        this.schedule = maybeSchedule.getValue();
        return Result.success();
    }

    public Result<Void> changeDuration(long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
        return Result.success();
    }

    public Result<Void> changeLocation(double latitude, double longitude) {
        var maybeLocation = Location.create(latitude, longitude);
        if (maybeLocation.isFailure()) {
            return Result.failure(maybeLocation.getError());
        }
        this.location = maybeLocation.getValue();
        return Result.success();
    }

    public void changeStatus(EventStatus status) {
        this.status = status;
    }

    public Result<Void> changeCategories(List<String> categories) {
        var maybeCategories = Categories.create(categories);
        if (maybeCategories.isFailure()) {
            return Result.failure(maybeCategories.getError());
        }
        this.category = maybeCategories.getValue();
        return Result.success();
    }

    public Result<Void> addCategories(List<String> categories) {
        var maybeCategories = Categories.create(categories);
        if (maybeCategories.isFailure()) {
            return Result.failure(maybeCategories.getError());
        }
        this.category.addCategories(categories);
        return Result.success();
    }

    public void removeCategories(List<String> categories) {
        for (String category : categories) {
            this.category.removeCategory(category);
        }
    }

    public Result<Void> addStaff(String userId, String role, boolean assistance_clerk) {
        var maybeStaff = Staff.create(userId, role, assistance_clerk);
        if (maybeStaff.isFailure()) {
            return Result.failure(maybeStaff.getError());
        }
        this.staff.add(maybeStaff.getValue());
        return Result.success();
    }

    public Result<Void> removeStaff(UserId userId) {
        var staffToRemove = this.staff.stream()
                .filter(staffMember -> staffMember.getUserId().equals(userId))
                .findFirst();

        if (staffToRemove.isPresent()) {
            this.staff.remove(staffToRemove.get());
            return Result.success();
        } else {
            return Result.failure(new Error("STAFF_NOT_FOUND", "Staff member not found"));
        }
    }

    public Result<Void> changePictureFileName(String fileName) {
        var maybePictureFileName = PictureFileName.create(fileName);
        if (maybePictureFileName.isFailure()) {
            return Result.failure(maybePictureFileName.getError());
        }
        this.pictureFileName = maybePictureFileName.getValue();
        return Result.success();
    }

    public Result<Void> changeModality(Modality modality) {
        if (modality == null) {
            return Result.failure(new Error("MODALITY_IS_NULL", "Modality cannot be null"));
        }
        this.modality = modality;
        return Result.success();
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
    // Domain events

    public void recordEvent(DomainEvent event) {
        if (event != null) {
            this.events.add(event);
        }
    }

    public void clearEvents() {
        this.events.clear();
    }

}
