package jpolanco.springbootapp.event.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.event.application.ports.input.StaffHolder;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.domain.model.valueobjects.Staff;
import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.*;
import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.LocationEntity;
import jpolanco.springbootapp.event.infrastructure.adapters.output.repository.JpaStaffRepository;
import jpolanco.springbootapp.event.infrastructure.adapters.output.repository.JpaStaffRoleRepository;
import jpolanco.springbootapp.event.infrastructure.errors.EventIntegrity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class EventEntityMapperImpl implements EventEntityMapper {
    private final JpaUserRepository jpaUserRepository;
    private final CategoryEntityMapper categoryEntityMapper;
    private final JpaStaffRoleRepository jpaStaffRoleRepository;
    private final JpaStaffRepository jpaStaffRepository;


    private int validScheduleForStaff(String eventId, Staff staff, Instant date, long duration) {
        var basic = jpaStaffRepository.findByUserIdAndEventId(UUID.fromString(staff.getUserId().getValue()), UUID.fromString(eventId));
        if (basic.isEmpty()) {
            return 0; // No staff found for the user in this event
        }
        var eventsForStaff = jpaStaffRepository.findAllByUserId(UUID.fromString(staff.getUserId().getValue()));
        if (eventsForStaff.isEmpty()) {
            return 0; // No events found for the staff member
        }
        for (StaffEntity staffEntity : eventsForStaff) {
            if (staffEntity.getEvent().getId().equals(UUID.fromString(eventId))) {
                continue; // Skip the current event
            }
            Instant eventStart = staffEntity.getEvent().getSchedule();
            Instant eventEnd = eventStart.plusSeconds(staffEntity.getEvent().getDurationInSeconds());
            Instant newEventEnd = date.plusSeconds(duration);

            if (eventStart.equals(date)) {
                return 1; // Staff member already has an event at the same schedule
            }
            if (eventStart.isBefore(newEventEnd) && eventEnd.isAfter(date)) {
                return 2; // Overlapping schedules
            }
        }
        return 0; // No conflicts found
    }

    @Override
    public EventEntity toEntity(Event domain) {

        var eventEntity = new EventEntity();
        var eventId = UUID.fromString(domain.getEventId());
        eventEntity.setId(eventId);

        // Create LocationEntity and PreferencesEntity
        var location = new LocationEntity();
        location.setId(UUID.randomUUID());
        location.setName(domain.getLocationName());
        location.setCity(domain.getLocationCity());
        location.setCountry(domain.getLocationCountry());
        location.setLatitude(domain.getLatitude());
        location.setLongitude(domain.getLongitude());

        var preferences = new PreferencesEntity();
        preferences.setId(UUID.randomUUID()); // Assuming ID is auto-generated
        preferences.setPublic(domain.isPublic());
        preferences.setEnableComments(domain.isEnableComments());
        preferences.setModality(domain.getModality());

        // Map staff members to StaffEntity and set roles
        List<StaffEntity> staffEntityList;
        if (domain.getStaff().isEmpty() || domain.getStaff() == null) {
            eventEntity.setStaff(null);
        } else {
            staffEntityList = domain.getStaff().stream()
                    .map(staff -> {
                        if (staff == null || staff.getUserId() == null || staff.getRole() == null) {
                            throw new EventIntegrity("Staff member or required fields are null");
                        }
                        switch (validScheduleForStaff(domain.getEventId(), staff, domain.getSchedule(), domain.getDurationInSeconds())) {
                            case 1:
                                throw new EventIntegrity("Staff member already has an event at the same schedule: " + staff.getUserId().getValue());
                            case 2:
                                throw new EventIntegrity("Overlapping schedules for staff member: " + staff.getUserId().getValue());
                            case 0:
                                // No conflicts found, proceed
                                break;
                        }
                        var staffEntity = new StaffEntity();
                        staffEntity.setEvent(eventEntity);

                        staffEntity.setAssistanceClerk(staff.isAssistanceClerk());
                        var role = jpaStaffRoleRepository.findById(staff.getRole());
                        if (role.isEmpty()) {
                            return null;
                        }
                        staffEntity.setRole(role.get());

                        var userEntity = jpaUserRepository.findById(UUID.fromString(staff.getUserId().getValue()));
                        userEntity.ifPresent(staffEntity::setUser);

                        return staffEntity;
                    })
                    .filter(Objects::nonNull)
                    .toList();
            if (staffEntityList.isEmpty()) {
                eventEntity.setStaff(null);
            }
        }

        // Set eventEntity properties
        eventEntity.setTitle(domain.getTitle());
        eventEntity.setDescription(domain.getDescription());
        eventEntity.setSchedule(domain.getSchedule());
        eventEntity.setDurationInSeconds(domain.getDurationInSeconds());
        eventEntity.setStatus(domain.getStatus());
        eventEntity.setPicture_path(domain.getPictureFileName());
        eventEntity.setCreator(jpaUserRepository.findById(UUID.fromString(domain.getCreatorId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + domain.getCreatorId())));
        eventEntity.setLocation(location);
        eventEntity.setPreferences(preferences);
        eventEntity.setCategories(new HashSet<>(categoryEntityMapper.toEntity(domain.getCategories())));
        eventEntity.setCreatedAt(domain.getCreatedAt());
        eventEntity.setMaxInvitees(domain.getMaxAssistanceCount());
        return eventEntity;
    }

    @Override
    public Event toDomain(EventEntity entity) {
        var categories = categoryEntityMapper.toDomain(new ArrayList<>(entity.getCategories()));
        var maybeEvent = Event.load(
                entity.getId().toString(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getSchedule().toString(),
                entity.getDurationInSeconds(),
                entity.getStatus(),
                entity.getLocation().getName(),
                entity.getLocation().getCity(),
                entity.getLocation().getCountry(),
                entity.getLocation().getLatitude(),
                entity.getLocation().getLongitude(),
                categories.getValues(),
                entity.getPreferences().isPublic(),
                entity.getPreferences().isEnableComments(),
                entity.getPreferences().getModality().name(),
                entity.getStaff().stream().
                        map(staffEntity -> {
                            return new StaffHolder(
                                    staffEntity.getUser().getId().toString(),
                                    staffEntity.getRole().getName(),
                                    staffEntity.isAssistanceClerk());
                        })
                        .collect(Collectors.toList()),
                entity.getPicture_path(),
                entity.getCreator().getId().toString(),
                entity.getCreatedAt(),
                entity.getMaxInvitees()
        );
        if (maybeEvent.isFailure()) {
            throw new EventIntegrity("Error converting EventEntity to Event: " + maybeEvent.getMessage());
        }
        return maybeEvent.getValue();
    }
}
