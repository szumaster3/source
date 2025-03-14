package core.cache.def.impl;

import com.displee.cache.CacheLibrary;
import com.displee.cache.index.archive.Archive;
import core.cache.Cache;
import core.cache.CacheArchive;
import core.cache.CacheIndex;
import core.net.packet.IoBuffer;
import core.net.packet.PacketHeader;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

import static core.tools.SystemLogger.logCache;

/**
 * The type Iface definition.
 */
public class IfaceDefinition {
    private static HashMap<Integer, IfaceDefinition> defCache = new HashMap<>();
    /**
     * The Id.
     */
    public int id;
    /**
     * The Type.
     */
    public ComponentType type;
    /**
     * The Version.
     */
    public int version;
    /**
     * The Client code.
     */
    public int clientCode;
    /**
     * The Base x.
     */
    public int baseX, /**
     * The Base y.
     */
    baseY;
    /**
     * The Base width.
     */
    public int baseWidth, /**
     * The Base height.
     */
    baseHeight;
    /**
     * The Dyn width.
     */
    public int dynWidth, /**
     * The Dyn height.
     */
    dynHeight;
    /**
     * The X mode.
     */
    public int xMode, /**
     * The Y mode.
     */
    yMode;
    /**
     * The Overlayer.
     */
    public int overlayer;
    /**
     * The Hidden.
     */
    public boolean hidden;
    /**
     * The Scroll max h.
     */
    public int scrollMaxH, /**
     * The Scroll max v.
     */
    scrollMaxV;
    /**
     * The No click through.
     */
    public boolean noClickThrough;
    /**
     * The Sprite id.
     */
    public int spriteId, /**
     * The Active sprite id.
     */
    activeSpriteId;
    /**
     * The Angle 2 d.
     */
    public int angle2d;
    /**
     * The Has alpha.
     */
    public boolean hasAlpha;
    /**
     * The Sprite tiling.
     */
    public boolean spriteTiling;
    /**
     * The Alpha.
     */
    public int alpha;
    /**
     * The Outline thickness.
     */
    public int outlineThickness;
    /**
     * The Shadow color.
     */
    public int shadowColor;
    /**
     * The H flip.
     */
    public boolean hFlip, /**
     * The V flip.
     */
    vFlip;
    /**
     * The Model type.
     */
    public int modelType, /**
     * The Active model type.
     */
    activeModelType;
    /**
     * The Model id.
     */
    public int modelId, /**
     * The Active model id.
     */
    activeModelId;
    /**
     * The Unknown model prop 1.
     */
    public int unknownModelProp_1;
    /**
     * The Unknown model prop 2.
     */
    public int unknownModelProp_2;
    /**
     * The Model x angle.
     */
    public int modelXAngle, /**
     * The Model y angle.
     */
    modelYAngle;
    /**
     * The Model y offset.
     */
    public int modelYOffset;
    /**
     * The Model zoom.
     */
    public int modelZoom;
    /**
     * The Model anim id.
     */
    public int modelAnimId, /**
     * The Active model anim id.
     */
    activeModelAnimId;
    /**
     * The Model ortho.
     */
    public boolean modelOrtho;
    /**
     * The Unknown model prop 3.
     */
    public int unknownModelProp_3;
    /**
     * The Unknown model prop 4.
     */
    public int unknownModelProp_4;
    /**
     * The Unknown model prop 5.
     */
    public boolean unknownModelProp_5;
    /**
     * The Unknown model prop 6.
     */
    public int unknownModelProp_6;
    /**
     * The Unknown model prop 7.
     */
    public int unknownModelProp_7;
    /**
     * The Font.
     */
    public int font;
    /**
     * The Text.
     */
    public String text, /**
     * The Active text.
     */
    activeText;
    /**
     * The V padding.
     */
    public int vPadding;
    /**
     * The Halign.
     */
    public int halign, /**
     * The Valign.
     */
    valign;
    /**
     * The Shadowed.
     */
    public boolean shadowed;
    /**
     * The Color.
     */
    public int color, /**
     * The Active color.
     */
    activeColor, /**
     * The Over color.
     */
    overColor, /**
     * The Unknown color.
     */
    unknownColor;
    /**
     * The Filled.
     */
    public boolean filled;
    /**
     * The Line width.
     */
    public int lineWidth;
    /**
     * The Unknown prop 8.
     */
    public boolean unknownProp_8;
    /**
     * The Unknown int array 1.
     */
    public int[] unknownIntArray_1;
    /**
     * The Unknown int array 2.
     */
    public int[] unknownIntArray_2;
    /**
     * The Unknown byte array 1.
     */
    public byte[] unknownByteArray_1;
    /**
     * The Unknown byte array 2.
     */
    public byte[] unknownByteArray_2;
    /**
     * The Option base.
     */
    public String optionBase;
    /**
     * The Ops.
     */
    public String[] ops;
    /**
     * The Drag deadzone.
     */
    public int dragDeadzone;
    /**
     * The Drag deadtime.
     */
    public int dragDeadtime;
    /**
     * The Drag render behavior.
     */
    public boolean dragRenderBehavior;
    /**
     * The Op circumfix.
     */
    public String opCircumfix, /**
     * The Op suffix.
     */
    opSuffix, /**
     * The Option.
     */
    option;
    /**
     * The Unknown prop 9.
     */
    public int unknownProp_9, /**
     * The Unknown prop 10.
     */
    unknownProp_10, /**
     * The Unknown prop 11.
     */
    unknownProp_11;
    /**
     * The Cs 1 comparison operands.
     */
    public int[] cs1ComparisonOperands, /**
     * The Cs 1 comparison opcodes.
     */
    cs1ComparisonOpcodes;
    /**
     * The Cs 1 scripts.
     */
    public int[][] cs1Scripts;
    /**
     * The Obj counts.
     */
    public int[] objCounts;
    /**
     * The Obj types.
     */
    public int[] objTypes;
    /**
     * The Inv margin x.
     */
    public int invMarginX, /**
     * The Inv margin y.
     */
    invMarginY;
    /**
     * The Inv offset x.
     */
    public int[] invOffsetX, /**
     * The Inv offset y.
     */
    invOffsetY;
    /**
     * The Inv sprite.
     */
    public int[] invSprite;
    /**
     * The Button type.
     */
    public int buttonType;
    /**
     * The Inv options.
     */
    public String[] invOptions;
    /**
     * The Scripts.
     */
    public LinkedScripts scripts;
    /**
     * The Triggers.
     */
    public ScriptTriggers triggers;
    /**
     * The Children.
     */
    public IfaceDefinition[] children;
    /**
     * The Parent.
     */
    public int parent;

