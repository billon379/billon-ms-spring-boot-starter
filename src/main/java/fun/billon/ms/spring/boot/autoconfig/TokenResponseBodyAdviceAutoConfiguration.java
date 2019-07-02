package fun.billon.ms.spring.boot.autoconfig;

import fun.billon.ms.spring.boot.controller.advice.TokenResponseBodyAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 对Controller的返回结果做二次处理。如果token过期，则修改resultCode的返回码为token过期:(10005)
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(value = "billon.advice.token.enabled")
@EnableConfigurationProperties({BillonProperties.TokenAdviceProperties.class})
@Import(TokenResponseBodyAdvice.class)
public class TokenResponseBodyAdviceAutoConfiguration {

}