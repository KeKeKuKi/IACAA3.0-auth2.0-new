package com.gapache.uid.buffer;

/**
 * If cursor catches the tail it means that the ring buffer is empty, any more buffer take request will be rejected.
 * Specify the policy to handle the reject. This is a Lambda supported interfacea
 *
 * @author HuSen
 * create on 2020/1/9 15:26
 */
@FunctionalInterface
public interface RejectedTakeBufferHandler {

    /**
     * Reject take buffer request
     *
     * @param ringBuffer RingBuffer
     */
    void rejectTakeBuffer(RingBuffer ringBuffer);
}
