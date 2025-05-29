package jpolanco.springbootapp.event.infrastructure.adapters.output.persistence;

import jakarta.persistence.*;
import jpolanco.springbootapp.event.domain.model.Modality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "event_preferences")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreferencesEntity {
    @Id
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Modality modality;

    @Column(nullable = false)
    private boolean enableComments;

}
