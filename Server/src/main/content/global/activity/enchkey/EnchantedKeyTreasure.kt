package content.global.activity.enchkey

import core.api.*
import core.game.node.entity.player.Player
import core.game.world.map.Location
import shared.consts.Items
import kotlin.random.Random

/**
 * Represents the Enchanted key activity rewards.
 */
enum class EnchantedKeyTreasure(val location: Location, val rewards: List<Pair<Int, Int>>, val group: Group, val completionMessage: String? = null) {
    RELLEKKA(EnchKeyUtils.rellekkaTreasure, listOf(Items.STEEL_ARROW_886 to 20, Items.MITHRIL_ORE_448 to 10, Items.LAW_RUNE_563 to 15), Group.MAKING),
    ARDOUGNE(EnchKeyUtils.ardougneTreasure, listOf(Items.PURE_ESSENCE_7937 to 36, Items.IRON_ORE_441 to 15, Items.FIRE_RUNE_554 to 30), Group.MAKING),
    BENCH(EnchKeyUtils.benchTreasure, listOf(Items.PURE_ESSENCE_7937 to 40, Items.IRON_ARROWTIPS_40 to 20, Items.FIRE_RUNE_554 to 20), Group.MAKING),
    GNOME(EnchKeyUtils.gnomeTreasure, listOf(Items.PURE_ESSENCE_7937 to 39, Items.IRON_ARROWTIPS_40 to 20, Items.WATER_RUNE_555 to 30), Group.MAKING),
    ALTAR(EnchKeyUtils.altarTreasure, listOf(Items.MITHRIL_ORE_448 to 10, Items.IRON_ORE_441 to 15, Items.EARTH_RUNE_557 to 45), Group.MAKING),
    FALADOR(EnchKeyUtils.faladorTreasure, listOf(Items.EARTH_RUNE_557 to 15, Items.IRON_ARROW_884 to 20, Items.SARADOMIN_MJOLNIR_6762 to 1), Group.MAKING),
    MUDSKIPPER(EnchKeyUtils.mudskipperTreasure, listOf(Items.IRON_ORE_441 to 15, Items.MITHRIL_ARROW_888 to 20, Items.DEATH_RUNE_560 to 15), Group.MAKING),
    SWAMP(EnchKeyUtils.swampTreasure, listOf(Items.PURE_ESSENCE_7937 to 29, Items.MIND_RUNE_558 to 20, Items.STEEL_ARROW_886 to 20, Items.ZOMBIE_HEAD_6722 to 1), Group.MAKING),
    ALKHARID(EnchKeyUtils.alkharidTreasure, listOf(Items.PURE_ESSENCE_7937 to 40, Items.MITHRIL_ORE_448 to 10, Items.ZAMORAK_MJOLNIR_6764 to 1), Group.MAKING),
    EXAM(EnchKeyUtils.examTreasure, listOf(Items.PURE_ESSENCE_7937 to 40, Items.IRON_ORE_441 to 15, Items.GUTHIX_MJOLNIR_6760 to 1), Group.MAKING),
    GE(EnchKeyUtils.geTreasure, listOf(Items.PURE_ESSENCE_7937 to 39, Items.MITHRIL_ARROW_888 to 10, Items.LAW_RUNE_563 to 15), Group.MAKING, completionMessage = "You have recovered all the buried rewards!"),
    GNOMEBALL_FIELD(EnchKeyUtils.gnomeballfieldTreasure, listOf(Items.COINS_995 to 510, Items.GOLD_CHARM_12158 to 3, Items.LAW_RUNE_563 to 15, Items.MITHRIL_ARROW_888 to 20), Group.MEETING),
    SHANTAY_PASS(EnchKeyUtils.shantaypassTreasure, listOf(Items.COINS_995 to 530, Items.GOLD_CHARM_12158 to 3, Items.PURE_ESSENCE_7937 to 10, Items.UNCUT_SAPPHIRE_1624 to 3), Group.MEETING),
    BRIMHAVEN(EnchKeyUtils.brimhavenTreasure, listOf(Items.COINS_995 to 560, Items.GREEN_CHARM_12159 to 1, Items.COSMIC_RUNE_564 to 5, Items.UNCUT_EMERALD_1622 to 2), Group.MEETING),
    WILDERNESS(EnchKeyUtils.wildernessTreasure, listOf(Items.COINS_995 to 650, Items.GREEN_CHARM_12159 to 1, Items.PURE_ESSENCE_7937 to 10, Items.UNCUT_RUBY_1620 to 1), Group.MEETING),
    TAIBWO_WANNAI(EnchKeyUtils.taibwowannaiTreasure, listOf(Items.COINS_995 to 750, Items.GREEN_CHARM_12159 to 2, Items.COSMIC_RUNE_564 to 10, Items.MITHRIL_ARROW_888 to 30), Group.MEETING),
    FELDIP_HILLS(EnchKeyUtils.feldiphillsTreasure, listOf(Items.COINS_995 to 800, Items.GOLD_CHARM_12158 to 30, Items.CRIMSON_CHARM_12160 to 1, Items.NATURE_RUNE_561 to 15), Group.MEETING),
    AGILITY_PYRAMID(EnchKeyUtils.agilitypyramidTreasure, listOf(Items.COINS_995 to 830, Items.CRIMSON_CHARM_12160 to 1, Items.DEATH_RUNE_560 to 5, Items.UNCUT_RUBY_1620 to 2), Group.MEETING),
    BANDIT_CAMP(EnchKeyUtils.banditcampTreasure, listOf(Items.COINS_995 to 950, Items.CRIMSON_CHARM_12160 to 2, Items.UNCUT_EMERALD_1621 to 3, Items.CHAOS_RUNE_562 to 15), Group.MEETING),
    DAEMONHEIM(EnchKeyUtils.daemonheimTreasure, listOf(Items.COINS_995 to 950, Items.BLUE_CHARM_12163 to 1, Items.PURE_ESSENCE_7937 to 20, Items.GOLD_BAR_2358 to 5), Group.MEETING),
    DEATH_PLATEAU(EnchKeyUtils.deathplateauTreasure, listOf(Items.COINS_995 to 1010, Items.BLUE_CHARM_12163 to 1, Items.PURE_ESSENCE_7937 to 20, Items.BLOOD_RUNE_565 to 10), Group.MEETING),
    SCORPION_PIT(EnchKeyUtils.scorpionPitTreasure, listOf(Items.COINS_995 to 1100, Items.BLUE_CHARM_12163 to 2, Items.GOLD_BAR_2358 to 15, Items.DEATH_RUNE_560 to 10), Group.MEETING, completionMessage = "You have found the final treasure!");

