package com.gapache.sentinel;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;

/**
 * @author HuSen
 * @since 2021/3/16 1:53 下午
 */
public class DegradeRuleHelper {

    public static DegradeRule createDefault(String resource) {
        // 生成降级的策略
        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setLimitApp("default");
        // 资源名
        degradeRule.setResource(resource);
        // 熔断策略，支持慢调用比例/异常比例/异常数策略 慢调用比例
        degradeRule.setGrade(0);
        // 熔断时长，单位为 s
        degradeRule.setTimeWindow(60);
        // 慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用）；异常比例/异常数模式下为对应的阈值
        degradeRule.setCount(2000);
        // 慢调用比例阈值，仅慢调用比例模式有效（1.8.0 引入）
        degradeRule.setSlowRatioThreshold(0.8);
        // 统计时长
        degradeRule.setStatIntervalMs(1000 * 60);
        // 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断（1.7.0 引入） 5
        degradeRule.setMinRequestAmount(5);
        return degradeRule;
    }
}
