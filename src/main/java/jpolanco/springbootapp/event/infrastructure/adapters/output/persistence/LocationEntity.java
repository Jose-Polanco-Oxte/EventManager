package jpolanco.springbootapp.event.infrastructure.adapters.output.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;
}
