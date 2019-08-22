package com.unsafe.serialization;

import com.unsafe.util.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static com.unsafe.serialization.SerializationExample.REPETITIONS;

public class DirectByteBufferSerializer {

    private static final ByteBuffer dbb = ByteBuffer.allocateDirect(1024 * 1024 * 256).order(ByteOrder.nativeOrder());

    public void writeArray(ObjectToSerialize[] objectsInput) {
        dbb.clear();
        for (ObjectToSerialize item : objectsInput) {
            ByteBufferUtil.serialize(item, dbb);
        }
    }

    public ObjectToSerialize[] readArray(ObjectToSerialize[] objectsInput) {
        ObjectToSerialize[] deserialized = new ObjectToSerialize[objectsInput.length];
        dbb.flip();
        for (int i = 0; i < deserialized.length; i++) {
            deserialized[i] = ByteBufferUtil.deserialize(dbb);
        }
        return deserialized;
    }

    public ObjectToSerialize read() {
        ObjectToSerialize objectToSerialize = null;
        for (int i = 0; i < REPETITIONS; i++) {
            dbb.flip();
            objectToSerialize = ByteBufferUtil.deserialize(dbb);
        }
        return objectToSerialize;
    }

    public void write(ObjectToSerialize input) {
        for (int i = 0; i < REPETITIONS; i++) {
            dbb.clear();
            ByteBufferUtil.serialize(input, dbb);
        }
    }
}
