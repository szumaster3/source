package content.global.skill.cooking;

import core.game.node.item.Item;
import org.rs.consts.Items;

import java.util.HashMap;

/**
 * The enum Cookable item.
 */
public enum CookableItem {
    /**
     * Chicken.
     */
    CHICKEN(Items.COOKED_CHICKEN_2140, Items.RAW_CHICKEN_2138, Items.BURNT_CHICKEN_2144, 1, 30.0, 128, 512, 128, 512),
    /**
     * Ugthanki meat.
     */
    UGTHANKI(Items.UGTHANKI_MEAT_1861, Items.RAW_UGTHANKI_MEAT_1859, Items.BURNT_MEAT_2146, 1, 40.0, 40, 252, 30, 253),
    /**
     * Turkey drumstick.
     */
    TURKEY_DRUMSTICK(Items.COOKED_TURKEY_DRUMSTICK_14543, Items.RAW_TURKEY_DRUMSTICK_14542, Items.BURNT_TURKEY_DRUMSTICK_14544, 1, 30.0, 128, 512, 128, 512),
    /**
     * Turkey.
     */
    TURKEY(Items.COOKED_TURKEY_14540, Items.RAW_TURKEY_14539, Items.BURNT_TURKEY_14541, 1, 30.0, 128, 512, 128, 512),
    /**
     * Rabbit.
     */
    RABBIT(Items.COOKED_RABBIT_3228, Items.RAW_RABBIT_3226, Items.BURNT_RABBIT_7222, 1, 30.0, 128, 512, 128, 512),
    /**
     * Crab.
     */
    CRAB(7521, 7518, 7520, 21, 100.0, 57, 377, 57, 377),
    /**
     * Chompy.
     */
    CHOMPY(2878, 2876, 2880, 30, 100.0, 200, 255, 200, 255),
    /**
     * Jubbly.
     */
    JUBBLY(7568, 7566, 7570, 41, 160.0, 195, 250, 195, 250),
    /**
     * Crayfish.
     */
    CRAYFISH(13433, 13435, 13437, 1, 30.0, 128, 512, 128, 512),
    /**
     * Shrimp.
     */
    SHRIMP(315, 317, 7954, 1, 30.0, 128, 512, 128, 512),
    /**
     * Karambwanji.
     */
    KARAMBWANJI(3151, 3150, 592, 1, 10.0, 200, 400, 200, 400),
    /**
     * Sardine.
     */
    SARDINE(325, 327, 369, 1, 40.0, 118, 492, 118, 492),
    /**
     * Anchovies.
     */
    ANCHOVIES(319, 321, 323, 1, 30.0, 128, 512, 128, 512),
    /**
     * Herring.
     */
    HERRING(347, 345, 357, 5, 50.0, 108, 472, 108, 472),
    /**
     * Mackerel.
     */
    MACKEREL(355, 353, 357, 10, 60.0, 98, 452, 98, 452),
    /**
     * Trout.
     */
    TROUT(333, 335, 343, 15, 70.0, 88, 432, 88, 432),
    /**
     * Cod.
     */
    COD(339, 341, 343, 18, 75.0, 83, 422, 88, 432),
    /**
     * Pike.
     */
    PIKE(351, 349, 343, 20, 80.0, 78, 412, 78, 412),
    /**
     * Salmon.
     */
    SALMON(329, 331, 343, 25, 90.0, 68, 392, 68, 392),
    /**
     * Slimy eel.
     */
    SLIMY_EEL(3381, 3379, 3383, 28, 95.0, 63, 382, 63, 382),
    /**
     * Tuna.
     */
    TUNA(361, 359, 367, 30, 100.0, 58, 372, 58, 372),
    /**
     * Rainbow fish.
     */
    RAINBOW_FISH(10136, 10138, 10140, 35, 110.0, 56, 370, 56, 370),
    /**
     * Cave eel.
     */
    CAVE_EEL(5003, 5001, 5002, 38, 115.0, 38, 332, 38, 332),
    /**
     * Lobster.
     */
    LOBSTER(379, 377, 381, 40, 120.0, 38, 332, 38, 332),
    /**
     * Bass.
     */
    BASS(365, 363, 367, 43, 130.0, 33, 312, 33, 312),
    /**
     * Swordfish.
     */
    SWORDFISH(373, 371, 375, 45, 140.0, 18, 292, 30, 310),
    /**
     * Lava eel.
     */
    LAVA_EEL(2149, 2148, 3383, 53, 30.0, 256, 256, 256, 256),
    /**
     * Monkfish.
     */
    MONKFISH(7946, 7944, 7948, 62, 150.0, 11, 275, 13, 280),
    /**
     * Shark.
     */
    SHARK(385, 383, 387, 80, 210.0, 1, 202, 1, 232),
    /**
     * Sea turtle.
     */
    SEA_TURTLE(397, 395, 399, 82, 212.0, 1, 202, 1, 222),
    /**
     * Manta ray.
     */
    MANTA_RAY(391, 389, 393, 91, 216.0, 1, 202, 1, 222),
    /**
     * Karambwan.
     */
    KARAMBWAN(3144, 3142, 3146, 30, 190.0, 70, 255, 70, 255),
    /**
     * Thin snail.
     */
    THIN_SNAIL(3369, 3363, 3375, 12, 70.0, 93, 444, 93, 444),
    /**
     * Lean snail.
     */
    LEAN_SNAIL(3371, 3365, 3375, 17, 80.0, 85, 428, 93, 444),
    /**
     * Fat snail.
     */
    FAT_SNAIL(3373, 3367, 3375, 22, 95.0, 73, 402, 73, 402),
    /**
     * Bread.
     */
    BREAD(2309, 2307, 2311, 1, 40.0, 118, 492, 118, 492),
    /**
     * Pitta bread.
     */
    PITTA_BREAD(1865, 1863, 1867, 58, 40.0, 118, 492, 118, 492),
    /**
     * Cake.
     */
    CAKE(1891, 1889, 1903, 40, 180.0, 0, 0, 38, 332),
    /**
     * Beef.
     */
    BEEF(2142, 2132, 2146, 1, 30.0, 128, 512, 128, 512),
    /**
     * Rat meat.
     */
    RAT_MEAT(2142, 2134, 2146, 1, 30.0, 128, 512, 128, 512),
    /**
     * Bear meat.
     */
    BEAR_MEAT(2142, 2136, 2146, 1, 30.0, 128, 512, 128, 512),
    /**
     * Yak meat.
     */
    YAK_MEAT(2142, 10816, 2146, 1, 30.0, 128, 512, 128, 512),
    /**
     * Skewer chompy.
     */
    SKEWER_CHOMPY(2878, 7230, 2880, 30, 100.0, 200, 255, 200, 255),
    /**
     * Roasted chompy.
     */
    ROASTED_CHOMPY(Items.COOKED_CHOMPY_7228, Items.COOKED_CHOMPY_7229, Items.BURNT_CHOMPY_7226, 30, 100.0, 200, 255, 200, 255),
    /**
     * Skewer roast rabbit.
     */
    SKEWER_ROAST_RABBIT(7223, 7224, 7222, 16, 72.0, 160, 255, 160, 255),
    /**
     * Skewer roast bird.
     */
    SKEWER_ROAST_BIRD(9980, 9984, 9982, 11, 62.0, 155, 255, 155, 255),
    /**
     * Skewer roast beast.
     */
    SKEWER_ROAST_BEAST(9988, 9992, 9990, 21, 82.5, 180, 255, 180, 255),
    /**
     * Redberry pie.
     */
    REDBERRY_PIE(2325, 2321, 2329, 10, 78.0, 0, 0, 98, 452),
    /**
     * Meat pie.
     */
    MEAT_PIE(2327, 2319, 2329, 20, 110.0, 0, 0, 78, 412),
    /**
     * Mud pie.
     */
    MUD_PIE(7170, 7168, 2329, 29, 128.0, 0, 0, 58, 372),
    /**
     * Apple pie.
     */
    APPLE_PIE(2323, 2317, 2329, 30, 130.0, 0, 0, 58, 372),
    /**
     * Garden pie.
     */
    GARDEN_PIE(7178, 7176, 2329, 34, 138.0, 0, 0, 48, 352),
    /**
     * Fish pie.
     */
    FISH_PIE(7188, 7186, 2329, 47, 164.0, 0, 0, 38, 332),
    /**
     * Admiral pie.
     */
    ADMIRAL_PIE(7198, 7196, 2329, 70, 210.0, 0, 0, 15, 270),
    /**
     * Wild pie.
     */
    WILD_PIE(7208, 7206, 2329, 85, 240.0, 0, 0, 1, 222),
    /**
     * Summer pie.
     */
    SUMMER_PIE(7218, 7216, 2329, 95, 260.0, 0, 0, 1, 212),
    /**
     * Pizza plain.
     */
    PIZZA_PLAIN(2289, 2287, 2305, 35, 143.0, 0, 0, 48, 352),
    /**
     * Bowl stew.
     */
    BOWL_STEW(2003, 2001, 2005, 25, 117.0, 68, 392, 68, 392),
    /**
     * Bowl curry.
     */
    BOWL_CURRY(2011, 2009, 2013, 60, 280.0, 38, 332, 38, 332),
    /**
     * Bowl nettle.
     */
    BOWL_NETTLE(4239, 4237, 1923, 20, 52.0, 78, 412, 78, 412),
    /**
     * Bowl of hot water.
     */
    BOWL_OF_HOT_WATER(4456, 1921, 1923, 0, 0.0, 128, 512, 128, 512),
    /**
     * Bowl egg.
     */
    BOWL_EGG(7078, 7076, 7090, 13, 50.0, 0, 0, 90, 438),
    /**
     * Bowl onion.
     */
    BOWL_ONION(7084, 1871, 7092, 43, 60.0, 36, 322, 36, 322),
    /**
     * Bowl mushroom.
     */
    BOWL_MUSHROOM(Items.FRIED_MUSHROOMS_7082, Items.SLICED_MUSHROOMS_7080, Items.BURNT_MUSHROOM_7094, 46, 60.0, 16, 282, 16, 282),
    /**
     * Baked potato.
     */
    BAKED_POTATO(6701, 1942, 6699, 7, 15.0, 0, 0, 108, 472),
    /**
     * Cup of hot water.
     */
    CUP_OF_HOT_WATER(4460, 4458, 1980, 0, 0.0, 128, 512, 128, 512),
    /**
     * Sweetcorn.
     */
    SWEETCORN(5988, 5986, 5990, 28, 104.0, 78, 412, 78, 412),
    /**
     * Barley malt.
     */
    BARLEY_MALT(6008, 6006, 6008, 1, 1.0, 0, 0, 0, 0),
    /**
     * Raw oomlie.
     */
    RAW_OOMLIE(2337, 0, 2426, 50, 0.0, 0, 0, 0, 0),
    /**
     * Oomlie wrap.
     */
    OOMLIE_WRAP(2343, 2341, 2345, 50, 30.0, 106, 450, 112, 476),
    /**
     * Seaweed.
     */
    SEAWEED(401, 0, 1781, 0, 0.0, 0, 0, 0, 0),
    /**
     * Sinew.
     */
    SINEW(9436, 2132, 9436, 0, 3.0, 0, 0, 0, 0),
    /**
     * Swamp paste.
     */
    SWAMP_PASTE(1941, 1940, 1941, 0, 2.0, 0, 0, 0, 0),
    /**
     * Swamp weed.
     */
    SWAMP_WEED(1781, 10978, 1781, 0, 3.0, 0, 0, 0, 0);
    /**
     * The cooking map.
     */
    public final static HashMap<Integer, CookableItem> cookingMap = new HashMap<>();
    /**
     * The intentional burn.
     */
    public final static HashMap<Integer, CookableItem> intentionalBurnMap = new HashMap<>();
    /**
     * The gauntlet values.
     */
    public final static HashMap<Integer, int[]> gauntletValues = new HashMap<>();
    /**
     * The lumbridge range values.
     */
    public final static HashMap<Integer, int[]> lumbridgeRangeValues = new HashMap<>();
    /**
     * The Cooked.
     */
    public final int cooked,
    /**
     * Raw cookable item.
     */
    raw,
    /**
     * Burnt cookable item.
     */
    burnt,
    /**
     * Level cookable item.
     */
    level,
    /**
     * Low cookable item.
     */
    low,
    /**
     * High cookable item.
     */
    high,
    /**
     * Low range cookable item.
     */
    lowRange,
    /**
     * High range cookable item.
     */
    highRange;
    /**
     * The Experience.
     */
    public final double experience;
	