    @Override
    public String toString() {
        if (parent == id) {
            return "[IF " + id + " (P)]";
        } else {
            return "[IF " + (id >> 16) +  ", CH " + (id & 0xFF) + " (" + type.name() + ")]";
        }
    }

    /**
     * For id iface definition.
     *
     * @param id the id
     * @return the iface definition
     */
    public static IfaceDefinition forId (int id) {
        if (defCache.containsKey(id)) {
            return defCache.get(id);
        }
        IfaceDefinition def = loadAndParse(id);
        defCache.put(id, def);
        return def;
    }

    /**
     * For id iface definition.
     *
     * @param id    the id
     * @param child the child
     * @return the iface definition
     */
    public static IfaceDefinition forId (int id, int child) {
        if (defCache.containsKey(child + (id << 16)))
            return defCache.get(child + (id << 16));
        IfaceDefinition def = forId(id);
        return def.children[child];
    }

    private static IfaceDefinition loadAndParse (int id) {
        IfaceDefinition def = new IfaceDefinition();
        int childCount = Cache.getIndexCapacity(CacheIndex.COMPONENTS);
        def.children = new IfaceDefinition[childCount];
        def.id = id;
        def.parent = id;
        for (int i = 0; i < childCount; i++) {
            def.children[i] = parseChild(id, i);
        }
        return def;
    }

