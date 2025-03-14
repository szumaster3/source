package core.cache.def.impl;

import core.cache.Cache;
import core.cache.CacheIndex;
import core.cache.def.Definition;
import core.cache.misc.buffer.ByteBufferUtils;
import core.game.interaction.OptionHandler;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.npc.drop.NPCDropTables;
import core.game.node.entity.player.Player;
import core.game.system.config.NPCConfigParser;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.tools.StringUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static core.api.ContentAPIKt.getVarp;

/**
 * The type Npc definition.
 */
public final class NPCDefinition extends Definition<NPC> {
	private static final Map<Integer, NPCDefinition> DEFINITIONS = new HashMap<>();
	private static final Map<String, OptionHandler> OPTION_HANDLERS = new HashMap<>();
    /**
     * The Size.
     */
    public int size = 1;
	private int combatLevel;
    /**
     * The Head icons.
     */
    public int headIcons;
    /**
     * The Is visible on map.
     */
    public boolean isVisibleOnMap;
	private NPCDropTables dropTables = new NPCDropTables(this);
    /**
     * The An int 833.
     */
    public int anInt833;
    /**
     * The An int 836.
     */
    public int anInt836;
    /**
     * The An int 837.
     */
    public int anInt837;
    /**
     * The A boolean 841.
     */
    public boolean aBoolean841;
    /**
     * The An int 842.
     */
    public int anInt842;
    /**
     * The Config file id.
     */
    public int configFileId;
    /**
     * The Child npc ids.
     */
    public int[] childNPCIds;
    /**
     * The An int 846.
     */
    public int anInt846;
    /**
     * The An int 850.
     */
    public int anInt850;
    /**
     * The A byte 851.
     */
    public byte aByte851;
    /**
     * The A boolean 852.
     */
    public boolean aBoolean852;
    /**
     * The An int 853.
     */
    public int anInt853;
    /**
     * The A byte 854.
     */
    public byte aByte854;
    /**
     * The A boolean 856.
     */
    public boolean aBoolean856;
    /**
     * The A boolean 857.
     */
    public boolean aBoolean857;
    /**
     * The A short array 859.
     */
    public short[] aShortArray859;
    /**
     * The A byte array 861.
     */
    public byte[] aByteArray861;
    /**
     * The A short 862.
     */
    public short aShort862;
    /**
     * The A boolean 863.
     */
    public boolean aBoolean863;
    /**
     * The An int 864.
     */
    public int anInt864;
    /**
     * The A short array 866.
     */
    public short[] aShortArray866;
    /**
     * The An int array 868.
     */
    public int[] anIntArray868;
    /**
     * The An int 869.
     */
    public int anInt869;
    /**
     * The An int 870.
     */
    public int anInt870;
    /**
     * The An int 871.
     */
    public int anInt871;
    /**
     * The An int 872.
     */
    public int anInt872;
    /**
     * The An int 874.
     */
    public int anInt874;
    /**
     * The An int 875.
     */
    public int anInt875;
    /**
     * The An int 876.
     */
    public int anInt876;
    /**
     * The An int 879.
     */
    public int anInt879;
    /**
     * The A short array 880.
     */
    public short[] aShortArray880;
    /**
     * The An int 884.
     */
    public int anInt884;
    /**
     * The Config id.
     */
    public int configId;
    /**
     * The An int 889.
     */
    public int anInt889;
    /**
     * The An int array 892.
     */
    public int[] anIntArray892;
    /**
     * The A short 894.
     */
    public short aShort894;
    /**
     * The A short array 896.
     */
    public short[] aShortArray896;
    /**
     * The An int 897.
     */
    public int anInt897;
    /**
     * The An int 899.
     */
    public int anInt899;
    /**
     * The An int 901.
     */
    public int anInt901;
    /**
     * The Stand animation.
     */
    public int standAnimation;
    /**
     * The Walk animation.
     */
    public int walkAnimation;
    /**
     * The Render animation id.
     */
    public int renderAnimationId;
	private int combatDistance;

	private Graphics[] combatGraphics = new Graphics[3];

	private int turnAnimation;
	private int turn180Animation;
	private int turnCWAnimation;
	private int turnCCWAnimation;

