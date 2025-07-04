package jpolanco.springbootapp.shared.infrastructure.auditory.persistence;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auditory_log", indexes = {
        @Index(name = "idx_auditory_log_type", columnList = "type"),
        @Index(name = "idx_auditory_log_timestamp", columnList = "timeStamp"),
        @Index(name = "idx_auditory_log_uuid", columnList = "UUID")
})
public class AuditoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID UUID;

    private String type;

    @Lob
    private String payload;

    private Instant timeStamp;

    public AuditoryLog(String type, String payload, Instant timeStamp) {
        this.type = type;
        this.payload = payload;
        this.timeStamp = timeStamp;
    }

    public AuditoryLog() {
    }

    @PrePersist
    public void prePersist() {
        if (this.UUID == null) {
            this.UUID = java.util.UUID.randomUUID();
        }
        if (this.timeStamp == null) {
            this.timeStamp = Instant.now();
        }
    }
}