    private static IfaceDefinition parseChild (int id, int childIndex) {
        IfaceDefinition def = new IfaceDefinition();
        def.id = childIndex + (id << 16);
        def.parent = id;
        defCache.put(def.id, def);

        byte[] dataRaw = Cache.getData(CacheIndex.COMPONENTS, id, childIndex);
        if (dataRaw == null) return def;

        IoBuffer data = new IoBuffer(-1, PacketHeader.NORMAL, ByteBuffer.wrap(dataRaw));

        if (dataRaw[0] == -1)
            decodeIf3 (def, data);
        else
            decodeIf1 (def, data);

        return def;
    }


    private static void decodeIf1 (IfaceDefinition def, IoBuffer data) {
        data.g1();
        def.version = 1;
        def.type = ComponentType.values()[data.g1()];
        def.buttonType = data.g1();
        def.clientCode = data.g2();
        def.baseX = data.g2b();
        def.baseY = data.g2b();
        def.baseWidth = data.g2();
        def.baseHeight = data.g2();
        def.dynWidth = def.dynHeight = 0;
        def.xMode = def.yMode = 0;
        def.alpha = data.g1();
        def.overlayer = data.g2();
        if (def.overlayer == 65535)
            def.overlayer = -1;
        else def.overlayer += def.id & 0xFFFF0000;
        def.unknownProp_11 = data.g2();

        int unknownLocal_1 = data.g1();
        if (unknownLocal_1 > 0) {
            def.cs1ComparisonOpcodes = new int[unknownLocal_1];
            def.cs1ComparisonOperands = new int[unknownLocal_1];
            for (int i = 0; i < unknownLocal_1; i++) {
                def.cs1ComparisonOpcodes[i] = data.g1();
                def.cs1ComparisonOperands[i] = data.g2();
            }
        }

        int unknownLocal_2 = data.g1();
        int unknownLocal_3;
        if (unknownLocal_2 > 0) {
            def.cs1Scripts = new int[unknownLocal_2][];
            for (int i = 0; i < unknownLocal_2; i++) {
                unknownLocal_3 = data.g2();
                def.cs1Scripts[i] = new int[unknownLocal_3];
                for (int j = 0; j < unknownLocal_3; j++) {
                    def.cs1Scripts[i][j] = data.g2();
                    if (def.cs1Scripts[i][j] == 65535)
                        def.cs1Scripts[i][j] = -1;
                }
            }
        }

        int flags = 0;
        if (def.type == ComponentType.SCROLLABLE) {
            def.scrollMaxV = data.g2();
            def.hidden = data.g1() == 1;
        }
        if (def.type == ComponentType.UNKNOWN_1) {
            data.g2();
            data.g1();
        }
        if (def.type == ComponentType.UNKNOWN_2) {
            def.dynHeight = 3;
            def.objCounts = new int[def.baseWidth * def.baseHeight];
            def.objTypes = new int[def.baseWidth * def.baseHeight];
            def.dynWidth = 3;
            int unknownLocal_4 = data.g1();
            int unknownLocal_5 = data.g1();
            if (unknownLocal_4 == 1)
                flags = 268435456;
            int unknownLocal_6 = data.g1();
            if (unknownLocal_5 == 1)
                flags |= 0x40000000;
            if (unknownLocal_6 == 1)
                flags |= Integer.MAX_VALUE;
            int unknownLocal_7 = data.g1();
            if (unknownLocal_7 == 1)
                flags |= 0x20000000;
            def.invMarginX = data.g1();
            def.invMarginY = data.g1();
            def.invOffsetX = new int[20];
            def.invOffsetY = new int[20];
            def.invSprite = new int[20];
            for (int i = 0; i < 20; i++) {
                int hasSprite = data.g1();
                if (hasSprite == 1) {
                    def.invOffsetX[i] = data.g2b();
                    def.invOffsetY[i] = data.g2b();
                    def.invSprite[i] = data.g4();
                } else {
                    def.invSprite[i] = -1;
                }
            }
            def.invOptions = new String[5];
            for (int i = 0; i < 5; i++) {
                String option = data.getJagString();
                if (option.length() > 0) {
                    def.invOptions[i] = option;
                    flags |= 0x1 << i + 23;
                }
            }
        }
        if (def.type == ComponentType.UNKNOWN_3) {
            def.filled = data.g1() == 1;
        }
        if (def.type == ComponentType.TEXT || def.type == ComponentType.UNKNOWN_1) {
            def.halign = data.g1();
            def.valign = data.g1();
            def.vPadding = data.g1();
            def.font = data.g2();
            if (def.font == 65535)
                def.font = -1;
            def.shadowed = data.g1() == 1;
        }
        if (def.type == ComponentType.TEXT) {
            def.text = data.getJagString();
            def.activeText = data.getJagString();
        }
        if (def.type == ComponentType.UNKNOWN_1 || def.type == ComponentType.UNKNOWN_3 || def.type == ComponentType.TEXT) {
            def.color = data.g4();
        }
        if (def.type == ComponentType.UNKNOWN_3 || def.type == ComponentType.TEXT) {
            def.activeColor = data.g4();
            def.overColor = data.g4();
            def.unknownColor = data.g4();
        }
        if (def.type == ComponentType.SPRITE) {
            def.spriteId = data.g4();
            def.activeSpriteId = data.g4();
        }
        if (def.type == ComponentType.MODEL) {
            def.modelType = 1;
            def.modelId = data.g2();
            if (def.modelId == 65535)
                def.modelId = -1;
            def.activeModelType = 1;
            def.activeModelId = data.g2();
            if (def.activeModelId == 65535)
                def.activeModelId = -1;
            def.modelAnimId = data.g2();
            if (def.modelAnimId == 65535)
                def.modelAnimId = -1;
            def.activeModelAnimId = data.g2();
            if (def.activeModelAnimId == 65535)
                def.activeModelAnimId = -1;
            def.modelZoom = data.g2();
            def.modelXAngle = data.g2();
            def.modelYAngle = data.g2();
        }
        if (def.type == ComponentType.UNKNOWN_7) {
            def.dynHeight = def.dynWidth = 3;
            def.objCounts = new int [def.baseHeight * def.baseWidth];
            def.objTypes = new int [def.baseHeight * def.baseWidth];
            def.halign = data.g1();
            def.font = data.g2();
            if (def.font == 65535)
                def.font = -1;
            def.shadowed = data.g1() == 1;
            def.color = data.g4();
            def.invMarginX = data.g2b();
            def.invMarginY = data.g2b();
            int invHasOptions = data.g1();
            if (invHasOptions == 1) {
                flags |= 0x40000000;
            }
            def.invOptions = new String[5];
            for (int i = 0; i < 5; i++) {
                String option = data.getJagString();
                if (option.length() > 0) {
                    def.invOptions[i] = option;
                    flags |= 0x1 << i + 23;
                }
            }
        }
        if (def.type == ComponentType.UNKNOWN_8)
            def.text = data.getJagString();
        if (def.buttonType == 2 || def.type == ComponentType.UNKNOWN_2) {
            def.opCircumfix = data.getJagString();
            def.opSuffix = data.getJagString();
            int unknownLocal_4 = data.g2() & 0x3F;
            flags |= unknownLocal_4 << 11;
        }
        if (def.buttonType == 1 || def.buttonType == 4 || def.buttonType == 5 || def.buttonType == 6) {
            def.option = data.getJagString();
            if (def.option.length() == 0) {
                switch (def.buttonType) {
                    case 1:
                        def.option = "Ok";
                        break;
                    case 4:
                    case 5:
                        def.option = "Select";
                        break;
                    case 6:
                        def.option = "Continue";
                        break;
                }
            }
        }
        if (def.buttonType == 1 || def.buttonType == 4 || def.buttonType == 5) {
            flags |= 0x400000;
        }
        if (def.buttonType == 6) {
            flags |= 0x1;
        }
    }

