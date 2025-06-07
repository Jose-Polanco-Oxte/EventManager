package jpolanco.springbootapp.event.domain.model;

import jpolanco.springbootapp.event.application.ports.input.StaffHolder;
import jpolanco.springbootapp.event.domain.model.domainevents.EventCreated;
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
    private int maxAssistanceCount;

    protected Event(EventId eventId, Header header, Schedule schedule, long durationInSeconds, EventStatus status,
                    Location location, Categories category, boolean isPublic, boolean enableComments,
                    Modality modality, List<Staff> staff, PictureFileName pictureFileName, UserId creatorId,
                    Instant createdAt, int maxAssistanceCount) {
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
        this.maxAssistanceCount = maxAssistanceCount;
    }

    public static Result<Event> create(
            String title,
            String description,
            String schedule,
            long durationInSeconds,
            String locationName,
            String locationCity,
            String locationCountry,
            double latitude,
            double longitude,
            List<String> categories,
            boolean isPublic,
            boolean enableComments,
            String modality,
            String pictureFileName,
            List<StaffHolder> staffs,
            String creatorId,
            int maxInvitees
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
                .maxInvitees(maxInvitees)
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
            String schedule,
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
            String modality,
            List<StaffHolder> staffs,
            String pictureFileName,
            String creatorId,
            Instant createdAt,
            int maxInvitees
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
                .maxInvitees(maxInvitees)
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

    public Modality getModality() {
        return modality;
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

    public void setStaff(List<StaffHolder> staff) {
        this.staff = staff.stream()
                .map(staffHolder -> Staff.create(staffHolder.staffId(), staffHolder.role(), staffHolder.isAssistanceClerk()))
                .filter(Result::isSuccess)
                .map(Result::getValue)
                .toList();
    }

    public List<Staff> getStaff() {
        return staff;
    }

    public String getCreatorId() {
        return creatorId.getValue();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getPictureFileName() {
        return pictureFileName.getValue();
    }

    public int getMaxAssistanceCount() {
        return maxAssistanceCount;
    }


    // Domain methods

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
        if (durationInSeconds < 0) {
            return Result.failure(new Error("DURATION_NEGATIVE", "Duration cannot be negative"));
        } else if (durationInSeconds == 0) {
            return Result.failure(new Error("DURATION_ZERO", "Duration cannot be zero"));
        } else if (durationInSeconds > 24 * 60 * 60) { // More than 24 hours
            return Result.failure(new Error("DURATION_EXCEEDED", "Duration cannot exceed 24 hours"));
        } else if (durationInSeconds < 60 * 10) { // Less than 10 minutes
            return Result.failure(new Error("DURATION_TOO_SHORT", "Duration cannot be less than 10 minutes"));
        }
        this.durationInSeconds = durationInSeconds;
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
            return Result.failure(new Error("STAFF_NOT_FOUND", "Staff member not found"));
        }
    }

    public Result<Void> clearStaff() {
        this.staff.clear();
        return Result.success();
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

    public Result<Void> setMaxAttendees(int maxInvitees) {
        if (maxInvitees < 0) {
            return Result.failure(new Error("MAX_INVITEES_NEGATIVE", "Max invitees cannot be negative"));
        } else if (maxInvitees == 0) {
            return Result.failure(new Error("MAX_INVITEES_ZERO", "Max invitees cannot be zero"));
        } else if(maxInvitees > 1000) {
            return Result.failure(new Error("MAX_INVITEES_EXCEEDED", "Max invitees cannot exceed 1000"));
        } else {
            this.maxAssistanceCount = maxInvitees;
            return Result.success();
        }
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
