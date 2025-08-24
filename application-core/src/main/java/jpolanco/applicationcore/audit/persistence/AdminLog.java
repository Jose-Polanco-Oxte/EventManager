package jpolanco.applicationcore.audit.persistence;

import jpolanco.applicationcore.audit.persistence.base.BaseLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "admin_logs")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@TypeAlias("ADMINISTRATION_LOG")
public class AdminLog extends BaseLog {
    private String userId;
    private String action;
    private String ip;
    private String targetId;
    private String reason;
}
