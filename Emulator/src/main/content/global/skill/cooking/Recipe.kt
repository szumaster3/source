package content.global.skill.cooking

import core.api.inInventory
import core.api.sendMessage
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
    REDBERRY_PIE(
        base = Item(Items.PIE_SHELL_2315),
        product = Item(Items.UNCOOKED_BERRY_PIE_2321),
        ingredients = arrayOf(Item(Items.REDBERRIES_1951)),
        parts = emptyArray(),
        message = { "You fill the pie with redberries." },
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
    ),

    PLAIN_PIZZA(
        base = Item(Items.PIZZA_BASE_2283),
        product = Item(Items.UNCOOKED_PIZZA_2287),
        ingredients = arrayOf(Item(Items.TOMATO_1982), Item(Items.CHEESE_1985)),
        parts =
            arrayOf(
                Item(Items.PIZZA_BASE_2283),
                Item(Items.INCOMPLETE_PIZZA_2285),
                Item(Items.UNCOOKED_PIZZA_2287),
            ),
        message = { event -> "You add the ${event.baseItem.name.lowercase()} to the pizza." },
        singular = false,
        level = 35,
    ),
    BUTTER_POTATO(
        base = Item(Items.BAKED_POTATO_6701),
        product = Item(Items.POTATO_WITH_BUTTER_6703),
        ingredients = arrayOf(Item(Items.PAT_OF_BUTTER_6697)),
        parts = emptyArray(),
        message = { "You add the butter to the potato." },
        level = 39,
        experience = 40.5,
    ),
    TUNA_POTATO(
        base = Item(Items.POTATO_WITH_BUTTER_6703),
        product = Item(Items.TUNA_POTATO_7060),
        ingredients = arrayOf(Item(Items.TUNA_AND_CORN_7068)),
        parts = emptyArray(),
        message = { "You add the topping to the potato." },
        level = 68,
        experience = 10.0,
    ),
    CHILLI_POTATO(
        base = Item(Items.POTATO_WITH_BUTTER_6703),
        product = Item(Items.CHILLI_POTATO_7054),
        ingredients = arrayOf(Item(Items.CHILLI_CON_CARNE_7062)),
        parts = emptyArray(),
        message = { "You add the topping to the potato." },
        level = 41,
        experience = 10.0,
    ),
    CHEESE_POTATO(
        base = Item(Items.POTATO_WITH_BUTTER_6703),
        product = Item(Items.POTATO_WITH_CHEESE_6705),
        ingredients = arrayOf(Item(Items.CHEESE_1985)),
        parts = emptyArray(),
        message = { "You add the cheese to the potato." },
        level = 47,
        experience = 10.0,
    ),
    EGG_POTATO(
        base = Item(Items.POTATO_WITH_BUTTER_6703),
        product = Item(Items.EGG_POTATO_7056),
        ingredients = arrayOf(Item(Items.EGG_AND_TOMATO_7064)),
        parts = emptyArray(),
        message = { "You add the topping to the potato." },
        level = 51,
        experience = 10.0,
    ),
    MUSHROOM_POTATO(
        base = Item(Items.POTATO_WITH_BUTTER_6703),
        product = Item(Items.MUSHROOM_POTATO_7058),
        ingredients = arrayOf(Item(Items.MUSHROOM_AND_ONION_7066)),
        parts = emptyArray(),
        message = { "You add the mushroom to the potato." },
        level = 64,
        experience = 10.0,
    ),

    UNCOOKED_EGG(
        base = Item(Items.BOWL_1923),
        product = Item(Items.UNCOOKED_EGG_7076),
        ingredients = arrayOf(Item(Items.EGG_1944)),
        parts = emptyArray(),
        message = { "You prepare an uncooked egg." },
        level = 1,
        experience = 1.0,
    ),
    TUNA_AND_CORN(
        base = Item(Items.CHOPPED_TUNA_7086),
        product = Item(Items.TUNA_AND_CORN_7068),
        ingredients = arrayOf(Item(Items.COOKED_SWEETCORN_5988)),
        parts = emptyArray(),
        message = { "You mix the ingredients to make the topping." },
        level = 67,
        experience = 204.0,
    ),
    SPICY_SAUCE(
        base = Item(Items.BOWL_1923),
        product = Item(Items.SPICY_SAUCE_7072),
        ingredients = arrayOf(Item(Items.GARLIC_1550), Item(Items.GNOME_SPICE_2169)),
        parts = arrayOf(Item(Items.BOWL_1923), Item(Items.CHOPPED_GARLIC_7074), Item(Items.SPICY_SAUCE_7072)),
        message = { "You prepare the spicy sauce." },
        level = 9,
        experience = 25.0,
    ),
    CHILLI_CON_CARNE(
        base = Item(Items.SPICY_SAUCE_7072),
        product = Item(Items.CHILLI_CON_CARNE_7062),
        ingredients = arrayOf(Item(Items.COOKED_MEAT_2142)),
        parts = emptyArray(),
        message = { "You put the cut up meat into the bowl." },
        level = 9,
        experience = 25.0,
        event = { player, _ ->
            if (!inInventory(player, Items.KNIFE_946)) {
                sendMessage(player, "You need a knife to slice up the onion.")
            }
        },
    ),
    MUSHROOM_AND_ONION(
        base = Item(Items.FRIED_MUSHROOMS_7082),
        product = Item(Items.MUSHROOM_AND_ONION_7066),
        ingredients = arrayOf(Item(Items.FRIED_ONIONS_7084)),
        parts = emptyArray(),
        message = { "You mix the fried onions and mushrooms." },
        level = 57,
        experience = 120.0,
    ),
    EGG_AND_TOMATO(
        base = Item(Items.SCRAMBLED_EGG_7078),
        product = Item(Items.EGG_AND_TOMATO_7064),
        ingredients = arrayOf(Item(Items.TOMATO_1982)),
        parts = emptyArray(),
        message = { "You mix the scrambled egg with the tomato." },
        level = 23,
        experience = 50.0,
    ),
    OOMLIE_WRAP(
        base = Item(Items.RAW_OOMLIE_2337),
        product = Item(Items.WRAPPED_OOMLIE_2341),
        ingredients = arrayOf(Item(Items.PALM_LEAF_2339)),
        parts = emptyArray(),
        message = { "You wrap the raw oomlie in the palm leaf." },
        singular = true,
        level = 50,
        experience = 30.0,
    );
}
