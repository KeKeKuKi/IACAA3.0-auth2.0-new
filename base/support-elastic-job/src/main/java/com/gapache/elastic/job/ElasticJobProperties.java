package com.gapache.elastic.job;

import lombok.Data;

/**
 * @author HuSen
 * @since 2020/8/17 10:37 上午
 */
@Data
public class ElasticJobProperties {

    private Zookeeper zookeeper;

    @Data
    public static class Zookeeper {
        private String serverLists;
        private String namespace;
    }
}
