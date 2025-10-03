package content.global.skill.smithing

import core.game.node.item.Item
import shared.consts.Items

enum class Bar(
    val level: Int,
    val experience: Double,
    val product: Item,
    vararg ores: Item
) {
    BRONZE(1, 6.2, Item(Items.BRONZE_BAR_2349, 1), Item(Items.COPPER_ORE_436, 1), Item(Items.TIN_ORE_438, 1)),
    BLURITE(8, 8.0, Item(Items.BLURITE_BAR_9467, 1), Item(Items.BLURITE_ORE_668, 1)),
    IRON(15, 12.5, Item(Items.IRON_BAR_2351, 1), Item(Items.IRON_ORE_440, 1)),
    SILVER(20, 13.7, Item(Items.SILVER_BAR_2355, 1), Item(Items.SILVER_ORE_442, 1)),
    STEEL(30, 17.5, Item(Items.STEEL_BAR_2353, 1), Item(Items.COAL_453, 2), Item(Items.IRON_ORE_440, 1)),
    GOLD(40, 22.5, Item(Items.GOLD_BAR_2357, 1), Item(Items.GOLD_ORE_444, 1)),
    PERFECT_GOLD(40, 22.5, Item(Items.PERFECT_GOLD_BAR_2365, 1), Item(Items.PERFECT_GOLD_ORE_446, 1)),
    MITHRIL(50, 30.0, Item(Items.MITHRIL_BAR_2359, 1), Item(Items.MITHRIL_ORE_447, 1), Item(Items.COAL_453, 4)),
    ADAMANT(70, 37.5, Item(Items.ADAMANTITE_BAR_2361, 1), Item(Items.ADAMANTITE_ORE_449, 1), Item(Items.COAL_453, 6)),
    RUNITE(85, 50.0, Item(Items.RUNITE_BAR_2363, 1), Item(Items.RUNITE_ORE_451, 1), Item(Items.COAL_453, 8));

    val ores: Array<Item> = Array(ores.size) { ores[it] }

    companion object {
        private val idToBar = HashMap<Int, Bar>()
        private val oreIdToBar = HashMap<Int, Bar>()

        init {
            for (bar in values()) {
                idToBar[bar.product.id] = bar
                for (ore in bar.ores) {
                    oreIdToBar[ore.id] = bar
                }
            }
        }

        @JvmStatic
        fun forId(id: Int): Bar? = idToBar[id]

        fun forOre(id: Int): Bar? = oreIdToBar[id]
    }
}
