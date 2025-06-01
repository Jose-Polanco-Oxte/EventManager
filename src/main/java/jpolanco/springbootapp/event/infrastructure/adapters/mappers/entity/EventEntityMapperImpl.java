package jpolanco.springbootapp.event.infrastructure.adapters.mappers.entity;

import jpolanco.springbootapp.event.application.ports.input.StaffHolder;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.domain.model.Modality;
import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.*;
import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.LocationEntity;
import jpolanco.springbootapp.event.infrastructure.adapters.output.repository.JpaEventRepository;
import jpolanco.springbootapp.event.infrastructure.adapters.output.repository.JpaStaffRoleRepository;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class EventEntityMapperImpl implements EventEntityMapper {
    private final JpaUserRepository jpaUserRepository;
    private final CategoryEntityMapper categoryEntityMapper;
    private final JpaStaffRoleRepository jpaStaffRoleRepository;

    @Override
    public EventEntity toEntity(Event domain) {

        var eventEntity = new EventEntity();
        var eventId = UUID.fromString(domain.getEventId());
        eventEntity.setId(eventId);

        // Create LocationEntity and PreferencesEntity
        var location = new LocationEntity(
                null,
                "Unknown Location",
                domain.getLatitude(),
                domain.getLongitude(),
                eventEntity
        );

        var preferences = new PreferencesEntity(
                null,
                domain.isPublic(),
                domain.getModality(),
                domain.isEnableComments(),
                eventEntity
        );

        // Map staff members to StaffEntity and set roles
        var staffEntityList = domain.getStaff().stream()
                .map(staff -> {
                    var staffEntity = new StaffEntity();
                    staffEntity.setEvent(eventEntity);

                    staffEntity.setAssistanceClerk(staff.isAssistanceClerk());
                    var role = jpaStaffRoleRepository.findById(staff.getRole())
                            .orElseThrow(() -> new IllegalArgumentException("Role not found: " + staff.getRole()));
                    staffEntity.setRole(role);

                    var userEntity = jpaUserRepository.findById(UUID.fromString(staff.getUserId().getValue()));
                    userEntity.ifPresent(staffEntity::setUser);

                    return staffEntity;
                })
                .toList();
        eventEntity.setStaff(staffEntityList);

        // Set eventEntity properties
        eventEntity.setId(eventId);
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
        eventEntity.setStaff(staffEntityList);
        eventEntity.setCategories(new HashSet<>(categoryEntityMapper.toEntity(domain.getCategories())));
        eventEntity.setCreatedAt(domain.getCreatedAt());
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
                entity.getCreatedAt()
        );
        if (maybeEvent.isFailure()) {
            throw new IllegalArgumentException("Error converting EventEntity to Event: " + maybeEvent.getError());
        }
        return maybeEvent.getValue();
    }
}
