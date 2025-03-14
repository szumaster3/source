package core.cache.def.impl;

import core.cache.Cache;
import core.cache.CacheIndex;
import core.cache.def.Definition;
import core.cache.misc.buffer.ByteBufferUtils;
import core.game.interaction.OptionHandler;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.GameWorld;
import core.tools.Log;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static core.api.ContentAPIKt.getVarp;
import static core.api.ContentAPIKt.log;

/**
 * The type Scenery definition.
 */
public class SceneryDefinition extends Definition<Scenery> {
    private static final Map<Integer, SceneryDefinition> DEFINITIONS = new HashMap<Integer, SceneryDefinition>();
    private static final Map<String, OptionHandler> OPTION_HANDLERS = new HashMap<>();
    private short[] originalColors;
    /**
     * The Config object ids.
     */
    public int[] configObjectIds;
    private int[] modelIds;
    private int[] modelConfiguration;
    /**
     * The An int 3832.
     */
    static int anInt3832;
    /**
     * The An int array 3833.
     */
    int[] anIntArray3833 = null;
    private int anInt3834;
    /**
     * The An int 3835.
     */
    int anInt3835;
    /**
     * The An int 3836.
     */
    static int anInt3836;
    private byte aByte3837;
    /**
     * The An int 3838.
     */
    int anInt3838 = -1;
    /**
     * The Mirrored.
     */
    boolean mirrored;
    private int contrast;
    private int modelSizeZ;
    /**
     * The An int 3842.
     */
    static int anInt3842;
    /**
     * The An int 3843.
     */
    static int anInt3843;
    /**
     * The An int 3844.
     */
    int anInt3844;
    /**
     * The A boolean 3845.
     */
    boolean aBoolean3845;
    /**
     * The An int 3846.
     */
    static int anInt3846;
    private byte aByte3847;
    private byte aByte3849;
    /**
     * The An int 3850.
     */
    int anInt3850;
    /**
     * The An int 3851.
     */
    int anInt3851;
    /**
     * The Blocks land.
     */
    public boolean blocksLand;
    /**
     * The A boolean 3853.
     */
    public boolean aBoolean3853;
    /**
     * The An int 3855.
     */
    int anInt3855;
    /**
     * The Ignore on route.
     */
    public boolean ignoreOnRoute;
    /**
     * The An int 3857.
     */
    int anInt3857;
    private byte[] recolourPalette;
    /**
     * The An int array 3859.
     */
    int[] anIntArray3859;
    /**
     * The An int 3860.
     */
    int anInt3860;
    /**
     * The Varbit index.
     */
    int varbitIndex;
    private short[] modifiedColors;
    /**
     * The An int 3865.
     */
    int anInt3865;
    /**
     * The A boolean 3866.
     */
    boolean aBoolean3866;
    /**
     * The A boolean 3867.
     */
    boolean aBoolean3867;
    /**
     * The Projectile clipped.
     */
    public boolean projectileClipped;
    private int[] anIntArray3869;
    /**
     * The A boolean 3870.
     */
    boolean aBoolean3870;
    /**
     * The Size y.
     */
    public int sizeY;
    /**
     * The Casts shadow.
     */
    boolean castsShadow;
    /**
     * The Members only.
     */
    boolean membersOnly;
    /**
     * The Boolean 1.
     */
    public boolean boolean1;
    private int anInt3875;
    /**
     * The Animation id.
     */
    public int animationId;
    private int anInt3877;
    private int brightness;
    /**
     * The Solid.
     */
    public int solid;
    private int anInt3881;
    private int anInt3882;
    private int offsetX;
    /**
     * The Loader.
     */
    Object loader;
    private int offsetZ;
    /**
     * The Size x.
     */
    public int sizeX;
    /**
     * The A boolean 3891.
     */
    public boolean aBoolean3891;
    /**
     * The Offset multiplier.
     */
    int offsetMultiplier;
    /**
     * The Interactive.
     */
    public int interactive;
    /**
     * The A boolean 3894.
     */
    boolean aBoolean3894;
    /**
     * The A boolean 3895.
     */
    boolean aBoolean3895;
    /**
     * The An int 3896.
     */
    int anInt3896;
    /**
     * The Config id.
     */
    int configId;
    private byte[] aByteArray3899;
    /**
     * The An int 3900.
     */
    int anInt3900;
    private int modelSizeX;
    /**
     * The An int 3904.
     */
    int anInt3904;
    /**
     * The An int 3905.
     */
    int anInt3905;
    /**
     * The A boolean 3906.
     */
    boolean aBoolean3906;
    /**
     * The An int array 3908.
     */
    int[] anIntArray3908;
    private byte contouredGround;
    /**
     * The An int 3913.
     */
    int anInt3913;
    private byte aByte3914;
    private int offsetY;
    private int[][] anIntArrayArray3916;
    private int modelSizeY;
    private short[] modifiedTextureColours;
    private short[] originalTextureColours;
    /**
     * The An int 3921.
     */
    int anInt3921;
    private Object aClass194_3922;
    /**
     * The A boolean 3923.
     */
    boolean aBoolean3923;
    /**
     * The A boolean 3924.
     */
    boolean aBoolean3924;
    /**
     * The Walking flag.
     */
    int walkingFlag;
    private boolean hasHiddenOptions;
    private short mapIcon;