    /**
     * Instantiates a new Npc definition.
     *
     * @param id the id
     */
    public NPCDefinition(int id) {
		this.id = id;
		anInt842 = -1;
		configFileId = -1;
		anInt837 = -1;
		anInt846 = -1;
		anInt853 = 32;
		standAnimation = -1;
		walkAnimation = -1;
		combatLevel = 0;
		anInt836 = -1;
		name = "null";
		anInt869 = 0;
		anInt850 = 255;
		anInt871 = -1;
		aBoolean852 = true;
		aShort862 = (short) 0;
		anInt876 = -1;
		aByte851 = (byte) -96;
		anInt875 = 0;
		anInt872 = -1;
		aBoolean857 = true;
		anInt870 = -1;
		anInt874 = -1;
		anInt833 = -1;
		anInt864 = 128;
		headIcons = -1;
		aBoolean856 = false;
		configId = -1;
		aByte854 = (byte) -16;
		aBoolean863 = false;
		isVisibleOnMap = true;
		anInt889 = -1;
		anInt884 = -1;
		aBoolean841 = true;
		anInt879 = -1;
		anInt899 = 128;
		aShort894 = (short) 0;
		options = new String[5];
		anInt897 = 0;
		anInt901 = -1;
		anIntArray868 = new int[0];
	}

    /**
     * For id npc definition.
     *
     * @param id the id
     * @return the npc definition
     */
    public static NPCDefinition forId(int id) {
		NPCDefinition def = DEFINITIONS.get(id);
		if (def == null) {
			def = new NPCDefinition(id);
			byte[] data = Cache.getData(CacheIndex.NPC_CONFIGURATION, id >>> 7, id & 0x7f);
			if (data == null) {
				if (id != -1) {

				}
			} else {
				def.parse(ByteBuffer.wrap(data));
			}
			DEFINITIONS.put(id, def);
		}
		return def;
	}

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Throwable the throwable
     */
    public static void main(String... args) throws Throwable {
		GameWorld.prompt(false);

		// for (int i = 0; i < 11000; i++) {
		// ItemDefinition def = ItemDefinition.forId(i);
		// if (def.getMaleWornModelId1() >= 1250 && def.getMaleWornModelId1() <=
		// 1550) {

		// def.getMaleWornModelId1());
		// }
		// }
	}

    /**
     * Gets child npc.
     *
     * @param player the player
     * @return the child npc
     */
    public NPCDefinition getChildNPC(Player player) {
		if (childNPCIds == null || childNPCIds.length < 1) {
			return this;
		}
		int configValue = -1;
		if (player != null) {
			if (configFileId != -1) {
				configValue = VarbitDefinition.forNpcId(configFileId).getValue(player);
			} else if (configId != -1) {
				configValue = getVarp(player, configId);
			}
		} else {
			configValue = 0;
		}
		if (configValue < 0 || configValue >= childNPCIds.length - 1 || childNPCIds[configValue] == -1) {
			int objectId = childNPCIds[childNPCIds.length - 1];
			if (objectId != -1) {
				return forId(objectId);
			}
			return this;
		}
		return forId(childNPCIds[configValue]);
	}

	private void parse(ByteBuffer buffer) {
		while (true) {
			int opcode = buffer.get() & 0xFF;
			if (opcode == 0) {
				break;
			}
			parseOpcode(buffer, opcode);
		}
	}

