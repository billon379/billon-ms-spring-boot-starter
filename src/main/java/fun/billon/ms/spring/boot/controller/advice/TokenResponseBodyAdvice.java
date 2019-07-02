package fun.billon.ms.spring.boot.controller.advice;

import fun.billon.common.model.ResultModel;
import fun.billon.ms.spring.boot.autoconfig.BillonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 对Controller的返回结果做二次处理。如果token过期，则修改resultCode的返回码为token过期:(10005)
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@ControllerAdvice
public class TokenResponseBodyAdvice<T> implements ResponseBodyAdvice<T> {

    @Autowired
    private BillonProperties.TokenAdviceProperties tokenAdviceProperties;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //返回true表示需要对数据进行处理
        return true;
    }

    @Override
    public T beforeBodyWrite(T t, MethodParameter methodParameter, MediaType mediaType,
                             Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                             ServerHttpResponse serverHttpResponse) {
        /*
         * 当返回类型不是ResultModel时(如:actuator日志级别动态调整返回结果是LinkedHashMap)不做处理，
         */
        if (!(t instanceof ResultModel)) {
            return t;
        }

        /*
         * 返回结果是ResultModel,说明是我们自己的rest服务返回结果
         */
        ResultModel resultModel = (ResultModel) t;
        Object tokenStatus = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getAttribute("token_status");
        String requestURI = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getRequestURI();
        if (null != tokenStatus && !tokenAdviceProperties.matches(requestURI)) {
            // 从reques中获取token_status的值,存在且resultModel的code是0的话设置到resultModel的code中
            if (resultModel.getCode() == ResultModel.RESULT_SUCCESS) {
                resultModel.setCode(Integer.parseInt(tokenStatus.toString()));
            }
        }
        return t;
    }

}