    /**
     * Instantiates a new Scenery definition.
     */
    public SceneryDefinition() {
        anInt3835 = -1;
        anInt3860 = -1;
        varbitIndex = -1;
        aBoolean3866 = false;
        anInt3851 = -1;
        anInt3865 = 255;
        aBoolean3845 = false;
        aBoolean3867 = false;
        anInt3850 = 0;
        anInt3844 = -1;
        anInt3881 = 0;
        anInt3857 = -1;
        castsShadow = true;
        anInt3882 = -1;
        anInt3834 = 0;
        options = new String[5];
        anInt3875 = 0;
        mirrored = false;
        anIntArray3869 = null;
        sizeY = 1;
        boolean1 = false;
        projectileClipped = true;
        offsetX = 0;
        aBoolean3895 = true;
        contrast = 0;
        aBoolean3870 = false;
        offsetZ = 0;
        aBoolean3853 = true;
        blocksLand = false;
        solid = 2;
        anInt3855 = -1;
        brightness = 0;
        anInt3904 = 0;
        sizeX = 1;
        animationId = -1;
        ignoreOnRoute = false;
        aBoolean3891 = false;
        anInt3905 = 0;
        name = "null";
        anInt3913 = -1;
        aBoolean3906 = false;
        membersOnly = false;
        aByte3914 = (byte) 0;
        offsetY = 0;
        anInt3900 = 0;
        interactive = -1;
        aBoolean3894 = false;
        contouredGround = (byte) 0;
        anInt3921 = 0;
        modelSizeX = 128;
        configId = -1;
        anInt3877 = 0;
        walkingFlag = 0;
        offsetMultiplier = 64;
        aBoolean3923 = false;
        aBoolean3924 = false;
        modelSizeZ = 128;
        modelSizeY = 128;
        mapIcon = -1;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Throwable the throwable
     */
    public static void main(String... args) throws Throwable {
        GameWorld.prompt(false);
        // if (true) {
        // for (int id = 0; id <= 27325; id++) {
        // ObjectDefinition def = ObjectDefinition.forId(id);
        // if (def.mapIcon > 69) {

        // def.mapIcon);
        // }
        // }
        // return; 2105
        // }

    }

    /**
     * Parse.
     *
     * @throws Throwable the throwable
     */
    public static void parse() throws Throwable {
        for (int objectId = 0; objectId < Cache.getIndexCapacity(CacheIndex.SCENERY_CONFIGURATION); objectId++) {
            byte[] data = Cache.getData(CacheIndex.SCENERY_CONFIGURATION, objectId >>> 8, objectId & 0xFF);
            if (data == null) {
                SceneryDefinition.getDefinitions().put(objectId, new SceneryDefinition());
                //SystemLogger.logErr("Could not load object definitions for id " + objectId + " - no data!");
                continue;
            }
            SceneryDefinition def = SceneryDefinition.parseDefinition(objectId, ByteBuffer.wrap(data));
            if (def == null) {
                //	SystemLogger.logErr("Could not load object definitions for id " + objectId + " - no definitions found!");
                return;
            }
            SceneryDefinition.getDefinitions().put(objectId, def);
            data = null;
        }
    }

    /**
     * For id scenery definition.
     *
     * @param objectId the object id
     * @return the scenery definition
     */
    public static SceneryDefinition forId(int objectId) {
        SceneryDefinition def = DEFINITIONS.get(objectId);
        if (def != null) {
            return def;
        }
        DEFINITIONS.put(objectId, def = new SceneryDefinition());
        def.id = objectId;
        return def;
    }

    /**
     * Parse definition scenery definition.
     *
     * @param objectId the object id
     * @param buffer   the buffer
     * @return the scenery definition
     */
    public static SceneryDefinition parseDefinition(int objectId, ByteBuffer buffer) {
        SceneryDefinition def = new SceneryDefinition();
        def.id = objectId;
//		SystemLogger.logErr("----------------------------------------------------\n\n\n");
        while (true) {
            if (!buffer.hasRemaining()) {
                log(SceneryDefinition.class, Log.ERR, "Buffer empty for " + objectId);
                break;
            }
            int opcode = buffer.get() & 0xFF;
            //SystemLogger.logErr("Parsing object " + objectId + " op " + opcode);
            if (opcode == 1 || opcode == 5) {
                int length = buffer.get() & 0xff;
                if (def.modelIds == null) {
                    def.modelIds = new int[length];
                    if (opcode == 1) {
                        def.modelConfiguration = new int[length];
                    }
                    for (int i = 0; i < length; i++) {
                        def.modelIds[i] = buffer.getShort() & 0xFFFF;
                        if (opcode == 1) {
                            def.modelConfiguration[i] = buffer.get() & 0xFF;
                        }
                    }
                } else {
                    buffer.position(buffer.position() + (length * (opcode == 1 ? 3 : 2)));
                }
            } else if (opcode == 2) {
                def.name = ByteBufferUtils.getString(buffer);
            } else if (opcode == 14) {
                def.sizeX = buffer.get() & 0xFF;
            } else if (opcode == 15) {
                def.sizeY = buffer.get() & 0xFF;
            } else if (opcode == 17) {
                def.projectileClipped = false;
                def.solid = 0;
            } else if (opcode == 18) {
                def.projectileClipped = false;
            } else if (opcode == 19) {
                def.interactive = buffer.get() & 0xFF;
            } else if (opcode == 21) {
                def.contouredGround = (byte) 1;
            } else if (opcode == 22) {
                def.aBoolean3867 = true;
            } else if (opcode == 23) {
                def.boolean1 = true;
            } else if (opcode == 24) {
                def.animationId = buffer.getShort() & 0xFFFF;
                if (def.animationId == 65535) {
                    def.animationId = -1;
                }
            } else if (opcode == 27) {
                def.solid = 1;
            } else if (opcode == 28) {
                def.offsetMultiplier = ((buffer.get() & 0xFF) << 2);
            } else if (opcode == 29) {
                def.brightness = buffer.get();
            } else if (opcode == 39) {
                def.contrast = buffer.get() * 5;
            } else if (opcode >= 30 && opcode < 35) {
                def.options[opcode - 30] = ByteBufferUtils.getString(buffer);
                if (def.options[opcode - 30].equals("Hidden")) {
                    def.options[opcode - 30] = null;
                    def.hasHiddenOptions = true;
                }
            } else if (opcode == 40) {
                int length = buffer.get() & 0xFF;
                def.originalColors = new short[length];
                def.modifiedColors = new short[length];
                for (int i = 0; i < length; i++) {
                    def.originalColors[i] = buffer.getShort();
                    def.modifiedColors[i] = buffer.getShort();
                }
            } else if (opcode == 41) {
                int length = buffer.get() & 0xFF;
                def.originalTextureColours = new short[length];
                def.modifiedTextureColours = new short[length];
                for (int i = 0; i < length; i++) {
                    def.originalTextureColours[i] = buffer.getShort();
                    def.modifiedTextureColours[i] = buffer.getShort();
                }
            } else if (opcode == 42) {
                int length = buffer.get() & 0xFF;
                def.recolourPalette = new byte[length];
                for (int i = 0; i < length; i++) {
                    def.recolourPalette[i] = buffer.get();
                }
            } else if (opcode == 60) {
                def.mapIcon = buffer.getShort();
            } else if (opcode == 62) {
                def.mirrored = true;
            } else if (opcode == 64) {
                def.castsShadow = false;
            } else if (opcode == 65) {
                def.modelSizeX = buffer.getShort() & 0xFFFF;
            } else if (opcode == 66) {
                def.modelSizeZ = buffer.getShort() & 0xFFFF;
            } else if (opcode == 67) {
                def.modelSizeY = buffer.getShort() & 0xFFFF;
            } else if (opcode == 68) {
                buffer.getShort();
            } else if (opcode == 69) {
                def.walkingFlag = buffer.get() & 0xFF;
            } else if (opcode == 70) {
                def.offsetX = buffer.getShort() << 2;
            } else if (opcode == 71) {
                def.offsetZ = buffer.getShort() << 2;
            } else if (opcode == 72) {
                def.offsetY = buffer.getShort() << 2;
            } else if (opcode == 73) {
                def.blocksLand = true;
            } else if (opcode == 74) {
                def.ignoreOnRoute = true;
            } else if (opcode == 75) {
                def.anInt3855 = buffer.get() & 0xFF;
            } else if (opcode == 77 || opcode == 92) {
                def.varbitIndex = buffer.getShort() & 0xFFFF;
                if (def.varbitIndex == 65535) {
                    def.varbitIndex = -1;
                }
                def.configId = buffer.getShort() & 0xFFFF;
                if (def.configId == 65535) {
                    def.configId = -1;
                }
                int defaultId = -1;
                if (opcode == 92) {
                    defaultId = buffer.getShort() & 0xFFFF;
                    if (defaultId == 65535) {
                        defaultId = -1;
                    }
                }
                int childrenAmount = buffer.get() & 0xFF;
                def.configObjectIds = new int[childrenAmount + 2];
                for (int index = 0; childrenAmount >= index; index++) {
                    def.configObjectIds[index] = buffer.getShort() & 0xFFFF;
                    if (def.configObjectIds[index] == 65535) {
                        def.configObjectIds[index] = -1;
                    }
                }
                def.configObjectIds[childrenAmount + 1] = defaultId;
            } else if (opcode == 78) {
                def.anInt3860 = buffer.getShort() & 0xFFFF;
                def.anInt3904 = buffer.get() & 0xFF;
            } else if (opcode == 79) {
                def.anInt3900 = buffer.getShort() & 0xFFFF;
                def.anInt3905 = buffer.getShort() & 0xFFFF;
                def.anInt3904 = buffer.get() & 0xFF;
                int length = buffer.get() & 0xFF;
                def.anIntArray3859 = new int[length];
                for (int i = 0; i < length; i++) {
                    def.anIntArray3859[i] = buffer.getShort() & 0xFFFF;
                }
            } else if (opcode == 81) {
                def.contouredGround = (byte) 2;
                def.anInt3882 = 256 * buffer.get() & 0xFF;
            } else if (opcode == 82 || opcode == 88) {
                // Nothing.
            } else if (opcode == 89) {
                def.aBoolean3895 = false;
            } else if (opcode == 90) {
                def.aBoolean3870 = true;
            } else if (opcode == 91) {
                def.membersOnly = true;
            } else if (opcode == 93) {
                def.contouredGround = (byte) 3;
                def.anInt3882 = buffer.getShort() & 0xFFFF;
            } else if (opcode == 94) {
                def.contouredGround = (byte) 4;
            } else if (opcode == 95) {
                def.contouredGround = (byte) 5;
            } else if (opcode == 96 || opcode == 97) {
                //
            } else if (opcode == 100) {
                buffer.get();
                buffer.getShort();
            } else if (opcode == 101) {
                buffer.get();
            } else if (opcode == 102) {
                buffer.getShort();
            } else if (opcode == 249) { // cs2 scripts
                int length = buffer.get() & 0xFF;
                for (int i = 0; i < length; i++) {
                    boolean string = buffer.get() == 1;
                    ByteBufferUtils.getMedium(buffer); // script id
                    if (!string) {
                        buffer.getInt(); // Value
                    } else {
                        ByteBufferUtils.getString(buffer); // value
                    }
                }
            } else {
                if (opcode != 0) {
                    log(SceneryDefinition.class, Log.ERR, "Unhandled object definition opcode: " + opcode);
                }
                break;
            }
        }
        def.configureObject();
        if (def.ignoreOnRoute) {
            def.solid = 0;
            def.projectileClipped = false;
        }
        return def;
    }

    /**
     * Configure object.
     */
    final void configureObject() {
        if (interactive == -1) {
            interactive = 0;
            if (modelIds != null && (getModelConfiguration() == null || getModelConfiguration()[0] == 10)) {
                interactive = 1;
            }
            for (int i = 0; i < 5; i++) {
                if (options[i] != null) {
                    interactive = 1;
                    break;
                }
            }
        }
        if (configObjectIds != null) {
            for (int i = 0; i < configObjectIds.length; ++i) {
                SceneryDefinition def = forId(configObjectIds[i]);
                def.varbitIndex = varbitIndex;
            }
        }
        if (anInt3855 == -1) {
            anInt3855 = solid == 0 ? 0 : 1;
        }
        // Manual changes
        if (id == 31017) {
            sizeX = sizeY = 2;
        }
        if (id == 29292) {
            projectileClipped = false;
        }
    }

    /**
     * Has actions boolean.
     *
     * @return the boolean
     */
    public boolean hasActions() {
        if (interactive > 0) {
            return true;
        }
        if (configObjectIds == null) {
            return hasOptions(false);
        }
        for (int i = 0; i < configObjectIds.length; i++) {
            if (configObjectIds[i] != -1) {
                SceneryDefinition def = forId(configObjectIds[i]);
                if (def.hasOptions(false)) {
                    return true;
                }
            }
        }
        return hasOptions(false);
    }

    /**
     * Gets child object.
     *
     * @param player the player
     * @return the child object
     */
    public SceneryDefinition getChildObject(Player player) {
        if (configObjectIds == null || configObjectIds.length < 1) {
            return this;
        }
        int configValue = -1;
        if (player != null) {
            if (varbitIndex != -1) {
                VarbitDefinition def = VarbitDefinition.forSceneryId(varbitIndex);
                if (def != null) {
                    configValue = def.getValue(player);
                }
            } else if (configId != -1) {
                configValue = getVarp(player, configId);
            }
        } else {
            configValue = 0;
        }
        SceneryDefinition childDef = getChildObjectAtIndex(configValue);
        if (childDef != null) childDef.varbitIndex = this.varbitIndex;
        return childDef;
    }

    /**
     * Gets child object at index.
     *
     * @param index the index
     * @return the child object at index
     */
    public SceneryDefinition getChildObjectAtIndex(int index) {
        if (configObjectIds == null || configObjectIds.length < 1) {
            return this;
        }
        if (index < 0 || index >= configObjectIds.length - 1 || configObjectIds[index] == -1) {
            int objectId = configObjectIds[configObjectIds.length - 1];
            if (objectId != -1) {
                return forId(objectId);
            }
            return this;
        }
        return forId(configObjectIds[index]);
    }

    /**
     * Gets config file.
     *
     * @return the config file
     */
    public VarbitDefinition getConfigFile() {
        if (varbitIndex != -1) {
            return VarbitDefinition.forSceneryId(varbitIndex);
        }
        return null;
    }

    /**
     * Sets option.
     *
     * @param slot   the slot
     * @param option the option
     */
    public void setOption(int slot, String option) {
        if (slot < 0 || slot >= options.length) {
            throw new IllegalArgumentException(": " + slot);
        }
        options[slot] = option;
    }

    /**
     * Remove option.
     *
     * @param slot the slot
     */
    public void removeOption(int slot) {
        if (slot < 0 || slot >= options.length) {
            throw new IllegalArgumentException("Wrong index: " + slot);
        }
        options[slot] = null;
    }

    /**
     * Print options.
     */
    public void printOptions() {
        System.out.println("Options for object " + id + ":");
        for (int i = 0; i < options.length; i++) {
            System.out.println("Slot " + i + ": " + (options[i] != null ? options[i] : "No options"));
        }
    }

    /**
     * Is mirrored boolean.
     *
     * @return the boolean
     */
    public boolean isMirrored() {
        return mirrored;
    }

    /**
     * Sets mirrored.
     *
     * @param mirrored the mirrored
     */
    public void setMirrored(boolean mirrored) {
        this.mirrored = mirrored;
    }

    /**
     * Get original colors short [ ].
     *
     * @return the short [ ]
     */
    public short[] getOriginalColors() {
        return originalColors;
    }

    /**
     * Get config object ids int [ ].
     *
     * @return the int [ ]
     */
    public int[] getConfigObjectIds() {
        return configObjectIds;
    }

    /**
     * Gets an int 3832.
     *
     * @return the an int 3832
     */
    public static int getAnInt3832() {
        return anInt3832;
    }

    /**
     * Get an int array 3833 int [ ].
     *
     * @return the int [ ]
     */
    public int[] getAnIntArray3833() {
        return anIntArray3833;
    }

    /**
     * Gets an int 3834.
     *
     * @return the an int 3834
     */
    public int getAnInt3834() {
        return anInt3834;
    }

    /**
     * Gets an int 3835.
     *
     * @return the an int 3835
     */
    public int getAnInt3835() {
        return anInt3835;
    }

    /**
     * Gets an int 3836.
     *
     * @return the an int 3836
     */
    public static int getAnInt3836() {
        return anInt3836;
    }

    /**
     * Gets byte 3837.
     *
     * @return the byte 3837
     */
    public byte getaByte3837() {
        return aByte3837;
    }

    /**
     * Gets an int 3838.
     *
     * @return the an int 3838
     */
    public int getAnInt3838() {
        return anInt3838;
    }

    /**
     * Gets contrast.
     *
     * @return the contrast
     */
    public int getContrast() {
        return contrast;
    }

    /**
     * Gets model size z.
     *
     * @return the model size z
     */
    public int getModelSizeZ() {
        return modelSizeZ;
    }

    /**
     * Gets an int 3842.
     *
     * @return the an int 3842
     */
    public static int getAnInt3842() {
        return anInt3842;
    }

    /**
     * Gets an int 3843.
     *
     * @return the an int 3843
     */
    public static int getAnInt3843() {
        return anInt3843;
    }

    /**
     * Gets an int 3844.
     *
     * @return the an int 3844
     */
    public int getAnInt3844() {
        return anInt3844;
    }

    /**
     * Isa boolean 3845 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean3845() {
        return aBoolean3845;
    }

    /**
     * Gets an int 3846.
     *
     * @return the an int 3846
     */
    public static int getAnInt3846() {
        return anInt3846;
    }

    /**
     * Gets byte 3847.
     *
     * @return the byte 3847
     */
    public byte getaByte3847() {
        return aByte3847;
    }

    /**
     * Gets byte 3849.
     *
     * @return the byte 3849
     */
    public byte getaByte3849() {
        return aByte3849;
    }

    /**
     * Gets an int 3850.
     *
     * @return the an int 3850
     */
    public int getAnInt3850() {
        return anInt3850;
    }

    /**
     * Gets an int 3851.
     *
     * @return the an int 3851
     */
    public int getAnInt3851() {
        return anInt3851;
    }

    /**
     * Is blocks land boolean.
     *
     * @return the boolean
     */
    public boolean isBlocksLand() {
        return blocksLand;
    }

    /**
     * Isa boolean 3853 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean3853() {
        return aBoolean3853;
    }

    /**
     * Gets an int 3855.
     *
     * @return the an int 3855
     */
    public int getAnInt3855() {
        return anInt3855;
    }

    /**
     * Is first bool boolean.
     *
     * @return the boolean
     */
    public boolean isFirstBool() {
        return ignoreOnRoute;
    }

    /**
     * Gets an int 3857.
     *
     * @return the an int 3857
     */
    public int getAnInt3857() {
        return anInt3857;
    }

    /**
     * Get recolour palette byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getRecolourPalette() {
        return recolourPalette;
    }

    /**
     * Get an int array 3859 int [ ].
     *
     * @return the int [ ]
     */
    public int[] getAnIntArray3859() {
        return anIntArray3859;
    }

    /**
     * Gets an int 3860.
     *
     * @return the an int 3860
     */
    public int getAnInt3860() {
        return anInt3860;
    }

    @Override
    public String[] getOptions() {
        return options;
    }

    /**
     * Gets varbit id.
     *
     * @return the varbit id
     */
    public int getVarbitID() {
        return varbitIndex;
    }

    /**
     * Get modified colors short [ ].
     *
     * @return the short [ ]
     */
    public short[] getModifiedColors() {
        return modifiedColors;
    }

    /**
     * Gets an int 3865.
     *
     * @return the an int 3865
     */
    public int getAnInt3865() {
        return anInt3865;
    }

    /**
     * Isa boolean 3866 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean3866() {
        return aBoolean3866;
    }

    /**
     * Isa boolean 3867 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean3867() {
        return aBoolean3867;
    }

    /**
     * Is projectile clipped boolean.
     *
     * @return the boolean
     */
    public boolean isProjectileClipped() {
        return projectileClipped;
    }

    /**
     * Get an int array 3869 int [ ].
     *
     * @return the int [ ]
     */
    public int[] getAnIntArray3869() {
        return anIntArray3869;
    }

    /**
     * Isa boolean 3870 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean3870() {
        return aBoolean3870;
    }

    /**
     * Gets size y.
     *
     * @return the size y
     */
    public int getSizeY() {
        return sizeY;
    }

    /**
     * Is casts shadow boolean.
     *
     * @return the boolean
     */
    public boolean isCastsShadow() {
        return castsShadow;
    }

    /**
     * Isa boolean 3873 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean3873() {
        return membersOnly;
    }

    /**
     * Gets third boolean.
     *
     * @return the third boolean
     */
    public boolean getThirdBoolean() {
        return boolean1;
    }

    /**
     * Gets an int 3875.
     *
     * @return the an int 3875
     */
    public int getAnInt3875() {
        return anInt3875;
    }

    /**
     * Gets add object check.
     *
     * @return the add object check
     */
    public int getAddObjectCheck() {
        return animationId;
    }

    /**
     * Gets an int 3877.
     *
     * @return the an int 3877
     */
    public int getAnInt3877() {
        return anInt3877;
    }

    /**
     * Gets brightness.
     *
     * @return the brightness
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     * Gets solid.
     *
     * @return the solid
     */
    public int getSolid() {
        return solid;
    }

    /**
     * Gets an int 3881.
     *
     * @return the an int 3881
     */
    public int getAnInt3881() {
        return anInt3881;
    }

    /**
     * Gets an int 3882.
     *
     * @return the an int 3882
     */
    public int getAnInt3882() {
        return anInt3882;
    }

    /**
     * Gets offset x.
     *
     * @return the offset x
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * Gets loader.
     *
     * @return the loader
     */
    public Object getLoader() {
        return loader;
    }

    /**
     * Gets offset z.
     *
     * @return the offset z
     */
    public int getOffsetZ() {
        return offsetZ;
    }

    /**
     * Gets size x.
     *
     * @return the size x
     */
    public int getSizeX() {
        return sizeX;
    }

    /**
     * Isa boolean 3891 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean3891() {
        return aBoolean3891;
    }

    /**
     * Gets offset multiplier.
     *
     * @return the offset multiplier
     */
    public int getOffsetMultiplier() {
        return offsetMultiplier;
    }

    /**
     * Gets interactive.
     *
     * @return the interactive
     */
    public int getInteractive() {
        return interactive;
    }

    /**
     * Isa boolean 3894 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean3894() {
        return aBoolean3894;
    }

    /**
     * Isa boolean 3895 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean3895() {
        return aBoolean3895;
    }

    /**
     * Gets an int 3896.
     *
     * @return the an int 3896
     */
    public int getAnInt3896() {
        return anInt3896;
    }

    /**
     * Gets config id.
     *
     * @return the config id
     */
    public int getConfigId() {
        return configId;
    }

    /**
     * Geta byte array 3899 byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getaByteArray3899() {
        return aByteArray3899;
    }

    /**
     * Gets an int 3900.
     *
     * @return the an int 3900
     */
    public int getAnInt3900() {
        return anInt3900;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets model size x.
     *
     * @return the model size x
     */
    public int getModelSizeX() {
        return modelSizeX;
    }

    /**
     * Gets an int 3904.
     *
     * @return the an int 3904
     */
    public int getAnInt3904() {
        return anInt3904;
    }

    /**
     * Gets an int 3905.
     *
     * @return the an int 3905
     */
    public int getAnInt3905() {
        return anInt3905;
    }

    /**
     * Isa boolean 3906 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean3906() {
        return aBoolean3906;
    }

    /**
     * Get an int array 3908 int [ ].
     *
     * @return the int [ ]
     */
    public int[] getAnIntArray3908() {
        return anIntArray3908;
    }

    /**
     * Gets contoured ground.
     *
     * @return the contoured ground
     */
    public byte getContouredGround() {
        return contouredGround;
    }

    /**
     * Gets an int 3913.
     *
     * @return the an int 3913
     */
    public int getAnInt3913() {
        return anInt3913;
    }

    /**
     * Gets byte 3914.
     *
     * @return the byte 3914
     */
    public byte getaByte3914() {
        return aByte3914;
    }

    /**
     * Gets offset y.
     *
     * @return the offset y
     */
    public int getOffsetY() {
        return offsetY;
    }

    /**
     * Get an int array array 3916 int [ ] [ ].
     *
     * @return the int [ ] [ ]
     */
    public int[][] getAnIntArrayArray3916() {
        return anIntArrayArray3916;
    }

    /**
     * Gets model size y.
     *
     * @return the model size y
     */
    public int getModelSizeY() {
        return modelSizeY;
    }

    /**
     * Get modified texture colours short [ ].
     *
     * @return the short [ ]
     */
    public short[] getModifiedTextureColours() {
        return modifiedTextureColours;
    }

    /**
     * Get original texture colours short [ ].
     *
     * @return the short [ ]
     */
    public short[] getOriginalTextureColours() {
        return originalTextureColours;
    }

    /**
     * Gets an int 3921.
     *
     * @return the an int 3921
     */
    public int getAnInt3921() {
        return anInt3921;
    }

    /**
     * Gets class 194 3922.
     *
     * @return the class 194 3922
     */
    public Object getaClass194_3922() {
        return aClass194_3922;
    }

    /**
     * Isa boolean 3923 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean3923() {
        return aBoolean3923;
    }

    /**
     * Isa boolean 3924 boolean.
     *
     * @return the boolean
     */
    public boolean isaBoolean3924() {
        return aBoolean3924;
    }

    /**
     * Get model ids int [ ].
     *
     * @return the int [ ]
     */
    public int[] getModelIds() {
        return modelIds;
    }

    /**
     * Has action boolean.
     *
     * @param action the action
     * @return the boolean
     */
    public boolean hasAction(String action) {
        if (options == null) {
            return false;
        }
        for (String option : options) {
            if (option == null) {
                continue;
            }
            if (option.equalsIgnoreCase(action)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets definitions.
     *
     * @return the definitions
     */
    public static Map<Integer, SceneryDefinition> getDefinitions() {
        return DEFINITIONS;
    }

    /**
     * Gets option handler.
     *
     * @param nodeId the node id
     * @param name   the name
     * @return the option handler
     */
    public static OptionHandler getOptionHandler(int nodeId, String name) {
        SceneryDefinition def = forId(nodeId);
        OptionHandler handler = def.getConfiguration("option:" + name);
        if (handler != null) {
            return handler;
        }
        return OPTION_HANDLERS.get(name);
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
     * Is has hidden options boolean.
     *
     * @return the boolean
     */
    public boolean isHasHiddenOptions() {
        return hasHiddenOptions;
    }

    /**
     * Sets has hidden options.
     *
     * @param hasHiddenOptions the has hidden options
     */
    public void setHasHiddenOptions(boolean hasHiddenOptions) {
        this.hasHiddenOptions = hasHiddenOptions;
    }

    /**
     * Gets walking flag.
     *
     * @return the walking flag
     */
    public int getWalkingFlag() {
        return walkingFlag;
    }

    /**
     * Get model configuration int [ ].
     *
     * @return the int [ ]
     */
    public int[] getModelConfiguration() {
        return modelConfiguration;
    }

    /**
     * Sets model configuration.
     *
     * @param modelConfiguration the model configuration
     */
    public void setModelConfiguration(int[] modelConfiguration) {
        this.modelConfiguration = modelConfiguration;
    }

    /**
     * Gets map icon.
     *
     * @return the map icon
     */
    public short getMapIcon() {
        return mapIcon;
    }

    /**
     * Gets container id.
     *
     * @param id the id
     * @return the container id
     */
    public static int getContainerId(int id) {
        return id >>> 8;
    }
}
