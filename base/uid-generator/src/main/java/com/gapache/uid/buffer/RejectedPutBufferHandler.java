package com.gapache.uid.buffer;

/**
 * if tail catches the cursor it means that the ring buffer is full, any more buffer put request will be rejected.
 * Specify the policy to handle the reject. This is a Lambda supported interface
 *
 * @author HuSen
 * create on 2020/1/9 15:25
 */
@FunctionalInterface
public interface RejectedPutBufferHandler {

    /**
     * Reject put buffer request
     *
     * @param ringBuffer RingBuffer
     * @param uid        uid
     */
    void rejectPutBuffer(RingBuffer ringBuffer, long uid);
}
