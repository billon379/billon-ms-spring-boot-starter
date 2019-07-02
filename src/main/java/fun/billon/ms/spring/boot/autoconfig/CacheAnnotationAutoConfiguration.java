package fun.billon.ms.spring.boot.autoconfig;

import fun.billon.common.cache.RedisAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 缓存切面配置
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Aspect
@Configuration
@ConditionalOnBean(StringRedisTemplate.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class CacheAnnotationAutoConfiguration {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * redis缓存aop操作类
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisAspect redisAspect(StringRedisTemplate stringRedisTemplate) {
        RedisAspect redisAspect = new RedisAspect();
        redisAspect.setRedisTemplate(stringRedisTemplate);
        return redisAspect;
    }

    /**
     * aop切入点
     */
    @Around("@annotation(fun.billon.common.cache.annotation.Cacheable) " +
            "|| @annotation(fun.billon.common.cache.annotation.CacheEvict)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return redisAspect(stringRedisTemplate).around(joinPoint);
    }

}