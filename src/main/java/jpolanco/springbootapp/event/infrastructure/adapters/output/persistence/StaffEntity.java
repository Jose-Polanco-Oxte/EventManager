package jpolanco.springbootapp.event.infrastructure.adapters.output.persistence;

import jakarta.persistence.*;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "event_staff")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private boolean AssistanceClerk;

    @OneToOne(fetch = FetchType.LAZY)
    private StaffRoleEntity role;
}
