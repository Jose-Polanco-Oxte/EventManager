package jpolanco.applicationcore.audit.persistence;

import jpolanco.applicationcore.audit.persistence.base.WebLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "auth_logs")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias("AUTHENTICATION_LOG")
public class AuthLog extends WebLog {
    private String userId;
    private String action;
    private String email;
    private Map<String, String> details;
}