    private static void decodeIf3 (IfaceDefinition def, IoBuffer data) {
        data.g1();
        def.version = 3;
        int type = data.g1();
        if ((type & 0x80) != 0) {
            type &= 0x7F;
            data.getJagString();
        }
        def.type = ComponentType.values()[type];
        def.clientCode = data.g2();
        def.baseX = data.g2b();
        def.baseY = data.g2b();
        def.baseWidth = data.g2();
        def.baseHeight = data.g2();
        def.dynWidth = data.g1b();
        def.dynHeight = data.g1b();
        def.yMode = data.g1b();
        def.xMode = data.g1b();
        def.overlayer = data.g2();
        if (def.overlayer == 65535) {
            def.overlayer = -1;
        } else {
            def.overlayer = (def.id & 0xFFFF0000) + def.overlayer;
        }
        def.hidden = data.g1() == 1;
        parseIf3Type (def, data);

        int unknownLocal_1 = data.g3();
        int unknownLocal_2 = data.g1();
        int unknownLocal_3;
        if (unknownLocal_2 != 0) {
            def.unknownIntArray_1 = new int[10];
            def.unknownByteArray_1 = new byte[10];
            def.unknownByteArray_2 = new byte[10];

            while (unknownLocal_2 != 0) {
                unknownLocal_3 = (unknownLocal_2 >> 4) - 1;
                unknownLocal_2 = data.g1() | unknownLocal_2 << 8;
                unknownLocal_2 &= 0xFFF;
                if (unknownLocal_2 == 4095) {
                    def.unknownIntArray_1[unknownLocal_3] = -1;
                } else {
                    def.unknownIntArray_1[unknownLocal_3] = unknownLocal_2;
                }
                def.unknownByteArray_2[unknownLocal_3] = (byte) data.g1b();
                def.unknownByteArray_1[unknownLocal_3] = (byte) data.g1b();
                unknownLocal_2 = data.g1();
            }
        }

        def.optionBase = data.getJagString();
        unknownLocal_2 = data.g1();
        int opCount = unknownLocal_2 & 0xF;
        if (opCount > 0) {
            def.ops = new String[opCount];
            for (int i = 0; i < opCount; i++) {
                def.ops[i] = data.getJagString();
            }
        }

        int unknownLocal_5;
        int unknownLocal_6 = unknownLocal_2 >> 4;
        if (unknownLocal_6 > 0) {
            unknownLocal_5 = data.g1();
            def.unknownIntArray_2 = new int[unknownLocal_5 + 1];
            for (int i = 0; i < def.unknownIntArray_2.length; i++)
                def.unknownIntArray_2[i] = -1;
            def.unknownIntArray_2[unknownLocal_5] = data.g2();
        }
        if (unknownLocal_6 > 1) {
            unknownLocal_5 = data.g1();
            def.unknownIntArray_2[unknownLocal_5] = data.g2();
        }
        def.dragDeadzone = data.g1();
        def.dragDeadtime = data.g1();
        def.dragRenderBehavior = data.g1() == 1;
        unknownLocal_5 = -1;
        def.opCircumfix = data.getJagString();

        if ((unknownLocal_1 >> 11 & 0x7F) != 0) {
            unknownLocal_5 = data.g2();
            def.unknownProp_9 = data.g2();
            if (unknownLocal_5 == 65535)
                unknownLocal_5 = -1;
            if (def.unknownProp_9 == 65535)
                def.unknownProp_9 = -1;
            def.unknownProp_10 = data.g2();
            if (def.unknownProp_10 == 65535)
                def.unknownProp_10 = -1;
        }

        def.scripts = new LinkedScripts();
        def.scripts.unknown = parseIf3ScriptArgs(data);
        def.scripts.onMouseOver = parseIf3ScriptArgs(data);
        def.scripts.onMouseLeave = parseIf3ScriptArgs(data);
        def.scripts.onUseWith = parseIf3ScriptArgs(data);
        def.scripts.onUse = parseIf3ScriptArgs(data);
        def.scripts.onVarpTransmit = parseIf3ScriptArgs(data);
        def.scripts.onInvTransmit = parseIf3ScriptArgs(data);
        def.scripts.onStatTransmit = parseIf3ScriptArgs(data);
        def.scripts.onTimer = parseIf3ScriptArgs(data);
        def.scripts.onOptionClick = parseIf3ScriptArgs(data);
        def.scripts.onMouseRepeat = parseIf3ScriptArgs(data);
        def.scripts.onClickRepeat = parseIf3ScriptArgs(data);
        def.scripts.onDrag = parseIf3ScriptArgs(data);
        def.scripts.onRelease = parseIf3ScriptArgs(data);
        def.scripts.onHold = parseIf3ScriptArgs(data);
        def.scripts.onDragStart = parseIf3ScriptArgs(data);
        def.scripts.onDragRelease = parseIf3ScriptArgs(data);
        def.scripts.onScroll = parseIf3ScriptArgs(data);
        def.scripts.onVarcTransmit = parseIf3ScriptArgs(data);
        def.scripts.onVarcstrTransmit = parseIf3ScriptArgs(data);

        def.triggers = new ScriptTriggers();
        def.triggers.varpTriggers = parseIf3Triggers(data);
        def.triggers.inventoryTriggers = parseIf3Triggers(data);
        def.triggers.statTriggers = parseIf3Triggers(data);
        def.triggers.varcTriggers = parseIf3Triggers(data);
        def.triggers.varcstrTriggers = parseIf3Triggers(data);
    }

