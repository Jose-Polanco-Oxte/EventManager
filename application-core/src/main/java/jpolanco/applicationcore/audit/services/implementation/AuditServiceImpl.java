package jpolanco.applicationcore.audit.services.implementation;

import jpolanco.applicationcore.audit.persistence.ActionLog;
import jpolanco.applicationcore.audit.persistence.ErrorLog;
import jpolanco.applicationcore.audit.persistence.base.BaseLog;
import jpolanco.applicationcore.audit.persistence.base.WebLog;
import jpolanco.applicationcore.audit.repository.LogRepository;
import jpolanco.applicationcore.audit.services.interfaces.AuditService;
import jpolanco.applicationcore.shared.application.exceptions.ApplicationExceptionHandler;
import jpolanco.applicationcore.shared.application.exceptions.DomainExceptionHandler;
import jpolanco.applicationcore.shared.infrastructure.context.ThreadLocalContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final LogRepository logRepository;
    private final ThreadLocalContext threadLocalContext;

    @Override
    public <T extends BaseLog> void saveLog(T log) {
        log.setTransactionId(threadLocalContext.get("transactionId", String.class).orElse("NO_TX_ID"));
        logRepository.save(log);
    }

    @Override
    public void deleteLog(String id) {

    }

    @Override
    public <T extends WebLog> void saveWebLog(T log) {
        log.setTransactionId(threadLocalContext.get("transactionId", String.class).orElse("NO_TX_ID"));
        log.setIp(threadLocalContext.get("ip", String.class).orElse("NO_IP"));
        log.setUserAgent(threadLocalContext.get("userAgent", String.class).orElse("NO_USER_AGENT"));
        logRepository.save(log);
    }

    @Override
    public void auditError(String action, ProceedingJoinPoint joinPoint, Throwable e) {
        if (e == null) {
            return;
        }
        ErrorLog errorLog = new ErrorLog();
        errorLog.setService(joinPoint.getSignature().getDeclaringType().getSimpleName());
        if (e instanceof DomainExceptionHandler) {
            errorLog.setLevel("FATAL");
        } else if (e instanceof ApplicationExceptionHandler) {
            errorLog.setLevel("ERROR");
        } else {
            errorLog.setLevel("WARNING");
        }
        errorLog.setAction(action);
        errorLog.setMessage(e.getMessage());
        errorLog.setStackTrace(getStackTrace(e));

        Map<String, Object> context = new HashMap<>();
        context.put("class", joinPoint.getSignature().getDeclaringTypeName());
        context.put("method", joinPoint.getSignature().getName());
        context.put("args", getArguments(joinPoint));
        errorLog.setContext(context);
        saveLog(errorLog);
    }

    private String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Extracts method arguments from a ProceedingJoinPoint and returns them as a map.
     *
     * @param joinPoint the join point from which to extract arguments
     * @return a map of argument names to their corresponding values
     */
    private Map<String, Object> getArguments(ProceedingJoinPoint joinPoint) {
        Map<String, Object> argsMap = new HashMap<>();
        Object[] args = joinPoint.getArgs(); // Get method arguments
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames(); // Get argument names
        if (argNames != null && argNames.length > 0) {
            // Map argument names to their values
            for (int i = 0; i < args.length; i++) {
                argsMap.put(argNames[i], args[i]);
            }
        }
        return argsMap;
    }

    @Override
    public void auditAction(String action, ProceedingJoinPoint joinPoint, String serializedResultPayload) {
        ActionLog actionLog = new ActionLog();
        actionLog.setService(joinPoint.getSignature().getDeclaringType().getSimpleName());
        actionLog.setAction(action);
        Map<String, Object> context = new HashMap<>();
        context.put("class", joinPoint.getSignature().getDeclaringTypeName());
        context.put("method", joinPoint.getSignature().getName());
        context.put("args", getArguments(joinPoint));
        context.put("result", serializedResultPayload);
        actionLog.setContext(context);
        saveWebLog(actionLog);
    }
}
