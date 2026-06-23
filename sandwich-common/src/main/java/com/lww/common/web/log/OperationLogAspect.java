package com.lww.common.web.log;

import com.alibaba.fastjson.JSON;
import com.lww.common.utils.ServletRequestUtil;
import com.lww.common.web.log.entity.OperationLog;
import com.lww.common.web.log.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author lww
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private static final SpelExpressionParser SPEL_PARSER = new SpelExpressionParser();
    private static final String JWT_CLASS_NAME = "org.springframework.security.oauth2.jwt.Jwt";

    private final Ip2regionSearcher regionSearcher;

    private final OperationLogService operationLogService;

    @Around("@annotation(loggable)")
    public Object logOperation(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = method.getName();

        HttpServletRequest request = resolveRequest();
        String ipAddress = ServletRequestUtil.getClientIp(request);
        String region = ipAddress != null ? regionSearcher.getAddress(ipAddress) : null;
        String requestUri = request != null ? request.getRequestURI() : null;
        OperatorInfo operatorInfo = resolveOperatorInfo();
        LocalDateTime startTime = LocalDateTime.now();
        OperationLog logContext = OperationLog.builder()
                .module(loggable.module())
                .type(loggable.type())
                .description(resolveSpel(loggable.description(), method, joinPoint.getArgs()))
                .method(methodName)
                .params(loggable.logParams() ? JSON.toJSONString(joinPoint.getArgs()) : null)
                .operatorId(operatorInfo != null ? operatorInfo.userId() : null)
                .operatorName(operatorInfo != null ? operatorInfo.userName() : null)
                .requestUri(requestUri)
                .startTime(startTime)
                .requestIp(ipAddress)
                .requestRegion(region)
                .createTime(startTime)
                .build();

        try {
            Object result = joinPoint.proceed();
            
            logContext.setResult(loggable.logResult() ? JSON.toJSONString(result) : null);
            logContext.setDuration(Duration.between(logContext.getStartTime(), LocalDateTime.now()).toMillis());

            if (loggable.async()) {
                asyncSaveLog(logContext);
            } else {
                saveLog(logContext);
            }

            return result;
        } catch (Exception e) {
            logContext.setDuration(Duration.between(logContext.getStartTime(), LocalDateTime.now()).toMillis());
            saveLog(logContext);
            throw e;
        }
    }

    private String resolveSpel(String expression, Method method, Object[] args) {
        if (expression == null || !expression.contains("#")) {
            return expression;
        }
        
        try {
            EvaluationContext context = new StandardEvaluationContext();
            ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
            String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
            
            if (paramNames != null) {
                for (int i = 0; i < args.length; i++) {
                    context.setVariable(paramNames[i], args[i]);
                }
            }
            
            Expression spelExpr = SPEL_PARSER.parseExpression(expression);
            return spelExpr.getValue(context, String.class);
        } catch (Exception e) {
            log.warn("解析SpEL表达式失败: {}, {}", expression, e.getMessage());
            return expression;
        }
    }

    @Async
    public void asyncSaveLog(OperationLog logContext) {
        saveLog(logContext);
    }

    private void saveLog(OperationLog logContext) {
        log.info("[操作日志] {} - {} | 耗时: {}ms | 参数: {} | 结果: {} ",
                logContext.getModule(),
                logContext.getDescription(),
                logContext.getDuration(),
                logContext.getParams(),
                logContext.getResult());
        operationLogService.save(logContext);
    }

    private HttpServletRequest resolveRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (!(ra instanceof ServletRequestAttributes sra)) {
            return null;
        }
        return sra.getRequest();
    }

    private OperatorInfo resolveOperatorInfo() {
        try {
            Class<?> securityContextHolder = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            Object context = securityContextHolder.getMethod("getContext").invoke(null);
            if (context == null) {
                return null;
            }
            Object authentication = context.getClass().getMethod("getAuthentication").invoke(context);
            if (authentication == null) {
                return null;
            }
            Object principal = authentication.getClass().getMethod("getPrincipal").invoke(authentication);
            if (principal == null) {
                return null;
            }
            if (!JWT_CLASS_NAME.equals(principal.getClass().getName())) {
                return null;
            }
            Object userIdValue = principal.getClass().getMethod("getClaim", String.class).invoke(principal, "userId");
            Object userNameValue = principal.getClass().getMethod("getClaim", String.class).invoke(principal, "userName");
            Long userId = userIdValue instanceof Number ? ((Number) userIdValue).longValue() : null;
            String userName = userNameValue != null ? String.valueOf(userNameValue) : null;
            if (userId == null && (userName == null || userName.isBlank())) {
                return null;
            }
            return new OperatorInfo(userId, userName);
        } catch (Exception ignored) {
            return null;
        }
    }

    private record OperatorInfo(Long userId, String userName) {
    }
}
