package content.minigame.fishingtrawler.plugin

import content.global.skill.gathering.fishing.Fish
import core.game.node.item.Item
import core.game.node.item.WeightedChanceItem
import core.tools.RandomFunction
import shared.consts.Items

object TrawlerLoot {
    private val junkItems =
        arrayOf(
            Items.BROKEN_ARMOUR_698,
            Items.BROKEN_ARROW_687,
            Items.OLD_BOOT_685,
            Items.BROKEN_GLASS_1469,
            Items.BROKEN_STAFF_689,
            Items.BUTTONS_688,
            Items.DAMAGED_ARMOUR_697,
            Items.RUSTY_SWORD_686,
            Items.EMPTY_POT_1931,
            Items.OYSTER_407,
        )
    private val trawlerFish =
        arrayOf(
            Fish.MANTA_RAY,
            Fish.SEA_TURTLE,
            Fish.SHARK,
            Fish.SWORDFISH,
            Fish.LOBSTER,
            Fish.TUNA,
            Fish.ANCHOVY,
            Fish.SARDINE,
            Fish.SHRIMP,
        )
    private val trawlerFishIds =
        arrayOf(
            Items.RAW_MANTA_RAY_389,
            Items.RAW_SEA_TURTLE_395,
            Items.RAW_SHARK_383,
            Items.RAW_SWORDFISH_371,
            Items.RAW_LOBSTER_377,
            Items.RAW_TUNA_359,
            Items.RAW_ANCHOVIES_321,
            Items.RAW_SARDINE_327,
            Items.RAW_SHRIMPS_317,
        )
    private val trawlerMisc =
        arrayOf(
            Items.LOOP_HALF_OF_A_KEY_987,
            Items.TOOTH_HALF_OF_A_KEY_985,
            Items.CASKET_405,
            Items.PIRATES_HAT_2651,
            Items.LUCKY_CUTLASS_7140,
        )

    private fun rollTrawlerFish(fishLevel: Int): Item {
        while (true) {
            for (f in trawlerFish) {
                if (f.level > fishLevel) {
                    continue
                }
                val lo = 0.6133
                val hi = 0.7852

                val chance = (fishLevel.toDouble() - 15.0) * ((hi - lo) / (99.0 - 15.0)) + lo
                if (RandomFunction.random(0.0, 1.0) < chance) {
                    return Item(f.id)
                }
            }
        }
    }

    @JvmStatic
    fun getLoot(fishLevel: Int, rolls: Int, skipJunk: Boolean): ArrayList<Item> {
        val loot = ArrayList<Item>()
        for (i in 0 until rolls) {
            val item = RandomFunction.rollWeightedChanceTable(listOf(*lootTable))
            if (item.id == 0) {
                loot.add(rollTrawlerFish(fishLevel))
            } else if (!skipJunk || item.id !in junkItems) {
                loot.add(item)
            }
        }
        return loot
    }

    private val lootTable =
        arrayOf(
            WeightedChanceItem(0, 1, 1430),
            WeightedChanceItem(Items.BROKEN_ARROW_687, 1, 70),
            WeightedChanceItem(Items.BROKEN_GLASS_1469, 1, 70),
            WeightedChanceItem(Items.BROKEN_STAFF_689, 1, 70),
            WeightedChanceItem(Items.BUTTONS_688, 1, 80),
            WeightedChanceItem(Items.DAMAGED_ARMOUR_697, 1, 70),
            WeightedChanceItem(Items.OLD_BOOT_685, 1, 60),
            WeightedChanceItem(Items.OYSTER_407, 1, 50),
            WeightedChanceItem(Items.EMPTY_POT_1931, 1, 50),
            WeightedChanceItem(Items.RUSTY_SWORD_686, 1, 50),
        )
}
