package com.gapache.commons.model;

import lombok.Data;

/**
 * @author HuSen
 * create on 2020/4/5 19:52
 */
@Data
public class ITuple<L, R> {
    private L left;
    private R right;
}
