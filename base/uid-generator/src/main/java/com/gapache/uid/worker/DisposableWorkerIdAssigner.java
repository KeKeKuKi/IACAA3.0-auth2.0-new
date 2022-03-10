package com.gapache.uid.worker;

import lombok.extern.slf4j.Slf4j;

/**
 * Represents an implementation of {@link WorkerIdAssigner},
 * the worker id will be discarded after assigned to the UidGenerator
 *
 * @author HuSen
 * create on 2020/1/9 17:22
 */
@Slf4j
public class DisposableWorkerIdAssigner implements WorkerIdAssigner {

    @Override
    public long assignWorkerId() {
        return 0;
    }
}
