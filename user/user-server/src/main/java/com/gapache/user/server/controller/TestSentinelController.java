package com.gapache.user.server.controller;

//import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * @since 2021/3/11 10:25 上午
 */
@RestController
@RequestMapping("/test")
public class TestSentinelController {

    @GetMapping("/flow")
    public JsonResult<String> flow() {
        return JsonResult.of("这是测试限流的接口");
    }

    @GetMapping("/paramFlow")
//    @SentinelResource("paramFlow")
    public JsonResult<String> paramFlow(@RequestParam(required = false) String name) {
        return JsonResult.of("这是热点参数限流的接口" + name);
    }

    @GetMapping("/degrade")
//    @SentinelResource("degrade")
    public JsonResult<String> degrade(int a) {
        try {
            // 测试慢调用
            TimeUnit.SECONDS.sleep(a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return JsonResult.of("这是测试默认降级的接口");
    }

    @GetMapping("/degrade2")
    public JsonResult<String> degrade2(int a) {
        try {
            // 测试慢调用
            TimeUnit.SECONDS.sleep(a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return JsonResult.of("这是测试默认降级的接口2");
    }

    @GetMapping("/auth")
//    @SentinelResource("auth")
    public JsonResult<String> auth() {
        return JsonResult.of("这是测试授权规则的接口");
    }
}
