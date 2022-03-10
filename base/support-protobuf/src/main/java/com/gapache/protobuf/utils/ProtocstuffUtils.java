package com.gapache.protobuf.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * @author HuSen
 */
public class ProtocstuffUtils {

    public static <T> T byte2Bean(byte[] data, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T bean = null;
        try {
            bean = schema.newMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data != null) {
            ProtobufIOUtil.mergeFrom(data, bean, schema);
        }

        return bean;
    }

    public static <T> byte[] bean2Byte(T bean, Class<T> clazz) {
        try {
            LinkedBuffer buffer = getApplicationBuffer();
            Schema<T> schema = RuntimeSchema.getSchema(clazz);

            return ProtobufIOUtil.toByteArray(bean, schema, buffer);
        } catch (Exception e) {
            System.err.println("catch exception on bean2Byte. class " + clazz.getName());
            return null;
        }
    }

    static final int BUFFER_SIZE = 2048;

    static final ThreadLocal<LinkedBuffer> LOCAL_BUFFER = ThreadLocal.withInitial(() -> LinkedBuffer.allocate(BUFFER_SIZE));

    public static LinkedBuffer getApplicationBuffer() {
        return LOCAL_BUFFER.get().clear();
    }
}
