package core.cache.def.impl;

import core.ServerConstants;
import core.cache.Cache;
import core.cache.CacheArchive;
import core.cache.CacheIndex;

import java.nio.ByteBuffer;

/**
 * The type Cloth definition.
 */
public final class ClothDefinition {
	private int bodyPartId;
	private int[] bodyModelIds;
	private boolean notSelectable;
	private int[] headModelIds = { -1, -1, -1, -1, -1 };
	private int[] originalColors;
	private int[] modifiedColors;
	private int[] originalTextureColors;
	private int[] modifiedTextureColors;

    /**
     * For id cloth definition.
     *
     * @param clothId the cloth id
     * @return the cloth definition
     */
    public static ClothDefinition forId(int clothId) {
		ClothDefinition def = new ClothDefinition();
		byte[] bs = Cache.getData(CacheIndex.CONFIGURATION, CacheArchive.PLAYER_KIT, clothId);
		if (bs != null) {
			def.load(ByteBuffer.wrap(bs));
		}
		return def;
	}

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String... args) {
		try {
			Cache.init(ServerConstants.CACHE_PATH);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		int length = Cache.getArchiveCapacity(CacheIndex.CONFIGURATION, CacheArchive.PLAYER_KIT);
		for (int i = 0; i < length; i++) {
			ClothDefinition def = forId(i);
		}
	}

    /**
     * Load.
     *
     * @param buffer the buffer
     */
    public void load(ByteBuffer buffer) {
		int opcode;
		while ((opcode = buffer.get() & 0xFF) != 0) {
			parse(opcode, buffer);
		}
	}

	private void parse(int opcode, ByteBuffer buffer) {
		switch (opcode) {
		case 1:
			bodyPartId = buffer.get() & 0xFF;
			break;
		case 2:
			int length = buffer.get() & 0xFF;
			bodyModelIds = new int[length];
			for (int i = 0; i < length; i++) {
				bodyModelIds[i] = buffer.getShort() & 0xFFFF;
			}
			break;
		case 3:
			notSelectable = true;
			break;
		case 40:
			length = buffer.get() & 0xFF;
			originalColors = new int[length];
			modifiedColors = new int[length];
			for (int i = 0; i < length; i++) {
				originalColors[i] = buffer.getShort();
				modifiedColors[i] = buffer.getShort();
			}
			break;
		case 41:
			length = buffer.get() & 0xFF;
			originalTextureColors = new int[length];
			modifiedTextureColors = new int[length];
			for (int i = 0; i < length; i++) {
				originalTextureColors[i] = buffer.getShort();
				modifiedTextureColors[i] = buffer.getShort();
			}
			break;
		default:
			if (opcode >= 60 && opcode < 70) {
				headModelIds[opcode - 60] = buffer.getShort() & 0xFFFF;
			}
			break;
		}
	}

    /**
     * Gets body part id.
     *
     * @return the body part id
     */
    public int getBodyPartId() {
		return bodyPartId;
	}

    /**
     * Get body model ids int [ ].
     *
     * @return the int [ ]
     */
    public int[] getBodyModelIds() {
		return bodyModelIds;
	}

    /**
     * Is not selectable boolean.
     *
     * @return the boolean
     */
    public boolean isNotSelectable() {
		return notSelectable;
	}

    /**
     * Get original colors int [ ].
     *
     * @return the int [ ]
     */
    public int[] getOriginalColors() {
		return originalColors;
	}

    /**
     * Get modified colors int [ ].
     *
     * @return the int [ ]
     */
    public int[] getModifiedColors() {
		return modifiedColors;
	}

    /**
     * Get original texture colors int [ ].
     *
     * @return the int [ ]
     */
    public int[] getOriginalTextureColors() {
		return originalTextureColors;
	}

    /**
     * Get modified texture colors int [ ].
     *
     * @return the int [ ]
     */
    public int[] getModifiedTextureColors() {
		return modifiedTextureColors;
	}

    /**
     * Get head model ids int [ ].
     *
     * @return the int [ ]
     */
    public int[] getHeadModelIds() {
		return headModelIds;
	}
}