package core.cache.def;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class ObjectSerialization {

    /**
     * Serializes the object.
     *
     * @param buffer The target.
     * @param object The object.
     */
    public static void putObject(ByteBuffer buffer, Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(object);
            }
            byte[] data = baos.toByteArray();
            buffer.putInt(data.length);
            buffer.put(data);
        } catch (Exception e) {
            e.printStackTrace();
            buffer.putInt(0);
        }
    }
}
