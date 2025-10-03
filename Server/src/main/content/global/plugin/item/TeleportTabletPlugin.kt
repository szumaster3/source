package content.global.plugin.item

import content.data.GameAttributes
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.quest.QuestReq
import core.game.node.entity.player.link.quest.QuestRequirements
import core.game.node.item.Item
import core.game.world.map.Location
import shared.consts.Items
import shared.consts.Quests

enum class TeleportTablet(
    val item: Int,
    val location: Location,
    val exp: Double,
) {
    ARDOUGNE_TELEPORT(Items.ARDOUGNE_TP_8011, Location.create(2662, 3307, 0), 61.0),
    AIR_ALTAR_TELEPORT(Items.AIR_ALTAR_TP_13599, Location.create(2978, 3296, 0), 0.0),
    ASTRAL_ALTAR_TELEPORT(Items.ASTRAL_ALTAR_TP_13611, Location.create(2156, 3862, 0), 0.0),
    BLOOD_ALTAR_TELEPORT(Items.BLOOD_ALTAR_TP_13610, Location.create(3559, 9778, 0), 0.0),
    BODY_ALTAR_TELEPORT(Items.BODY_ALTAR_TP_13604, Location.create(3055, 3443, 0), 0.0),
    CAMELOT_TELEPORT(Items.CAMELOT_TP_8010, Location.create(2757, 3477, 0), 55.5),
    CHAOS_ALTAR_TELEPORT(Items.CHAOS_ALTAR_TP_13606, Location.create(3058, 3593, 0), 0.0),
    COSMIC_ALTAR_TELEPORT(Items.COSMIC_ALTAR_TP_13605, Location.create(2411, 4380, 0), 0.0),
    DEATH_ALTAR_TELEPORT(Items.DEATH_ALTAR_TP_13609, Location.create(1863, 4639, 0), 0.0),
    EARTH_ALTAR_TELEPORT(Items.EARTH_ALTAR_TP_13602, Location.create(3304, 3472, 0), 0.0),
    FALADOR_TELEPORT(Items.FALADOR_TP_8009, Location.create(2966, 3380, 0), 47.0),
    FIRE_ALTAR_TELEPORT(Items.FIRE_ALTAR_TP_13603, Location.create(3311, 3252, 0), 0.0),
    LAW_ALTAR_TELEPORT(Items.LAW_ALTAR_TP_13608, Location.create(2857, 3378, 0), 0.0),
    LUMBRIDGE_TELEPORT(Items.LUMBRIDGE_TP_8008, Location.create(3222, 3218, 0), 41.0),
    MIND_ALTAR_TELEPORT(Items.MIND_ALTAR_TP_13600, Location.create(2979, 3510, 0), 0.0),
    NATURE_ALTAR_TELEPORT(Items.NATURE_ALTAR_TP_13607, Location.create(2868, 3013, 0), 0.0),
    VARROCK_TELEPORT(Items.VARROCK_TP_8007, Location.create(3212, 3423, 0), 35.00),
    WATCH_TOWER_TELEPORT(Items.WATCHTOWER_TPORT_8012, Location.create(2548, 3114, 0), 68.00),
    WATER_ALTAR_TELEPORT(Items.WATER_ALTAR_TP_13601, Location.create(3182, 3162, 0), 0.0),
    RUNECRAFTING_GUILD_TELEPORT(Items.RUNECRAFTING_GUILD_TP_13598, Location.create(1696, 5463, 2), 0.0),
    ;

    companion object {
        /**
         * A map that associates item ids.
         */
        val idMap = values().associateBy { it.item }

        /**
         * Gets the [TeleportTablet] for item id.
         */
        fun forId(id: Int): TeleportTablet? = idMap[id]
    }

}

class TeleportTabletOption : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles interaction with teleport tablets.
         */

        val tabIDs = TeleportTablet.values().map { it.item }.toIntArray()
        on(tabIDs, IntType.ITEM, "break") { player, node ->
            val tab = node.id
            val tabEnum = TeleportTablet.forId(tab)
            if (tabEnum != null && inInventory(player, tab)) {
                if (!isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                    sendMessage(player, "You need complete the Rune Mysteries quest in order to use this.")
                    return@on true
                }
                val tabloc = tabEnum.location

                if (inInventory(player, tab)) {
                    if (tab == Items.ARDOUGNE_TP_8011 && !getAttribute(player, GameAttributes.ARDOUGNE_TELEPORT, false)){
                        sendMessage(player, "You haven't learnt how to use this yet.")
                        return@on true
                    }
                    if(tab == Items.WATCHTOWER_TPORT_8012 && !getAttribute(player, GameAttributes.WATCHTOWER_TELEPORT, false)){
                        sendMessage(player, "You haven't learnt how to use this yet.")
                        return@on true
                    }
                    if (tab == Items.ASTRAL_ALTAR_TP_13611 &&
                        !hasRequirement(player, QuestReq(QuestRequirements.LUNAR_DIPLOMACY))
                    ) {
                        return@on true
                    }
                    if (tab == Items.COSMIC_ALTAR_TP_13605 && !isQuestComplete(player, Quests.LOST_CITY)) {
                        return@on true
                    }
                    if (tab == Items.DEATH_ALTAR_TP_13609 &&
                        !hasRequirement(player, QuestReq(QuestRequirements.MEP_2))
                    ) {
                        return@on true
                    }
                    if (tab == Items.BLOOD_ALTAR_TP_13610 &&
                        !hasRequirement(player, QuestReq(QuestRequirements.SEERGAZE))
                    ) {
                        return@on true
                    }
                    if (tab == Items.LAW_ALTAR_TP_13608 && !ItemDefinition.canEnterEntrana(player)) {
                        sendMessage(player, "You can't take weapons and armour into the law rift.")
                        return@on false
                    }
                    if (teleport(player, tabloc, TeleportManager.TeleportType.TELETABS)) {
                        removeItem(player, Item(node.id, 1))
                    }
                }
            }
            return@on true
        }
    }
}
