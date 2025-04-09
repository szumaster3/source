package content.global.skill.cooking

import core.game.interaction.NodeUsageEvent
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

enum class Recipe(
    val base: Item,
    val product: Item,
    val ingredients: Array<Item>,
    val parts: Array<Item>,
    val message: (NodeUsageEvent) -> String?,
    val singular: Boolean = true,
    val level: Int = 0,
    val experience: Double = 0.0,
    val event: ((Player, NodeUsageEvent) -> Unit)? = null,
) {
    ADMIRAL_PIE(
        base = Item(Items.PIE_SHELL_2315),
        product = Item(Items.RAW_ADMIRAL_PIE_7196),
        ingredients = arrayOf(Item(Items.SALMON_329), Item(Items.TUNA_361), Item(Items.POTATO_1942)),
        parts =
            arrayOf(
                Item(Items.PIE_SHELL_2315),
                Item(Items.PART_ADMIRAL_PIE_7192),
                Item(Items.PART_ADMIRAL_PIE_7194),
                Item(Items.RAW_ADMIRAL_PIE_7196),
            ),
        message = { "You prepare an admiral pie." },
        singular = false,
    ),
    APPLE_PIE(
        base = Item(Items.PIE_SHELL_2315),
        product = Item(Items.UNCOOKED_APPLE_PIE_2317),
        ingredients = arrayOf(Item(Items.COOKING_APPLE_1955)),
        parts = emptyArray(),
        message = { "You fill the pie with apple slices." },
    ),
    FISH_PIE(
        base = Item(Items.PIE_SHELL_2315),
        product = Item(Items.RAW_FISH_PIE_7186),
        ingredients = arrayOf(Item(Items.TROUT_333), Item(Items.COD_339), Item(Items.POTATO_1942)),
        parts =
            arrayOf(
                Item(Items.PIE_SHELL_2315),
                Item(Items.PART_FISH_PIE_7182),
                Item(Items.PART_FISH_PIE_7184),
                Item(Items.RAW_FISH_PIE_7186),
            ),
        message = { "You prepare a fish pie." },
        singular = false,
    ),
    GARDEN_PIE(
        base = Item(Items.PIE_SHELL_2315),
        product = Item(Items.RAW_GARDEN_PIE_7176),
        ingredients = arrayOf(Item(Items.TOMATO_1982), Item(Items.ONION_1957), Item(Items.CABBAGE_1965)),
        parts =
            arrayOf(
                Item(Items.PIE_SHELL_2315),
                Item(Items.PART_GARDEN_PIE_7172),
                Item(Items.PART_GARDEN_PIE_7174),
                Item(Items.RAW_GARDEN_PIE_7176),
            ),
        message = { "You prepare a garden pie." },
        singular = false,
    ),
    MEAT_PIE(
        base = Item(Items.PIE_SHELL_2315),
        product = Item(Items.UNCOOKED_MEAT_PIE_2319),
        ingredients =
            arrayOf(
                Item(Items.COOKED_MEAT_2142),
                Item(Items.COOKED_CHICKEN_2140),
                Item(Items.COOKED_RABBIT_3228),
            ),
        parts = emptyArray(),
        message = { "You fill the pie with meat." },
    ),
    MUD_PIE(
        base = Item(Items.PIE_SHELL_2315),
        product = Item(Items.RAW_MUD_PIE_7168),
        ingredients = arrayOf(Item(Items.COMPOST_6032), Item(Items.BUCKET_OF_WATER_1929), Item(Items.CLAY_434)),
        parts =
            arrayOf(
                Item(Items.PIE_SHELL_2315),
                Item(Items.PART_MUD_PIE_7164),
                Item(Items.PART_MUD_PIE_7166),
                Item(Items.RAW_MUD_PIE_7168),
            ),
        message = { "You prepare a mud pie." },
        singular = false,
    ),
    SUMMER_PIE(
        base = Item(Items.PIE_SHELL_2315),
        product = Item(Items.RAW_SUMMER_PIE_7216),
        ingredients = arrayOf(Item(Items.STRAWBERRY_5504), Item(Items.WATERMELON_5982), Item(Items.COOKING_APPLE_1955)),
        parts =
            arrayOf(
                Item(Items.PIE_SHELL_2315),
                Item(Items.PART_SUMMER_PIE_7212),
                Item(Items.PART_SUMMER_PIE_7214),
                Item(Items.RAW_SUMMER_PIE_7216),
            ),
        message = { "You prepare a summer pie." },
        singular = false,
    ),
    WILD_PIE(
        base = Item(Items.PIE_SHELL_2315),
        product = Item(Items.RAW_WILD_PIE_7206),
        ingredients = arrayOf(Item(Items.RAW_BEAR_MEAT_2136), Item(Items.RAW_CHOMPY_2876), Item(Items.RAW_RABBIT_3226)),
        parts =
            arrayOf(
                Item(Items.PIE_SHELL_2315),
                Item(Items.PART_WILD_PIE_7202),
                Item(Items.PART_WILD_PIE_7204),
                Item(Items.RAW_WILD_PIE_7206),
            ),
        message = { "You prepare a wild pie." },
        singular = false,
    );
}
