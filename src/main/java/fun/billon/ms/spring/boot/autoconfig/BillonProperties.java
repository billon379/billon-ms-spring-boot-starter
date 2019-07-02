package fun.billon.ms.spring.boot.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "billon")
public class BillonProperties {

    private boolean messageConvertFilterNull;

    private TokenAdviceProperties tokenAdviceProperties;

    @Data
    @ConfigurationProperties(prefix = "billon.advice.token")
    public static class TokenAdviceProperties {

        private List<String> excludedUris;

        /**
         * 当前uri是否匹配要过滤的uri
         *
         * @param uri 当前uri
         * @return 当前uri是否匹配要过滤的uri
         */
        public boolean matches(String uri) {
            for (String excludedUri : excludedUris) {
                if (uri.matches(excludedUri)) {
                    return true;
                }
            }
            return false;
        }
    }

}