	private void parseOpcode(ByteBuffer buffer, int opcode) {
		switch (opcode) {
		case 1:
			int length = buffer.get() & 0xFF;
			anIntArray868 = new int[length];
			for (int i_66_ = 0; i_66_ < length; i_66_++) {
				anIntArray868[i_66_] = buffer.getShort() & 0xFFFF;
				if ((anIntArray868[i_66_] ^ 0xffffffff) == -65536)
					anIntArray868[i_66_] = -1;
			}
			break;
		case 2:
			name = ByteBufferUtils.getString(buffer);
			break;
		case 12:
			size = buffer.get() & 0xFF;
			break;
		case 13:
			standAnimation = buffer.getShort();
			break;
		case 14:
			walkAnimation = buffer.getShort();
			break;
		case 15:
			turnAnimation = buffer.getShort();
			break;
		case 16:
			buffer.getShort(); // Another turn animation
			break;
		case 17:
			walkAnimation = buffer.getShort();
			turn180Animation = buffer.getShort();
			turnCWAnimation = buffer.getShort();
			turnCCWAnimation = buffer.getShort();
			break;
		case 30:
		case 31:
		case 32:
		case 33:
		case 34:
			options[opcode - 30] = ByteBufferUtils.getString(buffer);
			break;
		case 40:
			length = buffer.get() & 0xFF;
			aShortArray859 = new short[length];
			aShortArray896 = new short[length];
			for (int i_65_ = 0; (length ^ 0xffffffff) < (i_65_ ^ 0xffffffff); i_65_++) {
				aShortArray896[i_65_] = (short) (buffer.getShort() & 0xFFFF);
				aShortArray859[i_65_] = (short) (buffer.getShort() & 0xFFFF);
			}
			break;
		case 41:
			length = buffer.get() & 0xFF;
			aShortArray880 = new short[length];
			aShortArray866 = new short[length];
			for (int i_54_ = 0; (i_54_ ^ 0xffffffff) > (length ^ 0xffffffff); i_54_++) {
				aShortArray880[i_54_] = (short) (buffer.getShort() & 0xFFFF);
				aShortArray866[i_54_] = (short) (buffer.getShort() & 0xFFFF);
			}
			break;
		case 42:
			length = buffer.get() & 0xFF;
			aByteArray861 = new byte[length];
			for (int i_55_ = 0; length > i_55_; i_55_++) {
				aByteArray861[i_55_] = (byte) buffer.get();
			}
			break;
		case 60:
			length = buffer.get() & 0xFF;
			anIntArray892 = new int[length];
			for (int i_64_ = 0; (i_64_ ^ 0xffffffff) > (length ^ 0xffffffff); i_64_++) {
				anIntArray892[i_64_] = buffer.getShort() & 0xFFFF;
			}
			break;
		case 93:
			isVisibleOnMap = false;
			break;
		case 95:
			setCombatLevel(buffer.getShort() & 0xFFFF);
			break;
		case 97:
			anInt864 = buffer.getShort() & 0xFFFF;
			break;
		case 98:
			anInt899 = buffer.getShort() & 0xFFFF;
			break;
		case 99:
			aBoolean863 = true;
			break;
		case 100:
			anInt869 = buffer.get();
			break;
		case 101:
			anInt897 = buffer.get() * 5;
			break;
		case 102:
			headIcons = buffer.getShort() & 0xFFFF;
			break;
		case 103:
			anInt853 = buffer.getShort() & 0xFFFF;
			break;
		case 106:
		case 118:
			configFileId = buffer.getShort() & 0xFFFF;
			if (configFileId == 65535) {
				configFileId = -1;
			}
			configId = buffer.getShort() & 0xFFFF;
			if (configId == 65535) {
				configId = -1;
			}
			int defaultValue = -1;
			if ((opcode ^ 0xffffffff) == -119) {
				defaultValue = buffer.getShort() & 0xFFFF;
				if (defaultValue == 65535) {
					defaultValue = -1;
				}
			}
			length = buffer.get() & 0xFF;
			childNPCIds = new int[2 + length];
			for (int i = 0; length >= i; i++) {
				childNPCIds[i] = buffer.getShort() & 0xFFFF;
				if (childNPCIds[i] == 65535) {
					childNPCIds[i] = -1;
				}
			}
			childNPCIds[length + 1] = defaultValue;
			break;
		case 107:
			aBoolean841 = false;
			break;
		case 109:
			aBoolean852 = false;
			break;
		case 111:
			aBoolean857 = false;
			break;
		case 113:
			aShort862 = (short) (buffer.getShort() & 0xFFFF);
			aShort894 = (short) (buffer.getShort() & 0xFFFF);
			break;
		case 114:
			aByte851 = (byte) (buffer.get());
			aByte854 = (byte) (buffer.get());
			break;
		case 115:
			buffer.get();// & 0xFF;
			buffer.get();// & 0xFF;
			break;
		case 119:
			buffer.get();
			break;
		case 121:
			length = buffer.get() & 0xFF;
			for (int i = 0; i < length; i++) {
				buffer.get();
				buffer.get();
				buffer.get();
				buffer.get();
			}
			break;
		case 122:
			buffer.getShort();
			break;
		case 123:
			buffer.getShort();
			break;
		case 125:
			buffer.get();
			break;
		case 126:
			buffer.getShort();
		case 127:
			renderAnimationId = buffer.getShort();
			break;
		case 128:
			buffer.get();
			break;
		case 134:
			buffer.getShort();
			buffer.getShort();
			buffer.getShort();
			buffer.getShort();
			buffer.get();
			break;
		case 135:
			buffer.get();
			buffer.getShort();
			break;
		case 136:
			buffer.get();
			buffer.getShort();
			break;
		case 137:
			buffer.getShort();
			break;
		case 249:
			length = buffer.get() & 0xFF;
			for (int i = 0; i < length; i++) {
				boolean string = buffer.get() == 1;
				ByteBufferUtils.getMedium(buffer); // script id
				if (!string) {
					buffer.getInt(); // Value
				} else {
					ByteBufferUtils.getString(buffer); // value
				}
			}
			break;
		default:
			//SystemLogger.logErr("Unhandled NPC definition opcode: " + opcode);
		}
	}

