package jpolanco.applicationcore.audit.persistence.base;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
public abstract class BaseLog {
    @Id
    private String id;
    @Field("timestamp")
    private Instant timestamp = Instant.now();

    @Field("transaction_id")
    private String transactionId;
}
