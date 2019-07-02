package fun.billon.ms.spring.boot.controller.advice;

import fun.billon.common.model.ResultModel;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

/**
 * 对Controller的返回结果做二次处理。如果token过期，则修改resultCode的返回码为token过期:(10005)
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@ControllerAdvice
public class GlobalErrorResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final String ERROR_URI = "/error";

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //返回true表示需要对数据进行处理
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object t, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        /*
         * 拦截uri"/error"
         */
        String requestURI = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getRequestURI();
        if (ERROR_URI.matches(requestURI)) {
            ResultModel<Map<String, Object>> resultModel = new ResultModel<>();
            resultModel.setFailed(ResultModel.RESULT_ERROR, t.toString());
            return resultModel;
        }
        return t;
    }

}