	CookableItem(int cooked, int raw, int burnt, int level, double experience, int low, int high, int lowRange, int highRange) {
		this.cooked = cooked;
		this.raw = raw;
		this.burnt = burnt;
		this.level = level;
		this.experience = experience;
		this.low = low;
		this.high = high;
		this.lowRange = lowRange;
		this.highRange = highRange;
	}
	
	static {
		for (CookableItem item : values()) {
			cookingMap.putIfAbsent(item.raw, item);
			intentionalBurnMap.putIfAbsent(item.cooked, item);
		}
		
		gauntletValues.put(Items.RAW_LOBSTER_377, new int[]{55, 368});
		gauntletValues.put(Items.RAW_SWORDFISH_371, new int[]{30, 310});
		gauntletValues.put(Items.RAW_MONKFISH_7944, new int[]{24, 290});
		gauntletValues.put(Items.RAW_SHARK_383, new int[]{15, 270});
		
		lumbridgeRangeValues.put(Items.BREAD_DOUGH_2307, new int[]{128, 512});
		lumbridgeRangeValues.put(Items.RAW_BEEF_2132, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_RAT_MEAT_2134, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_BEAR_MEAT_2136, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_YAK_MEAT_10816, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_CHICKEN_2138, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_SHRIMPS_317, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_ANCHOVIES_321, new int[]{138, 532});
		lumbridgeRangeValues.put(Items.RAW_SARDINE_327, new int[]{128, 512});
		lumbridgeRangeValues.put(Items.RAW_HERRING_345, new int[]{118, 492});
		lumbridgeRangeValues.put(Items.RAW_MACKEREL_353, new int[]{108, 472});
		lumbridgeRangeValues.put(Items.UNCOOKED_BERRY_PIE_2321, new int[]{108, 462});
		lumbridgeRangeValues.put(Items.THIN_SNAIL_3363, new int[]{103, 464});
		lumbridgeRangeValues.put(Items.RAW_TROUT_335, new int[]{98, 452});
		lumbridgeRangeValues.put(Items.LEAN_SNAIL_3365, new int[]{95, 448});
		lumbridgeRangeValues.put(Items.RAW_COD_341, new int[]{93, 442});
		lumbridgeRangeValues.put(Items.RAW_PIKE_349, new int[]{88, 432});
		lumbridgeRangeValues.put(Items.UNCOOKED_MEAT_PIE_2319, new int[]{88, 432});
		lumbridgeRangeValues.put(Items.FAT_SNAIL_3367, new int[]{83, 422});
		lumbridgeRangeValues.put(Items.UNCOOKED_STEW_2001, new int[]{78, 412});
		lumbridgeRangeValues.put(Items.RAW_SALMON_331, new int[]{78, 402});
	}

    /**
     * For id cookable item.
     *
     * @param id the id
     * @return the cookable item
     */
    public static CookableItem forId(int id) {
		return cookingMap.get(id);
	}

    /**
     * Gets burnt.
     *
     * @param id the id
     * @return the burnt
     */
    public static Item getBurnt(int id) {
		return new Item(cookingMap.get(id).burnt);
	}

    /**
     * Intentional burn boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public static boolean intentionalBurn(int id) {
		return (intentionalBurnMap.get(id) != null);
	}

    /**
     * Gets intentional burn.
     *
     * @param id the id
     * @return the intentional burn
     */
    public static Item getIntentionalBurn(int id) {
		return new Item(intentionalBurnMap.get(id).burnt);
	}
}