    enum class Group { MAKING, MEETING }

    data class Treasure(
        val rewards: List<Pair<Int, Int>>,
        val completionMessage: String? = null,
    )

    private fun giveRewards(
        player: Player,
        rewards: List<Pair<Int, Int>>,
    ) {
        rewards.forEach { (item, amount) ->
            addItemOrDrop(player, item, amount)
        }
    }

    private fun finishActivity(
        player: Player,
        attribute: String,
    ) {
        removeItem(player, Items.ENCHANTED_KEY_6754)
        removeAttribute(player, attribute)
        sendMessage(player, "Congratulations! You have completed the Enchanted key mini-quest!")
    }

    private fun handleTreasureDig(
        player: Player,
        location: Location,
        treasures: List<EnchantedKeyTreasure>,
        attribute: String
    ) {
        val currentProgress = getAttribute(player, attribute, 0)

        if (currentProgress < treasures.size && treasures[currentProgress].location == location) {
            val treasure = treasures[currentProgress]

            player.incrementAttribute(attribute)
            giveRewards(player, treasure.rewards)
            sendMessage(player, "You found a treasure!")

            if (treasure.completionMessage != null && currentProgress == treasures.lastIndex) {
                sendMessage(player, treasure.completionMessage)
                finishActivity(player, attribute)
            }
        }
    }

    companion object {
        fun getTreasures(group: Group): List<EnchantedKeyTreasure> =
            values().filter { it.group == group }

        fun getShuffledMaking(player: Player): List<EnchantedKeyTreasure> {
            val (initial, rest) = getTreasures(Group.MAKING).partition { it == RELLEKKA }
            return initial + rest.shuffled(Random(player.hashCode()))
        }

        fun asMap(treasures: List<EnchantedKeyTreasure>): Map<Location, Treasure> =
            treasures.associate { it.location to Treasure(it.rewards, it.completionMessage) }
    }
}
