package jpolanco.applicationcore.audit.services.interfaces;

import jpolanco.applicationcore.audit.persistence.base.BaseLog;
import jpolanco.applicationcore.audit.persistence.base.WebLog;
import org.aspectj.lang.ProceedingJoinPoint;

public interface AuditService {
    /**
     * Saves a log entry to the repository, enriching it with the current transaction ID from the thread-local context.
     * @param log the log entry to be saved
     * @param <T> the type of the log entry, extending BaseLog
     */
    <T extends BaseLog> void saveLog(T log);

    /**
     * Deletes a log entry by its ID. (Method currently not implemented)
     * @param id the ID of the log entry to be deleted
     * TODO: Implement deleteLog method
     */
    void deleteLog(String id);

    /**
     * Saves a web log entry to the repository, enriching it with the current transaction ID, IP address,
     * and user agent from the thread-local context.
     * @param log the web log entry to be saved
     * @param <T> the type of the web log entry, extending WebLog
     */
    <T extends WebLog> void saveWebLog(T log);

    void auditError(String action, ProceedingJoinPoint joinPoint, Throwable e);

    /**
     * Audits an action by creating an ActionLog entry with details from the provided join point and serialized result payload.
     * @param action a description of the action being audited
     * @param joinPoint the join point where the action occurred
     * @param serializedResultPayload the serialized result of the action
     */
    void auditAction(String action, ProceedingJoinPoint joinPoint, String serializedResultPayload);
}
