package jpolanco.applicationcore.audit.utils;

import java.util.Map;

public class AuditAux {
    // Utility method to build a change with before and after values
    public static Map<String, Map<String, Object>> buildChange(String field, Object before, Object after) {
        return Map.of(field, Map.of("before", before, "after", after));
    }

    // Overloaded method to include a reason for the change
    public static Map<String, Map<String, Object>> buildChange(String field, Object before, Object after, String reason) {
        return Map.of(field, Map.of("before", before, "after", after, "reason", reason));
    }
}
