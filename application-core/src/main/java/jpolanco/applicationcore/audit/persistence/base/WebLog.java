package jpolanco.applicationcore.audit.persistence.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class WebLog extends BaseLog {
    private String ip;
    private String userAgent;
}
