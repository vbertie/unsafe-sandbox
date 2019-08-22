package com.unsafe.serialization;

import java.io.*;

import static com.unsafe.serialization.SerializationExample.REPETITIONS;

public class StandardJavaSerializer {

    private final ByteArrayOutputStream baos;

    public StandardJavaSerializer() {
        this.baos =  new ByteArrayOutputStream();
    }

    public ObjectToSerialize read() throws IOException, ClassNotFoundException {
        ObjectToSerialize object = null;
        for (int i = 0; i < REPETITIONS; i++) {
            ByteArrayInputStream bais =
                    new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            object = (ObjectToSerialize) ois.readObject();
        }
        return object;
    }

    public void write(final ObjectToSerialize objectToSerialize) throws IOException {
        for (int i = 0; i < REPETITIONS; i++) {
            baos.reset();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(objectToSerialize);
            oos.close();
        }
    }

    public ObjectToSerialize[] readArray(final ObjectToSerialize[] items) throws IOException, ClassNotFoundException {
        final ObjectToSerialize[] deserialized = new ObjectToSerialize[items.length];
        final ByteArrayInputStream bais =
                new ByteArrayInputStream(baos.toByteArray());
        final ObjectInputStream ois = new ObjectInputStream(bais);
        for (int i = 0; i < items.length; i++) {
            deserialized[i] = (ObjectToSerialize) ois.readObject();
        }
        bais.close();
        ois.close();
        return deserialized;
    }

    public void writeArray(final ObjectToSerialize[] items) throws IOException {
        baos.reset();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        for (int i = 0; i < items.length; i++) {
            oos.writeObject(items[i]);
        }
        oos.close();
    }
}
