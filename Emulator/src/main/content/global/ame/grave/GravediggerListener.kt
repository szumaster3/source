package content.global.ame.grave

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.api.ui.setMinimapState
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Scenery

class GravediggerListener : InteractionListener {
    data class CoffinSet(val name: String, val coffinId: Int, val gravestoneId: Int, val graveId: Int, val emptyGraveId: Int, val content: Array<IntArray>)
    companion object {
        const val COFFIN_INTERFACE = Components.GRAVEDIGGER_COFFIN_141
        const val GRAVESTONE_INTERFACE = Components.GRAVEDIGGER_GRAVE_143
        const val MAUSOLEUM = Scenery.MAUSOLEUM_12731

        val ANIMATION = Animation(Animations.MULTI_BEND_OVER_827)

        val GRAVE = intArrayOf(Scenery.GRAVE_12721, Scenery.GRAVE_12722, Scenery.GRAVE_12723, Scenery.GRAVE_12724, Scenery.GRAVE_12725)
        val EMPTY_GRAVE = intArrayOf(Scenery.GRAVE_12726, Scenery.GRAVE_12727, Scenery.GRAVE_12728, Scenery.GRAVE_12729, Scenery.GRAVE_12730)
        val GRAVESTONE = intArrayOf(Scenery.GRAVESTONE_12716, Scenery.GRAVESTONE_12717, Scenery.GRAVESTONE_12718, Scenery.GRAVESTONE_12719, Scenery.GRAVESTONE_12720)

        /*
         * Cook's coffin content:
         *  7604,7601,7598
         *  7600,7598,7611
         *  7598,7598,7598
         */

        val COOKS_COFFIN_CONTENT = arrayOf(intArrayOf(Items.ITEM_7604, Items.ITEM_7601, Items.ITEM_7598), intArrayOf(Items.ITEM_7600, Items.ITEM_7598, Items.ITEM_7611), intArrayOf(Items.ITEM_7598, Items.ITEM_7598, Items.ITEM_7598))

        /*
         * Farmer's coffin content:
         *  7598,7598,7610
         *  7611,7609,7598
         *  7602,7598,7598
         */

        val FARMER_COFFIN_CONTENT = arrayOf(intArrayOf(Items.ITEM_7598, Items.ITEM_7598, Items.ITEM_7610), intArrayOf(Items.ITEM_7613, Items.ITEM_7609, Items.ITEM_7598), intArrayOf(Items.ITEM_7602, Items.ITEM_7598, Items.ITEM_7598))

        /*
         * Potter's coffin content:
         *  7598,7599,7608
         *  7613,7598,7598
         *  7598,7611,7598
         */

        val POTTER_COFFIN_CONTENT = arrayOf(
            intArrayOf(Items.ITEM_7598, Items.ITEM_7599, Items.ITEM_7608),
            intArrayOf(Items.ITEM_7613, Items.ITEM_7598, Items.ITEM_7598),
            intArrayOf(Items.ITEM_7598, Items.ITEM_7611, Items.ITEM_7598)
        )

        /*
         * Lumberjack's coffin content:
         *  7611,7598,7598
         *  7598,7598,7603
         *  7598,7605,7612
         */

        val LUMBERJACK_COFFIN_CONTENT = arrayOf(intArrayOf(Items.ITEM_7611, Items.ITEM_7598, Items.ITEM_7598), intArrayOf(Items.ITEM_7598, Items.ITEM_7598, Items.ITEM_7603), intArrayOf(Items.ITEM_7598, Items.ITEM_7605, Items.ITEM_7612))

        /*
         * Miner's coffin content:
         *  7598,7598,7598
         *  7598,7597,7598
         *  7607,7598,7611
         */

        val MINER_COFFIN_CONTENT = arrayOf(intArrayOf(Items.ITEM_7598, Items.ITEM_7598, Items.ITEM_7606), intArrayOf(Items.ITEM_7598, Items.ITEM_7597, Items.ITEM_7598), intArrayOf(Items.ITEM_7607, Items.ITEM_7598, Items.ITEM_7611))
        val WOODCUTTING_TOOL = intArrayOf(Items.INFERNO_ADZE_13661, Items.DRAGON_AXE_6739, Items.RUNE_AXE_1359, Items.ADAMANT_AXE_1357, Items.MITHRIL_AXE_1355, Items.BLACK_AXE_1361, Items.STEEL_AXE_1353, Items.IRON_AXE_1349, Items.BRONZE_AXE_1351)
        val COFFIN = intArrayOf(Items.COFFIN_7587, Items.COFFIN_7588, Items.COFFIN_7589, Items.COFFIN_7590, Items.COFFIN_7591)

        fun reset(player: Player) {
            COFFIN_SETS.forEach { set ->
                val key = set.name
                removeAttributes(player, "coffin_id:$key", "gravestone_id:$key", "grave_id:$key", "empty_grave_id:$key", "coffin_content:$key")
            }
        }

        fun cleanup(player: Player) {
            player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
            setMinimapState(player, 0)
            clearLogoutListener(player, RandomEvent.logout())
            removeAttributes(player, RandomEvent.save(), GameAttributes.GRAVEDIGGER_SCORE)
            reset(player)
        }

        fun init(player: Player) {
            /*
             * Not needed, probably: val shuffledCoffin = COFFIN.toList().shuffled().iterator()
             */
            val shuffledGraves = GRAVE.toList().shuffled().iterator()
            val shuffledEmptyGraves = EMPTY_GRAVE.toList().shuffled().iterator()
            val shuffledGravestones = GRAVESTONE.toList().shuffled().iterator()

            COFFIN_SETS.forEach { set ->
                val key = set.name

                player.setAttribute("coffin_set:$key", set.name)
                player.setAttribute("coffin_id:$key", set.coffinId)

                player.setAttribute("gravestone_id:$key", shuffledGravestones.next())
                player.setAttribute("grave_id:$key", shuffledGraves.next())
                player.setAttribute("empty_grave_id:$key", shuffledEmptyGraves.next())
            }
        }

        val COFFIN_SETS = listOf(
            CoffinSet("Lumberjack", COFFIN[0], GRAVESTONE[0], GRAVE[0], EMPTY_GRAVE[0], LUMBERJACK_COFFIN_CONTENT),
            CoffinSet("Cooks",      COFFIN[1], GRAVESTONE[1], GRAVE[1], EMPTY_GRAVE[1], COOKS_COFFIN_CONTENT),
            CoffinSet("Miner",      COFFIN[2], GRAVESTONE[2], GRAVE[2], EMPTY_GRAVE[2], MINER_COFFIN_CONTENT),
            CoffinSet("Farmer",     COFFIN[3], GRAVESTONE[3], GRAVE[3], EMPTY_GRAVE[3], FARMER_COFFIN_CONTENT),
            CoffinSet("Potter",     COFFIN[4], GRAVESTONE[4], GRAVE[4], EMPTY_GRAVE[4], POTTER_COFFIN_CONTENT)
        )
    }

