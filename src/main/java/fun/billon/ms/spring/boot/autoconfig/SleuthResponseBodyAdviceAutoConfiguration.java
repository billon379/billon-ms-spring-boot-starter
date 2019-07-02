package fun.billon.ms.spring.boot.autoconfig;

import brave.Tracer;
import fun.billon.ms.spring.boot.controller.advice.SleuthResponseBodyAdvice;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 对Controller的返回结果做二次处理。将sleuth traceId加入到返回结果中
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@ConditionalOnBean(Tracer.class)
@AutoConfigureOrder(100)
@ConditionalOnProperty(value = "billon.advice.sleuth.enabled", matchIfMissing = true)
@Import(SleuthResponseBodyAdvice.class)
public class SleuthResponseBodyAdviceAutoConfiguration {

}