    private static ScriptArgs parseIf3ScriptArgs (IoBuffer data) {
        int argCount = data.g1();
        if (argCount == 0)
            return null;
        Object[] argArray = new Object[argCount];
        for (int i = 0; i < argCount; i++) {
            int type = data.g1();
            if (type == 0) {
                int datum = Integer.valueOf(data.g4());
                argArray[i] = datum;
            } else if (type == 1) {
                String datum = data.getJagString();
                argArray[i] = datum;
            }
        }
        return new ScriptArgs((int) argArray[0], Arrays.copyOfRange(argArray, 1, argArray.length));
    }

    private static int[] parseIf3Triggers (IoBuffer data) {
        int triggerCount = data.g1();
        if (triggerCount == 0)
            return null;
        int[] triggers = new int[triggerCount];
        for (int i = 0; i < triggerCount; i++) {
            triggers[i] = data.g4();
        }
        return triggers;
    }

    private static void parseIf3Type (IfaceDefinition def, IoBuffer data) {
        switch (def.type) {
            case SCROLLABLE:
                def.scrollMaxH = data.g2();
                def.scrollMaxV = data.g2();
                def.noClickThrough = data.g1() == 1;
                break;
            case SPRITE:
                def.spriteId = data.g4();
                def.angle2d = data.g2();
                int spriteFlags = data.g1();
                def.hasAlpha = (spriteFlags & 0x2) != 0;
                def.spriteTiling = (spriteFlags & 0x1) != 0;
                def.alpha = data.g1();
                def.outlineThickness = data.g1();
                def.shadowColor = data.g4();
                def.vFlip = data.g1() == 1;
                def.hFlip = data.g1() == 1;
                break;
            case MODEL:
                def.modelType = 1;
                def.modelId = data.g2();
                if (def.modelId == 65535)
                    def.modelId = -1;
                def.unknownModelProp_1 = data.g2b();
                def.unknownModelProp_2 = data.g2b();
                def.modelXAngle = data.g2();
                def.modelYAngle = data.g2();
                def.modelYOffset = data.g2();
                def.modelZoom = data.g2();
                def.modelAnimId = data.g2();
                if (def.modelAnimId == 65535)
                    def.modelAnimId = -1;
                def.modelOrtho = data.g1() == 1;
                def.unknownModelProp_3 = data.g2();
                def.unknownModelProp_4 = data.g2();
                def.unknownModelProp_5 = data.g1() == 1;
                if (def.dynWidth != 0)
                    def.unknownModelProp_6 = data.g2();
                if (def.dynHeight != 0)
                    def.unknownModelProp_7 = data.g2();
                break;
            case TEXT:
                def.font = data.g2();
                if (def.font == 65535)
                    def.font = -1;
                def.text = data.getJagString();
                def.vPadding = data.g1();
                def.halign = data.g1();
                def.valign = data.g1();
                def.shadowed = data.g1() == 1;
                def.color = data.g4();
                break;
            case UNKNOWN_3:
                def.color = data.g4();
                def.filled = data.g1() == 1;
                def.alpha = data.g1();
                break;
            case UNKNOWN_9:
                def.lineWidth = data.g1();
                def.color = data.g4();
                def.unknownProp_8 = data.g1() == 1;
                break;
        }
    }

