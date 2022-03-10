package com.gapache.uid.worker;

import com.gapache.uid.utils.ValuedEnum;

/**
 * WorkerNodeType
 * <li>CONTAINER: Such as Docker
 * <li>ACTUAL: Actual machine
 *
 * @author HuSen
 * create on 2020/1/9 17:41
 */
public enum WorkerNodeType implements ValuedEnum<Integer> {

    CONTAINER(1), ACTUAL(2);

    /**
     * Lock type
     */
    private final Integer type;

    WorkerNodeType(Integer type) {
        this.type = type;
    }

    @Override
    public Integer value() {
        return this.type;
    }
}
