package org.hj.chain.platform.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.hj.chain.platform.annotation.CustomParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-12
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-12
 */
@Slf4j
public class CustomMethodArgumentResolver implements HandlerMethodArgumentResolver{
    private static final String POST = "post";
    private static final String APPLICATION_JSON = "application/json";

    /**
     * 判断是否需要处理该参数
     *
     * @param parameter the method parameter to check
     * @return {@code true} if this resolver supports the supplied parameter;
     * {@code false} otherwise
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 只处理带有@CustomParam注解的参数
        return parameter.hasParameterAnnotation(CustomParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String contentType = Objects.requireNonNull(servletRequest).getContentType();

        if (contentType == null || !contentType.contains(APPLICATION_JSON)) {
            log.error("解析参数异常，contentType需为{}", APPLICATION_JSON);
            throw new RuntimeException("解析参数异常，contentType需为application/json");
        }

        if (!POST.equalsIgnoreCase(servletRequest.getMethod())) {
            log.error("解析参数异常，请求类型必须为post");
            throw new RuntimeException("解析参数异常，请求类型必须为post");
        }
        return bindRequestParams(parameter, servletRequest);
    }

    private Object bindRequestParams(MethodParameter parameter, HttpServletRequest servletRequest) {
        CustomParam customParam = parameter.getParameterAnnotation(CustomParam.class);

        Class<?> parameterType = parameter.getParameterType();
        String requestBody = getRequestBody(servletRequest);
        Map<String, Object> params = JSON.parseObject(requestBody, new TypeReference<Map<String, Object>>(){});
        params = params == null || params.isEmpty()? new HashMap<>(0) : params;
        String name = StrUtil.isBlank(customParam.value()) ? parameter.getParameterName() : customParam.value();
        Object value = params.get(name);

        if (parameterType.equals(String.class)) {
            if (StrUtil.isBlank((String) value)) {
                log.error("参数解析异常,String类型参数不能为空");
                throw new RuntimeException("参数解析异常,String类型参数不能为空");
            }
        }

        if (customParam.required()) {
            if (value == null) {
                log.error("参数解析异常,require=true,值不能为空");
                throw new RuntimeException("参数解析异常,require=true,值不能为空");
            }
        } else {
            if (customParam.defaultValue().equals(ValueConstants.DEFAULT_NONE)) {
                log.error("参数解析异常,require=false,必须指定默认值");
                throw new RuntimeException("参数解析异常,require=false,必须指定默认值");
            }
            if (value == null) {
                value = customParam.defaultValue();
            }
        }

        return ConvertUtils.convert(value, parameterType);
    }

    /**
     * 获取请求body
     *
     * @param servletRequest request
     * @return 请求body
     */
    private String getRequestBody(HttpServletRequest servletRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = servletRequest.getReader();
            char[] buf = new char[1024];
            int length;
            while ((length = reader.read(buf)) != -1) {
                stringBuilder.append(buf, 0, length);
            }
        } catch (IOException e) {
            log.error("读取流异常", e);
            throw new RuntimeException("读取流异常");
        }
        return stringBuilder.toString();
    }
}