    /**
     * Add option.
     *
     * @param ifaceId   the iface id
     * @param childId   the child id
     * @param newOption the new option
     */
    public static void addOption(int ifaceId, int childId, String newOption) {
        IfaceDefinition component = forId(ifaceId, childId);
        if (component == null) return;

        if (component.ops == null) {
            component.ops = new String[]{newOption};
        } else {
            String[] newOps = Arrays.copyOf(component.ops, component.ops.length + 1);
            newOps[component.ops.length] = newOption;
            component.ops = newOps;
        }
    }

    /**
     * Remove option.
     *
     * @param ifaceId        the iface id
     * @param childId        the child id
     * @param optionToRemove the option to remove
     */
    public static void removeOption(int ifaceId, int childId, String optionToRemove) {
        IfaceDefinition component = forId(ifaceId, childId);
        if (component == null || component.ops == null) return;

        component.ops = Arrays.stream(component.ops)
                .filter(option -> !option.equals(optionToRemove))
                .toArray(String[]::new);
    }

    /**
     * Sets text.
     *
     * @param ifaceId the iface id
     * @param childId the child id
     * @param newText the new text
     */
    public static void setText(int ifaceId, int childId, String newText) {
        IfaceDefinition component = forId(ifaceId, childId);
        if (component != null) {
            component.text = newText;
        }
    }

    /**
     * Sets color.
     *
     * @param ifaceId  the iface id
     * @param childId  the child id
     * @param newColor the new color
     */
    public static void setColor(int ifaceId, int childId, int newColor) {
        IfaceDefinition component = forId(ifaceId, childId);
        if (component != null) {
            component.color = newColor;
        }
    }

    /**
     * Sets hidden.
     *
     * @param ifaceId the iface id
     * @param childId the child id
     * @param hidden  the hidden
     */
    public static void setHidden(int ifaceId, int childId, boolean hidden) {
        IfaceDefinition component = forId(ifaceId, childId);
        if (component != null) {
            component.hidden = hidden;
        }
    }

    /**
     * Update component.
     *
     * @param ifaceId   the iface id
     * @param childId   the child id
     * @param newX      the new x
     * @param newY      the new y
     * @param newWidth  the new width
     * @param newHeight the new height
     */
    public static void updateComponent(int ifaceId, int childId, int newX, int newY, int newWidth, int newHeight) {
        IfaceDefinition component = forId(ifaceId, childId);
        if (component != null) {
            component.baseX = newX;
            component.baseY = newY;
            component.baseWidth = newWidth;
            component.baseHeight = newHeight;
        }
    }
}
