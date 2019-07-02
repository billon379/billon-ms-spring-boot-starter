package fun.billon.ms.spring.boot.autoconfig;

import fun.billon.ms.spring.boot.controller.advice.GlobalErrorResponseBodyAdvice;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 对Controller的返回结果做二次处理。处理全局异常
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@Import(GlobalErrorResponseBodyAdvice.class)
public class GlobalErrorControllerAutoConfiguration {

}