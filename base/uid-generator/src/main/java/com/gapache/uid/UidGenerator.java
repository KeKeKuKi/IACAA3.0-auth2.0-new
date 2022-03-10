package com.gapache.uid;

import com.gapache.uid.exception.UidGenerateException;

/**
 * Represents a unique id generator.
 *
 * @author HuSen
 * create on 2020/1/9 16:48
 */
public interface UidGenerator {

    /**
     * Get a unique ID
     *
     * @return UID
     * @throws UidGenerateException UidGenerateException
     */
    long getUID() throws UidGenerateException;

    /**
     * Parse the UID into elements which are used to generate the UID. <br>
     * Such as timestamp & workerId & sequence...
     *
     * @param uid UID
     * @return Parsed info
     */
    String parseUID(long uid);
}
