package jpolanco.springbootapp.event.infrastructure.adapters.output.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "event_locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationEntity {

    @Id
    private UUID id;

    @Column()
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;
}
