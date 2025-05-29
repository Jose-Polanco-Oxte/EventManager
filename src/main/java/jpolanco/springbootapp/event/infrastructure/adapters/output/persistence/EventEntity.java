package jpolanco.springbootapp.event.infrastructure.adapters.output.persistence;


import jakarta.persistence.*;
import jpolanco.springbootapp.event.domain.model.EventStatus;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Instant schedule;

    @Column(nullable = false)
    private Long durationInSeconds;

    @Column(nullable = false)
    private Instant createdAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserEntity creator;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(nullable = false)
    private String picture_path;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_categories",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<CategoryEntity> categories;


    @OneToOne(fetch = FetchType.LAZY)
    private PreferencesEntity preferences;

    @OneToOne(fetch = FetchType.LAZY)
    private LocationEntity location;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffEntity> staff = new ArrayList<>();

}
