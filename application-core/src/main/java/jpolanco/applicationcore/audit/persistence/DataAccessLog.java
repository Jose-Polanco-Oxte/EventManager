package jpolanco.applicationcore.audit.persistence;

import jpolanco.applicationcore.audit.persistence.base.WebLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "data_access_logs")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@TypeAlias("DATA_ACCESS_LOG")
public class DataAccessLog extends WebLog {
    private String userId;
    private String action;
    private String resource;
    private Map<String, Object> filters;
}
