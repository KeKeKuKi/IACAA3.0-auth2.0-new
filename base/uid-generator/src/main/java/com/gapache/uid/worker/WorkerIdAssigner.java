package com.gapache.uid.worker;

/**
 * Represents a worker id assigner for {@link com.gapache.uid.impl.DefaultUidGenerator}
 *
 * @author HuSen
 * create on 2020/1/9 17:06
 */
public interface WorkerIdAssigner {

    /**
     * Assign worker id for {@link com.gapache.uid.impl.DefaultUidGenerator}
     *
     * @return assigned worker id
     */
    long assignWorkerId();
}