    override fun defineListeners() {
        on(COFFIN, IntType.ITEM, "check") { player, _ ->
            val setName = player.getAttribute<String>("coffin_set") ?: return@on true

            val set = COFFIN_SETS.find { it.name == setName } ?: return@on true
            val coffinContent = set.content.flatMap { it.toList() }
            val components = (3..11).toList().toIntArray()
            openInterface(player, COFFIN_INTERFACE).also {
                coffinContent.forEachIndexed { index, itemId ->
                    if (index < components.size) {
                        sendItemOnInterface(player, COFFIN_INTERFACE, components[index], itemId, 1)
                    }
                }
            }
            return@on true
        }

        on(GRAVESTONE, IntType.SCENERY, "read") { player, node ->
            val set = COFFIN_SETS.find { player.getAttribute<Int>("gravestone_id:${it.name}") == node.id }
                ?: return@on true
            val gravestoneItems = mapOf(
                "Lumberjack" to Items.ITEM_7614,
                "Cooks"      to Items.ITEM_7615,
                "Miner"      to Items.ITEM_7616,
                "Farmer"     to Items.ITEM_7617,
                "Potter"     to Items.ITEM_7618
            )
            val gravestoneItemId = gravestoneItems[set.name] ?: return@on true
            openInterface(player, GRAVESTONE_INTERFACE)
            sendItemOnInterface(player, GRAVESTONE_INTERFACE, 2, gravestoneItemId, 1)
            sendMessage(player, "You look at the gravestone.")
            return@on true
        }

        on(GRAVE, IntType.SCENERY, "take-coffin") { player, node ->
            val set = COFFIN_SETS.find { player.getAttribute<Int>("grave_id:${it.name}") == node.id }
                ?: return@on true

            val coffinID = player.getAttribute<Int>("coffin_id:${set.name}") ?: return@on true
            val emptyGraveID = player.getAttribute<Int>("empty_grave_id:${set.name}") ?: return@on true

            if (!hasSpaceFor(player, Item(coffinID, 1))) {
                player.sendMessage("You need space in your inventory to take the coffin.")
                return@on true
            }

            //if (player.rights == Rights.ADMINISTRATOR) {
            //    player.inventory.add(Item(coffinID, 1))
            //    replaceScenery(node.asScenery(), emptyGraveID, -1)
            //} else {
                lock(player, 3)
                runTask(player, 3) {
                    player.animate(ANIMATION)
                    player.inventory.add(Item(coffinID, 1))
                    replaceScenery(node.asScenery(), emptyGraveID, -1)
                }
            //}

            sendMessage(player, "You take the coffin from the grave.")
            return@on true
        }

        onUseWith(IntType.SCENERY, COFFIN, *EMPTY_GRAVE) { player, _, with ->
            val set = COFFIN_SETS.find { player.getAttribute<Int>("empty_grave_id:${it.name}") == with.id } ?: return@onUseWith true
            val graveID = player.getAttribute<Int>("grave_id:${set.name}") ?: return@onUseWith true
            val coffinID = player.getAttribute<Int>("coffin_id:${set.name}") ?: return@onUseWith true
            if (player.inventory.remove(Item(coffinID))) {
                replaceScenery(with.asScenery(), graveID, -1)
                sendMessage(player, "You put the coffin into the grave.")
            } else {
                sendMessage(player, "Burying that here would be very silly.")
            }

            val currentPoints = player.getAttribute<Int>(GameAttributes.GRAVEDIGGER_SCORE) ?: 0
            player.setAttribute(GameAttributes.GRAVEDIGGER_SCORE, currentPoints + 1)
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, WOODCUTTING_TOOL, Scenery.DEAD_TREE_12732) { player, _, _ ->
            if (inZone(player, "graveyard"))
                sendMessages(player, "You don't need any wood.", "What are you planning on doing, making them a fresh coffin?")
            return@onUseWith true
        }

        on(MAUSOLEUM, IntType.SCENERY, "deposit") { player, _ ->
            player.bank.openDepositBox()
            player.bank::refreshDepositBoxInterface
            return@on true
        }
    }

}