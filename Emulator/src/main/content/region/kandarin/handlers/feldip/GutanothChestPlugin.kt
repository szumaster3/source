package content.region.kandarin.handlers.feldip

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import java.util.concurrent.TimeUnit

@Initializable
class GutanothChestPlugin : OptionHandler() {

    companion object {
        private const val DELAY_ATTRIBUTE = "/save:gutanoth-chest-delay"
        private const val CLOSED_CHEST_ID = org.rs.consts.Scenery.CHEST_2827
        private const val OPENED_CHEST_ID = 2828
    }

    override fun newInstance(arg: Any?): Plugin<Any?> {
        SceneryDefinition.forId(CLOSED_CHEST_ID).handlers["option:open"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (node.id != CLOSED_CHEST_ID) return false
        if (option.lowercase() != "open") return false

        val lastOpen = getAttribute(player, DELAY_ATTRIBUTE, 0L)
        if (System.currentTimeMillis() < lastOpen) {
            sendMessage(player, "You have already searched this chest.")
            return true
        }

        lock(player, 3)
        animate(player, Animations.OPEN_CHEST_536)

        SceneryBuilder.replace(node.asScenery(), Scenery(OPENED_CHEST_ID, node.location), 5)

        queueScript(player, 3, QueueStrength.SOFT) {
            giveReward(player)
            return@queueScript stopExecuting(player)
        }

        setAttribute(player, DELAY_ATTRIBUTE, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15))
        return true
    }

    private fun giveReward(player: Player) {
        val reward = ChestReward.values().random()
        player.sendChat(reward.message)

        when (reward.type) {
            RewardType.ITEM -> {
                if (!player.inventory.add(Item(reward.id))) {
                    GroundItemManager.create(Item(reward.id), player.location)
                }
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
        SPIDER(NPCs.SPIDER_1004, RewardType.NPC, "Awh, a cute lil spidey!")
    }

    private enum class RewardType { ITEM, NPC }
}
