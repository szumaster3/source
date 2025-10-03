package content.global.skill.construction.servants

import core.game.node.item.Item
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents servants NPC that player can hire for various services.
 */
enum class ServantType(
    val id: Int, // Wrappers
    val cost: Int,
    val capacity: Int,
    val level: Int,
    val timer: Int,
    vararg val food: Item?,
) {
    NONE(-1, -1, -1, -1, -1),
    RICK(NPCs.RICK_4236, 500, 6, 20, 60),
    MAID(NPCs.MAID_4238, 1000, 10, 25, 30, Item(Items.STEW_2003)),
    COOK(NPCs.COOK_4240, 3000, 16, 30, 17, Item(Items.PINEAPPLE_PIZZA_2301), Item(Items.AMULET_OF_GLORY4_1712)),
    BUTLER(NPCs.BUTLER_4242, 5000, 20, 40, 12, Item(Items.CHOCOLATE_CAKE_1897), Item(Items.CUP_OF_TEA_712)),
    DEMON_BUTLER(NPCs.DEMON_BUTLER_4244, 10000, 26, 50, 7, Item(Items.CURRY_2011)), ;

    companion object {
        @JvmStatic
        fun forId(id: Int): ServantType? = values().find { it.id == id }
    }
}