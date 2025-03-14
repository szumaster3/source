package core.cache.def.impl;

import core.cache.Cache;
import core.cache.CacheIndex;
import core.game.node.entity.player.Player;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static core.api.ContentAPIKt.getVarbit;

/**
 * The type Varbit definition.
 */
public final class VarbitDefinition {
	private static final Map<Integer, VarbitDefinition> MAPPING = new HashMap<>();
	private static final int[] BITS = new int[32];
    private final int id;
    private int varpId;
    private int startBit;
    private int endBit;

	/**
	 * Instantiates a new Varbit definition.
	 *
	 * @param id the id
	 */
	public VarbitDefinition(int id) {
		this.id = id;
	}

	/**
	 * Instantiates a new Varbit definition.
	 *
	 * @param varpId   the varp id
	 * @param id       the id
	 * @param startBit the start bit
	 * @param endBit   the end bit
	 */
	public VarbitDefinition(int varpId, int id, int startBit, int endBit)
	{
		this.varpId = varpId;
		this.id = id;
		this.startBit = startBit;
		this.endBit = endBit;
	}

	static {
		int flag = 2;
		for (int i = 0; i < 32; i++) {
			BITS[i] = flag - 1;
			flag += flag;
		}

	}

	/**
	 * For scenery id varbit definition.
	 *
	 * @param id the id
	 * @return the varbit definition
	 */
	public static VarbitDefinition forSceneryId(int id) {
		return forId(id);
	}

	/**
	 * For npc id varbit definition.
	 *
	 * @param id the id
	 * @return the varbit definition
	 */
	public static VarbitDefinition forNpcId(int id){
		return forId(id);
	}

	/**
	 * For item id varbit definition.
	 *
	 * @param id the id
	 * @return the varbit definition
	 */
	public static VarbitDefinition forItemId(int id){
		return forId(id);
	}

	/**
	 * For id varbit definition.
	 *
	 * @param id the id
	 * @return the varbit definition
	 */
	public static VarbitDefinition forId(int id){
		VarbitDefinition def = MAPPING.get(id);
		if (def != null) {
			return def;
		}

		def = new VarbitDefinition(id);
		byte[] bs = Cache.getData(CacheIndex.VAR_BIT, id >>> 10, id & 0x3ff);
		if (bs != null) {
			ByteBuffer buffer = ByteBuffer.wrap(bs);
			int opcode = 0;
			while ((opcode = buffer.get() & 0xFF) != 0) {
				if (opcode == 1) {
					def.varpId = buffer.getShort() & 0xFFFF;
					def.startBit = buffer.get() & 0xFF;
					def.endBit = buffer.get() & 0xFF;
				}
			}
		}
		MAPPING.put(id, def);
		return def;
	}

	/**
	 * Create.
	 *
	 * @param varpId   the varp id
	 * @param varbitId the varbit id
	 * @param startBit the start bit
	 * @param endBit   the end bit
	 */
	public static void create(int varpId, int varbitId, int startBit, int endBit){
		VarbitDefinition def = new VarbitDefinition(
			varpId,
			varbitId,
			startBit,
			endBit
		);
		MAPPING.put(varbitId, def);
	}

	/**
	 * Gets value.
	 *
	 * @param player the player
	 * @return the value
	 */
	public int getValue(Player player) {
            return getVarbit(player, id);
	}

	/**
	 * Gets mapping.
	 *
	 * @return the mapping
	 */
	public static Map<Integer, VarbitDefinition> getMapping() {
		return MAPPING;
	}

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets varp id.
	 *
	 * @return the varp id
	 */
	public int getVarpId() {
		return varpId;
	}

	/**
	 * Gets start bit.
	 *
	 * @return the start bit
	 */
	public int getStartBit() {
		return startBit;
	}

	/**
	 * Gets end bit.
	 *
	 * @return the end bit
	 */
	public int getEndBit() {
		return endBit;
	}

	/**
	 * Gets mask.
	 *
	 * @return the mask
	 */
	public int getMask() {
                int mask = 0;
                for (int i = startBit; i <= endBit; i++)
                    mask |= (1 << (i - startBit));
                return mask;
        }

	@Override
	public String toString() {
		return "ConfigFileDefinition [id=" + id + ", configId=" + varpId + ", bitShift=" + startBit + ", bitSize=" + endBit + "]";
	}
}
