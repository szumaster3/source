package core.cache.def.impl;

import com.displee.cache.index.Index;
import core.cache.Cache;
import core.cache.CacheArchive;
import core.cache.CacheIndex;
import core.cache.misc.buffer.ByteBufferUtils;
import core.tools.Log;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static core.api.ContentAPIKt.log;

/**
 * The type Struct.
 */
public class Struct {
    private static final Map<Integer, Struct> definitions = new HashMap<>();
    private final int id;
    /**
     * The Data store.
     */
    public HashMap<Integer, Object> dataStore = new HashMap<>();

    /**
     * Instantiates a new Struct.
     *
     * @param id the id
     */
    public Struct(int id) {
        this.id = id;
    }

    /**
     * Get int int.
     *
     * @param key the key
     * @return the int
     */
    public int getInt(int key){
        if(!dataStore.containsKey(key)){
            log(this.getClass(), Log.ERR,  "Invalid value passed for key: " + key + " struct: " + id);
            return -1;
        }
        return (int) dataStore.get(key);
    }

    /**
     * Get string string.
     *
     * @param key the key
     * @return the string
     */
    public String getString(int key){
        return (String) dataStore.get(key);
    }

    @Override
    public String toString() {
        return "Struct{" +
                "id=" + id +
                ", dataStore=" + dataStore +
                '}';
    }

    /**
     * Get struct.
     *
     * @param id the id
     * @return the struct
     */
    public static Struct get(int id){
        Struct def = definitions.get(id);
        if (def != null) {
            return def;
        }
        byte[] data = Cache.getData(CacheIndex.CONFIGURATION, CacheArchive.STRUCT_TYPE, id);
        def = parse(id, data);

        definitions.put(id, def);
        return def;
    }

    /**
     * Parse struct.
     *
     * @param id   the id
     * @param data the data
     * @return the struct
     */
    public static Struct parse(int id, byte[] data)
    {
        Struct def = new Struct(id);
        if (data != null) {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int opcode;

            while ((opcode = buffer.get() & 0xFF) != 0) {

                if (opcode == 249) {
                    int size = buffer.get() & 0xFF;

                    for (int i = 0; size > i; i++) {
                        boolean bool = (buffer.get() & 0xFF) == 1;
                        int key = ByteBufferUtils.getMedium(buffer);
                        Object value;
                        if (bool) {
                            value = ByteBufferUtils.getString(buffer);
                        } else {
                            value = buffer.getInt();
                        }
                        def.dataStore.put(key, value);
                    }
                }
            }
        }
        return def;
    }


    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }
}
