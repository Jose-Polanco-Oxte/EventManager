package jpolanco.applicationcore.audit.persistence;

import jpolanco.applicationcore.audit.persistence.base.WebLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "action_logs")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias("ACTION_LOG")
public class ActionLog extends WebLog {
    private String service;
    private String action;
    private Map<String, Object> context;
}
