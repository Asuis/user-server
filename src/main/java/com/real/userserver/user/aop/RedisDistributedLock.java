package com.real.userserver.user.aop;

import com.google.common.base.Joiner;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author asuis
 */
@Aspect
@Configuration
public class RedisDistributedLock {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RedisDistributedLock.class);
    private final RedisTemplate<String,String> redisTemplate;

    @Autowired
    public RedisDistributedLock(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut("execution(* com.real.userserver.user.LoginService.*(..))")
    public void pointCut(){};

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable{
        Signature signature = point.getSignature();
        MethodSignature mothodSignature = (MethodSignature) signature;
        Method method = mothodSignature.getMethod();
        String targetName = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();

        if (method != null && method.isAnnotationPresent(RedisLockable.class)){
            RedisLockable redisLock = method.getAnnotation(RedisLockable.class);
            long expire = redisLock.expiration();
            String redisKey = getLockKey(targetName, methodName, redisLock.key(),args);
            String uuid = UUID.randomUUID().toString();
            boolean isLock = !lock(redisKey, uuid ,expire);
            if (!isLock) {
                try {
                    return point.proceed();
                } finally {
                    unlock(redisKey, uuid);
                }
            } else {
                throw new RuntimeException("频繁操作");
            }
        }
        return point.proceed();
    }

    private String getLockKey(String targetName, String methodName, String[] keys, Object[] arguments) {
        StringBuilder sb = new StringBuilder();
        sb.append("lock.").append(targetName).append(".").append(methodName);

        if(keys != null) {
            String keyStr = Joiner.on(".").skipNulls().join(keys);
            String[] parameters = ReflectParamNames.getNames(targetName, methodName);
            ExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(keyStr);
            EvaluationContext context = new StandardEvaluationContext();
            int length = parameters.length;
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    context.setVariable(parameters[i], arguments[i]);
                }
            }
            String keysValue = expression.getValue(context, String.class);
            sb.append("#").append(keysValue);
        }
        return sb.toString();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public boolean lock(String key,String value, long expire) {
        Boolean f = redisTemplate.execute((RedisCallback<Boolean>) connection ->
                connection.set(key.getBytes(),value.getBytes(),Expiration.from(expire,TimeUnit.SECONDS),RedisStringCommands.SetOption.ifAbsent()));
        if (f!=null) {
            return f;
        }
        return false;
    }
    /**
     * 解锁
     *
     * @param key
     * @param value
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void unlock(String key, String value) {
        try {
            String currentVlue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentVlue) && currentVlue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("【redis分布式锁】 解锁异常" + e.getMessage());
        }
    }
}
