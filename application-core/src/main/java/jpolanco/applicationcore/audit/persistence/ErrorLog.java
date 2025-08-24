package jpolanco.applicationcore.audit.persistence;

import jpolanco.applicationcore.audit.persistence.base.BaseLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "error_logs")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias("ERROR_LOG")
public class ErrorLog extends BaseLog {
    private String service;
    private String action;
    private String level;
    private String message;
    private String stackTrace;
    private Map<String, Object> context;
}