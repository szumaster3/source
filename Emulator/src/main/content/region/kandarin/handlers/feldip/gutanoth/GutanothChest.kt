package content.region.kandarin.handlers.feldip.gutanoth

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object GutanothChest : InteractionListener {

    override fun defineListeners() {
        on(org.rs.consts.Scenery.CHEST_2827, IntType.SCENERY, "open") { player, node ->
            val lastOpen = getAttribute(player, "gutanoth-chest-delay", 0L)
            if (System.currentTimeMillis() < lastOpen) {
                sendMessage(player, "You have already searched this chest.")
                return@on true
            }

            lock(player, 3)
            animate(player, Animations.OPEN_CHEST_536)

            SceneryBuilder.replace(node.asScenery(), Scenery(2828, node.location), 5)

            queueScript(player, 3, QueueStrength.SOFT){
                giveReward(player)
                return@queueScript stopExecuting(player)
            }

            setAttribute(player, "/save:gutanoth-chest-delay", System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15))
            return@on true
        }
    }

    private fun giveReward(player: Player) {
        val reward = ChestReward.values()[Random.nextInt(ChestReward.values().size)]
        player.sendChat(reward.message)

        when (reward.type) {
            RewardType.ITEM -> if (!player.inventory.add(Item(reward.id))) {
                GroundItemManager.create(
                    Item(reward.id),
                    player.location,
                )
            }
            RewardType.NPC -> {
                val npc = NPC(reward.id)
                npc.location = player.location
                npc.isAggressive = true
                npc.isRespawn = false
                npc.properties.combatPulse.attack(player)
                npc.init()
            }
        }
    }

    private enum class ChestReward(val id: Int, val type: RewardType, val message: String) {
        BONES(Items.BONES_2530, RewardType.ITEM, "Oh! Some bones. Delightful."),
        EMERALD(Items.EMERALD_1605, RewardType.ITEM, "Ooh! A lovely emerald!"),
        ROTTEN_APPLE(Items.ROTTEN_APPLE_1984, RewardType.ITEM, "Oh, joy, spoiled fruit! My favorite!"),
        CHAOS_DWARF(NPCs.CHAOS_DWARF_119, RewardType.NPC, "You've gotta be kidding me, a dwarf?!"),
        RAT(NPCs.RAT_47, RewardType.NPC, "Eek!"),
        SCORPION(NPCs.SCORPION_1477, RewardType.NPC, "Zoinks!"),
        SPIDER(NPCs.SPIDER_1004, RewardType.NPC, "Awh, a cute lil spidey!");
    }

    private enum class RewardType { ITEM, NPC }
}