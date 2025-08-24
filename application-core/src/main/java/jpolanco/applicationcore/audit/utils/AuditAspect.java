package jpolanco.applicationcore.audit.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jpolanco.applicationcore.audit.services.interfaces.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    @Around("@annotation(audit)")
    public Object logAudit(ProceedingJoinPoint joinPoint, Audit audit) throws Throwable {
        Object result = null;
        Throwable exception = null;

        try {
            result = joinPoint.proceed(); // obtain the method result
            return result;
        } catch (Throwable e) { // catch any exception thrown by the method
            exception = e; // store the exception for auditing
            throw e; // rethrow the exception to maintain original behavior
        } finally {
            if (exception == null) {
                // Done without exception, audit success
                String resultPayload = null;
                try {
                    if (result != null) {
                        resultPayload = objectMapper.writeValueAsString(result);
                    }
                } catch (Exception serEx) {
                    // Handle serialization exception
                    auditService.auditError(audit.action(), joinPoint, serEx);
                }
                // Audit the successful action
                auditService.auditAction(audit.action(), joinPoint, resultPayload);
            } else {
                // An exception occurred, audit error
                auditService.auditError(audit.action(), joinPoint, exception);
            }
        }
    }
}

