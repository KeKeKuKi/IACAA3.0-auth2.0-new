package com.gapache.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.SystemError;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Sentinel全局异常处理
 *
 * @author HuSen
 * @since 2021/3/11 9:24 上午
 */
@Slf4j
public class GlobalExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
        log.error("GlobalExceptionHandler.", e);
        // 配置url上的生效的时候会走这里 直接会对url生效，不需要执行方法就能进行处理
        // 配置在SentinelResource的资源名上的时候回走全局异常处理，这里会进行aop代理，所以是抛出全局异常
        JsonResult<Object> jsonResult;
        if (e instanceof FlowException) {
            // url可以生效
            jsonResult = JsonResult.of(SystemError.SERVER_FLOW);
        } else if (e instanceof DegradeException) {
            // url不能生效
            jsonResult = JsonResult.of(SystemError.SERVER_DEGRADE);
        } else if (e instanceof ParamFlowException) {
            // url不能生效
            jsonResult = JsonResult.of(SystemError.SERVER_PARAM_FLOW);
        } else if (e instanceof SystemBlockException) {
            jsonResult = JsonResult.of(SystemError.SERVER_SYSTEM_BLOCK);
        } else if (e instanceof AuthorityException) {
            // url可以生效
            jsonResult = JsonResult.of(SystemError.SERVER_AUTHORITY);
        } else {
            jsonResult = JsonResult.of(SystemError.SERVER_EXCEPTION);
        }

        // http状态码
        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");

        new ObjectMapper()
                .writeValue(httpServletResponse.getWriter(), jsonResult);
    }

}
