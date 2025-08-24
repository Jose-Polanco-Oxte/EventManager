package jpolanco.applicationcore.audit.persistence;

import jpolanco.applicationcore.audit.persistence.base.WebLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "data_change_logs")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias("DATA_CHANGE_LOG")
public class DataChangeLog extends WebLog {
    private String userId;
    private String entityId;
    private String entity;
    private String action;
    private Map<String, Map<String, Object>> changes;
}