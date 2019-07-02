package fun.billon.ms.spring.boot.controller.advice;

import brave.Span;
import brave.Tracer;
import com.alibaba.fastjson.JSON;
import fun.billon.common.model.ResultModel;
import fun.billon.common.util.HttpRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 对Controller的返回结果做二次处理。将sleuth traceId加入到返回结果中
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@ControllerAdvice
public class SleuthResponseBodyAdvice<T> implements ResponseBodyAdvice<T> {

    @Autowired
    private Tracer tracer;

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
         * 1.将请求参数及返回结果加入span
         * 2.将sleuth的traceId加入到返回结果
         */
        ResultModel resultModel = (ResultModel) t;
        if (tracer != null && tracer.currentSpan() != null) {
            /*
             * 将请求参数及返回结果加入span
             */
            HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
            Span span = tracer.currentSpan();
            span.tag("http.request.headers", JSON.toJSONString(HttpRequestUtils.getRequestHeaders(httpServletRequest)));
            span.tag("http.request.params", JSON.toJSONString(HttpRequestUtils.getRequestParams(httpServletRequest)));
            span.tag("http.response.data", JSON.toJSONString(resultModel));

            // 将sleuth的traceId加入到返回结果
            resultModel.setTraceId(tracer.currentSpan().context().traceIdString());
        }
        return t;
    }

}