package com.gapache.uid.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.gapache.uid.UidGenerator;
import com.gapache.uid.annotation.EnableUidGenerator;
import com.gapache.uid.impl.CachedUidGenerator;
import com.gapache.uid.impl.DefaultUidGenerator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * @author HuSen
 * create on 2020/1/10 15:09
 */
@Slf4j
@Data
@ConditionalOnBean(annotation = EnableUidGenerator.class)
public class UidGeneratorAutoConfiguration implements InitializingBean {

    @NacosValue(value = "${uid.timeBits:0}")
    private int timeBits;

    @NacosValue(value = "${uid.workerBits:0}")
    private int workerBits;

    @NacosValue(value = "${uid.seqBits:0}")
    private int seqBits;

    @NacosValue(value = "${uid.epochStr}")
    private String epochStr;

    @NacosValue(value = "${uid.workerId:0}")
    private long workerId;

    @NacosValue(value = "${uid.useCache:false}")
    private boolean useCache;

    @Bean
    public UidGenerator uidGenerator() {
        DefaultUidGenerator uidGenerator = this.useCache ? new CachedUidGenerator() : new DefaultUidGenerator();
        uidGenerator.setEpochStr(this.epochStr);
        uidGenerator.setSeqBits(this.seqBits);
        uidGenerator.setTimeBits(this.timeBits);
        uidGenerator.setWorkerBits(this.workerBits);
        uidGenerator.setWorkerIdAssigner(() -> this.workerId);
        return uidGenerator;
    }

    @Override
    public void afterPropertiesSet() {
        log.info("load CachedUidGeneratorAutoConfiguration:{}", this);
    }
}
