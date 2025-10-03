package content.global.plugin.item

import com.google.gson.JsonObject
import content.global.skill.magic.spells.modern.AlchemySpell
import core.ServerStore
import core.ServerStore.Companion.getInt
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import shared.consts.Graphics
import shared.consts.Items

/**
 * Handles interactions for explorers ring.
 */
class ExplorersRingPlugin : InteractionListener {

    companion object {
        val RINGS = intArrayOf(Items.EXPLORERS_RING_1_13560, Items.EXPLORERS_RING_2_13561, Items.EXPLORERS_RING_3_13562)
        val CABBAGE_PORT: Location = Location.create(3051, 3291, 0)
    }

    override fun defineListeners() {
        on(RINGS, IntType.ITEM, "operate", "rub") { player, node ->
            if (getRingLevel(node.id) < 3) {
                sendMessage(player, "This item can not be operated.")
                return@on true
            }

            teleport(player)
            return@on true
        }

        on(RINGS, IntType.ITEM, "run-replenish") { player, node ->
            val charges = getStoreFile().getInt(player.username.lowercase() + ":run")
            if (charges >= getRingLevel(node.id)) {
                sendDialogue(player, "Your ring appears to have no more run energy recharges left for today.")
                return@on true
            }
            if (player.settings.runEnergy == 100.0) {
                sendMessage(player, "You are fully rested. You do not need to use the ring's power for the moment.")
                return@on true
            }
            player.settings.updateRunEnergy(-50.0)
            playAudio(player, 5035)

            getStoreFile().addProperty(player.username.lowercase() + ":run", charges + 1)
            sendMessage(player, "You feel refreshed as the ring revitalises you and a charge is used up.")
            visualize(player, 9988, Graphics.RECHARGE_RUN_1733)
            return@on true
        }

        on(RINGS, IntType.ITEM, "low-alchemy") { player, _ ->
            if (!hasLevelStat(player, Skills.MAGIC, 21)) {
                sendMessage(player, "You need a Magic level of 21 in order to do that.")
                return@on true
            }
            val remaining = getStoreFile().getInt(player.username.lowercase() + ":alchs", 30)
            if (remaining <= 0) {
                sendMessage(player, "You have used up all of your charges for the day.")
                return@on true
            }
            sendDialogue(player, "Choose the item that you wish to convert to coins.")
            addDialogueAction(player) { _, _ ->
                sendItemSelect(player, "Choose") { slot, _ ->
                    val item = player.inventory[slot]
                    if (item == null) return@sendItemSelect
                    if (!AlchemySpell().alchemize(player, item, false, explorersRing = true)) return@sendItemSelect
                    getStoreFile().addProperty(player.username.lowercase() + ":alchs", remaining - 1)
                }
            }
            return@on true
        }

        on(RINGS, IntType.ITEM, "cabbage-port") { player, _ ->
            teleport(player)
            return@on true
        }
    }

    /**
     * Teleports the player to the cabbage port location.
     */
    fun teleport(player: Player) {
        teleport(player, CABBAGE_PORT, TeleportType.CABBAGE)
    }

    /**
     * Gets the diary level based on item id.
     */
    private fun getRingLevel(id: Int): Int =
        when (id) {
            Items.EXPLORERS_RING_1_13560 -> 1
            Items.EXPLORERS_RING_2_13561 -> 2
            Items.EXPLORERS_RING_3_13562 -> 3
            else -> -1
        }

    /**
     * Gets the persistent storage for daily ring use charges.
     */
    fun getStoreFile(): JsonObject = ServerStore.getArchive("daily-explorer-ring")
}
