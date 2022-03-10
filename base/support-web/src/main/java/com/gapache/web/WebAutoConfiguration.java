package com.gapache.web;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gapache.commons.model.SecurityException;
import com.gapache.commons.model.*;
import com.gapache.commons.utils.ContextUtils;
import com.gapache.web.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/1/14 17:54
 */
@Configuration
public class WebAutoConfiguration {

    @Slf4j
    @RestControllerAdvice
    public static class Advice {

        @ExceptionHandler(Exception.class)
        public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Throwable {
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "application/json;charset=utf-8");
            response.setContentType("application/json;charset=utf-8");

            JsonResult<String> jsonResult;
            if (e instanceof BusinessException) {
                BusinessException iE = (BusinessException) e;
                log.error("{}.{}业务错误:", request.getMethod(), request.getRequestURI(), e);
                jsonResult = JsonResult.of(iE.getError());
            } else if (e instanceof ParamException) {
                ParamException pE = (ParamException) e;
                StringBuilder error = new StringBuilder();
                pE.getErrors().forEach((x, y) -> error.append(x).append(":").append(y.toString()).append(";"));
                jsonResult = JsonResult.of(CommonError.PARAM_ERROR.getCode(), error.substring(0, error.length() - 1));
            } else if (e instanceof SecurityException) {
                log.error("{}.{}禁止访问:", request.getMethod(), request.getRequestURI(), e);
                SecurityException securityException = (SecurityException) e;
                jsonResult = JsonResult.of(securityException.getError());
            } else {
                response.setStatus(500);
                if (e.getCause() != null) {
                    String name = e.getCause().getClass().getName();
                    if (name.equals(FlowException.class.getName())) {
                        jsonResult = JsonResult.of(SystemError.SERVER_FLOW);
                    } else if (name.equals(DegradeException.class.getName())) {
                        jsonResult = JsonResult.of(SystemError.SERVER_DEGRADE);
                    } else if (name.equals(ParamFlowException.class.getName())) {
                        jsonResult = JsonResult.of(SystemError.SERVER_PARAM_FLOW);
                    } else if (name.equals(SystemBlockException.class.getName())) {
                        jsonResult = JsonResult.of(SystemError.SERVER_SYSTEM_BLOCK);
                    } else if (name.equals(AuthorityException.class.getName())) {
                        jsonResult = JsonResult.of(SystemError.SERVER_AUTHORITY);
                    } else {
                        log.error("{}.{}发生未知异常:", request.getMethod(), request.getRequestURI(), e);
                        jsonResult = JsonResult.of(SystemError.SERVER_EXCEPTION);
                    }
                } else {
                    log.error("{}.{}发生未知异常:", request.getMethod(), request.getRequestURI(), e);
                    throw e;
                }
            }

            new ObjectMapper()
                    .writeValue(response.getWriter(), jsonResult);
        }
    }

    @Aspect
    @Slf4j
    @Configuration
    public static class AutoValidation {

        @Pointcut("@within(Validating)")
        public void pointCut() {}

        @Before("pointCut()")
        public void validating(JoinPoint pjp) {
            Map<String, StringBuilder> result = new HashMap<>(16);
            Object[] args = pjp.getArgs();
            Signature signature = pjp.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            Parameter[] parameters = method.getParameters();
            int length = parameters.length;
            for (int i = 0; i < length; i++) {
                Parameter parameter = parameters[i];
                Object arg = args[i];
                if (parameter.isAnnotationPresent(Check.class)) {
                    ValidatorUtil.validate(arg)
                            .then(errors -> {
                                if (errors != null) {
                                    errors.forEach((f, e) -> log.error("参数:{}, 错误:{}", f, e));
                                }
                            })
                            .then(errors -> {
                                if (errors != null) {
                                    result.putAll(errors);
                                }
                            })
                            .end();
                }
            }
            if (!result.isEmpty()) {
                throw new ParamException(result);
            }
        }
    }

    @Bean
    public ContextUtils contextUtils() {
        return new ContextUtils();
    }
}