    /**
     * Has attack option boolean.
     *
     * @return the boolean
     */
    public boolean hasAttackOption() {
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase("attack")) {
				return true;
			}
		}
		return false;
	}

    /**
     * Gets option handler.
     *
     * @param nodeId the node id
     * @param name   the name
     * @return the option handler
     */
    public static OptionHandler getOptionHandler(int nodeId, String name) {
		NPCDefinition def = forId(nodeId);
		OptionHandler handler = def.getConfiguration("option:" + name);
		if (handler != null) {
			return handler;
		}
		return OPTION_HANDLERS.get(name);
	}

    /**
     * Has action boolean.
     *
     * @param optionName the option name
     * @return the boolean
     */
    public boolean hasAction(String optionName) {
		if (options == null) {
			return false;
		}
		for (String action : options) {
			if (action == null) {
				continue;
			}
			if (action.equalsIgnoreCase(optionName)) {
				return true;
			}
		}
		return false;
	}

    /**
     * Sets option handler.
     *
     * @param name    the name
     * @param handler the handler
     * @return the option handler
     */
    public static boolean setOptionHandler(String name, OptionHandler handler) {
		return OPTION_HANDLERS.put(name, handler) != null;
	}

    /**
     * Gets option handlers.
     *
     * @return the option handlers
     */
    public static Map<String, OptionHandler> getOptionHandlers() {
		return OPTION_HANDLERS;
	}

    /**
     * Gets definitions.
     *
     * @return the definitions
     */
    public static final Map<Integer, NPCDefinition> getDefinitions() {
		return DEFINITIONS;
	}

	public final String getExamine() {
		String string = getConfiguration(NPCConfigParser.EXAMINE, examine);
		if (string != null) {
			return string;
		}
		if (name.length() <= 1) {
			return string;
		}
		return "It's a" + (StringUtils.isPlusN(name) ? "n " : " ") + name + ".";
	}

	public final void setExamine(String examine) {
		this.examine = examine;
	}

    /**
     * Init combat graphics.
     *
     * @param config the config
     */
    public void initCombatGraphics(Map<String, Object> config) {
		if (config.containsKey(NPCConfigParser.START_GRAPHIC)) {
			combatGraphics[0] = new Graphics((Integer) config.get(NPCConfigParser.START_GRAPHIC), getConfiguration(NPCConfigParser.START_HEIGHT, 0));
		}
		if (config.containsKey(NPCConfigParser.PROJECTILE)) {
			combatGraphics[1] = new Graphics((Integer) config.get(NPCConfigParser.PROJECTILE), getConfiguration(NPCConfigParser.PROJECTILE_HEIGHT, 42));
		}
		if (config.containsKey(NPCConfigParser.END_GRAPHIC)) {
			combatGraphics[2] = new Graphics((Integer) config.get(NPCConfigParser.END_GRAPHIC), getConfiguration(NPCConfigParser.END_HEIGHT, 96));
		}
	}

    /**
     * Gets combat animation.
     *
     * @param index the index
     * @return the combat animation
     */
    public Animation getCombatAnimation(int index) {
		String name = "";
		switch (index) {
		case 0:
			name = NPCConfigParser.MELEE_ANIMATION;
			break;
		case 1:
			name = NPCConfigParser.MAGIC_ANIMATION;
			break;
		case 2:
			name = NPCConfigParser.RANGE_ANIMATION;
			break;
		case 3:
			name = NPCConfigParser.DEFENCE_ANIMATION;
			break;
		case 4:
			name = NPCConfigParser.DEATH_ANIMATION;
			break;
			default:
				break;
		}
		return getConfiguration(name, null);
	}

    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize() {
		return size;
	}

    /**
     * Gets head icons.
     *
     * @return the head icons
     */
    public int getHeadIcons() {
		return headIcons;
	}

    /**
     * Is visible on map boolean.
     *
     * @return the boolean
     */
    public boolean isVisibleOnMap() {
		return isVisibleOnMap;
	}

    /**
     * Gets an int 833.
     *
     * @return the an int 833
     */
    public int getAnInt833() {
		return anInt833;
	}

    /**
     * Gets an int 836.
     *
     * @return the an int 836
     */
    public int getAnInt836() {
		return anInt836;
	}

    /**
     * Gets an int 837.
     *
     * @return the an int 837
     */
    public int getAnInt837() {
		return anInt837;
	}

    /**
     * Isa boolean 841 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean841() {
		return aBoolean841;
	}

    /**
     * Gets an int 842.
     *
     * @return the an int 842
     */
    public int getAnInt842() {
		return anInt842;
	}

    /**
     * Gets config file id.
     *
     * @return the config file id
     */
    public int getConfigFileId() {
		return configFileId;
	}

    /**
     * Get child npc ids int [ ].
     *
     * @return the int [ ]
     */
    public int[] getChildNPCIds() {
		return childNPCIds;
	}

    /**
     * Gets an int 846.
     *
     * @return the an int 846
     */
    public int getAnInt846() {
		return anInt846;
	}

    /**
     * Gets an int 850.
     *
     * @return the an int 850
     */
    public int getAnInt850() {
		return anInt850;
	}

    /**
     * Gets byte 851.
     *
     * @return the byte 851
     */
    public byte getaByte851() {
		return aByte851;
	}

    /**
     * Isa boolean 852 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean852() {
		return aBoolean852;
	}

    /**
     * Gets an int 853.
     *
     * @return the an int 853
     */
    public int getAnInt853() {
		return anInt853;
	}

    /**
     * Gets byte 854.
     *
     * @return the byte 854
     */
    public byte getaByte854() {
		return aByte854;
	}

    /**
     * Isa boolean 856 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean856() {
		return aBoolean856;
	}

    /**
     * Isa boolean 857 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean857() {
		return aBoolean857;
	}

    /**
     * Geta short array 859 short [ ].
     *
     * @return the short [ ]
     */
    public short[] getaShortArray859() {
		return aShortArray859;
	}

    /**
     * Geta byte array 861 byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getaByteArray861() {
		return aByteArray861;
	}

    /**
     * Gets short 862.
     *
     * @return the short 862
     */
    public short getaShort862() {
		return aShort862;
	}

    /**
     * Isa boolean 863 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean863() {
		return aBoolean863;
	}

    /**
     * Gets an int 864.
     *
     * @return the an int 864
     */
    public int getAnInt864() {
		return anInt864;
	}

    /**
     * Geta short array 866 short [ ].
     *
     * @return the short [ ]
     */
    public short[] getaShortArray866() {
		return aShortArray866;
	}

    /**
     * Get an int array 868 int [ ].
     *
     * @return the int [ ]
     */
    public int[] getAnIntArray868() {
		return anIntArray868;
	}

    /**
     * Gets an int 869.
     *
     * @return the an int 869
     */
    public int getAnInt869() {
		return anInt869;
	}

    /**
     * Gets an int 870.
     *
     * @return the an int 870
     */
    public int getAnInt870() {
		return anInt870;
	}

    /**
     * Gets an int 871.
     *
     * @return the an int 871
     */
    public int getAnInt871() {
		return anInt871;
	}

    /**
     * Gets an int 872.
     *
     * @return the an int 872
     */
    public int getAnInt872() {
		return anInt872;
	}

    /**
     * Gets an int 874.
     *
     * @return the an int 874
     */
    public int getAnInt874() {
		return anInt874;
	}

    /**
     * Gets an int 875.
     *
     * @return the an int 875
     */
    public int getAnInt875() {
		return anInt875;
	}

    /**
     * Gets an int 876.
     *
     * @return the an int 876
     */
    public int getAnInt876() {
		return anInt876;
	}

    /**
     * Gets an int 879.
     *
     * @return the an int 879
     */
    public int getAnInt879() {
		return anInt879;
	}

    /**
     * Geta short array 880 short [ ].
     *
     * @return the short [ ]
     */
    public short[] getaShortArray880() {
		return aShortArray880;
	}

    /**
     * Gets an int 884.
     *
     * @return the an int 884
     */
    public int getAnInt884() {
		return anInt884;
	}

    /**
     * Gets config id.
     *
     * @return the config id
     */
    public int getConfigId() {
		if(configFileId != -1) {
			return VarbitDefinition.forNpcId(configFileId).getVarpId();
		} else return configFileId;
	}

    /**
     * Gets varbit offset.
     *
     * @return the varbit offset
     */
    public int getVarbitOffset() {
		if(configFileId != -1){
			return VarbitDefinition.forNpcId(configFileId).getStartBit();
		}
		return -1;
	}

    /**
     * Gets varbit size.
     *
     * @return the varbit size
     */
    public int getVarbitSize() {
		if(configFileId != -1){
			return VarbitDefinition.forNpcId(configFileId).getEndBit() - VarbitDefinition.forNpcId(configFileId).getStartBit();
		}
		return -1;
	}

    /**
     * Gets an int 889.
     *
     * @return the an int 889
     */
    public int getAnInt889() {
		return anInt889;
	}

    /**
     * Get an int array 892 int [ ].
     *
     * @return the int [ ]
     */
    public int[] getAnIntArray892() {
		return anIntArray892;
	}

    /**
     * Gets short 894.
     *
     * @return the short 894
     */
    public short getaShort894() {
		return aShort894;
	}

    /**
     * Geta short array 896 short [ ].
     *
     * @return the short [ ]
     */
    public short[] getaShortArray896() {
		return aShortArray896;
	}

    /**
     * Gets an int 897.
     *
     * @return the an int 897
     */
    public int getAnInt897() {
		return anInt897;
	}

    /**
     * Gets an int 899.
     *
     * @return the an int 899
     */
    public int getAnInt899() {
		return anInt899;
	}

    /**
     * Gets an int 901.
     *
     * @return the an int 901
     */
    public int getAnInt901() {
		return anInt901;
	}

    /**
     * Gets stand animation.
     *
     * @return the stand animation
     */
    public int getStandAnimation() {
		return standAnimation;
	}

    /**
     * Gets walk animation.
     *
     * @return the walk animation
     */
    public int getWalkAnimation() {
		return walkAnimation;
	}

    /**
     * Gets turn animation.
     *
     * @return the turn animation
     */
    public int getTurnAnimation() {
		return turnAnimation;
	}

    /**
     * Gets turn 180 animation.
     *
     * @return the turn 180 animation
     */
    public int getTurn180Animation() {
		return turn180Animation;
	}

    /**
     * Gets turn cw animation.
     *
     * @return the turn cw animation
     */
    public int getTurnCWAnimation() {
		return turnCWAnimation;
	}

    /**
     * Gets turn ccw animation.
     *
     * @return the turn ccw animation
     */
    public int getTurnCCWAnimation() {
		return turnCCWAnimation;
	}

    /**
     * Gets drop tables.
     *
     * @return the drop tables
     */
    public NPCDropTables getDropTables() {
		return dropTables;
	}

    /**
     * Sets drop tables.
     *
     * @param dropTables the drop tables
     */
    public void setDropTables(NPCDropTables dropTables) {
		this.dropTables = dropTables;
	}

    /**
     * Gets combat level.
     *
     * @return the combat level
     */
    public int getCombatLevel() {
		return combatLevel;
	}

    /**
     * Sets combat level.
     *
     * @param combatLevel the combat level
     */
    public void setCombatLevel(int combatLevel) {
		this.combatLevel = combatLevel;
	}

    /**
     * Gets combat distance.
     *
     * @return the combat distance
     */
    public int getCombatDistance() {
		return combatDistance;
	}

    /**
     * Sets combat distance.
     *
     * @param combatDistance the combat distance
     */
    public void setCombatDistance(int combatDistance) {
		this.combatDistance = combatDistance;
	}

    /**
     * Get combat graphics graphics [ ].
     *
     * @return the graphics [ ]
     */
    public Graphics[] getCombatGraphics() {
		return combatGraphics;
	}

    /**
     * Sets combat graphics.
     *
     * @param combatGraphics the combat graphics
     */
    public void setCombatGraphics(Graphics[] combatGraphics) {
		this.combatGraphics = combatGraphics;
	}

    /**
     * Gets render animation id.
     *
     * @return the render animation id
     */
    public int getRenderAnimationId() {
		return renderAnimationId;
	}

    /**
     * Sets render animation id.
     *
     * @param renderAnimationId the render animation id
     */
    public void setRenderAnimationId(int renderAnimationId) {
		this.renderAnimationId = renderAnimationId;
	}
}
