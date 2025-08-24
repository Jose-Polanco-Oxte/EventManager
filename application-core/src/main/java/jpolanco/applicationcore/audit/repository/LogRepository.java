package jpolanco.applicationcore.audit.repository;

import jpolanco.applicationcore.audit.persistence.base.BaseLog;

public interface LogRepository {
    /**
     * Save a log entry to the repository.
     *
     * @param log the log entry to save
     * @param <T> the type of the log entry, extending BaseLog
     */
    <T extends BaseLog> void save(T log);
}
