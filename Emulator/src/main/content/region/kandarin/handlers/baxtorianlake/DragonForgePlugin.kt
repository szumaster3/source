package content.region.kandarin.handlers.baxtorianlake

import core.api.*
import core.api.item.allInInventory
import core.api.quest.hasRequirement
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.*
import core.game.node.Node
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.*

@Initializable
class DragonForgePlugin : Plugin<Any> {

    override fun newInstance(arg: Any?): Plugin<Any> {
        definePlugin(DragonAnvilUseWithHandler())
        definePlugin(MithrilDoorOpenHandler())
        definePlugin(MithrilDoorUseKeyHandler())
        definePlugin(DragonKeyCreationHandler())
        return this
    }

    /*
     * Handles using ruined pieces on the Dragon Anvil to repair dragon armor.
     */
    class DragonAnvilUseWithHandler : UseWithHandler(*RUINED_PIECES) {

        override fun newInstance(arg: Any?): Plugin<Any> {
            addHandler(DRAGON_ANVIL, OBJECT_TYPE, this)
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            val usedItem = event.usedItem ?: return false

            if (!hasRequirement(player, Quests.WHILE_GUTHIX_SLEEPS) || getStatLevel(
                    player,
                    Skills.SMITHING
                ) < 92 || !player.inventory.containsItem(FUSION_HAMMER) || (anyInInventory(
                    player,
                    *RUINED_PIECES
                ) && !allInInventory(player, *RUINED_PIECES))
            ) {
                when {
                    !hasRequirement(player, Quests.WHILE_GUTHIX_SLEEPS) -> return false
                    getStatLevel(player, Skills.SMITHING) < 92 -> {
                        sendMessage(player, "You need at least 92 smithing level to do this.")
                        return false
                    }

                    !player.inventory.containsItem(FUSION_HAMMER) -> {
                        sendDialogue(player, "You need a fusion hammer to work the metal with.")
                        return false
                    }

                    anyInInventory(player, *RUINED_PIECES) && !allInInventory(player, *RUINED_PIECES) -> {
                        sendDialogue(player, "You do not have the required items.")
                        return false
                    }
                }
            }

            lock(player, 3)
            lockInteractions(player, 3)
            queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        sendMessage(player, "You set to work repairing the ruined armour.")
                        animate(player, SMITHING_ANIMATION)
                        delayScript(player, SMITHING_ANIMATION.duration)
                    }

                    1 -> {
                        if (removeAll(player, RUINED_PIECES)) {
                            sendMessage(player, "You finish your efforts...")
                            removeItem(player, FUSION_HAMMER)
                            player.inventory.add(DRAGON_PLATEBODY)
                            rewardXP(player, Skills.SMITHING, 2000.0)
                        }
                        unlock(player)
                        stopExecuting(player)
                    }

                    else -> stopExecuting(player)
                }
            }
            return true
        }
    }

    /*
     * Handles opening of mithril doors.
     */
    class MithrilDoorOpenHandler : OptionHandler() {

        override fun newInstance(arg: Any?): Plugin<Any> {
            MITHRIL_DOOR.forEach { doorId ->
                SceneryDefinition.forId(doorId).handlers["option:open"] = this
            }
            return this
        }

        override fun handle(player: Player, node: Node, option: String): Boolean {
            if (!inInventory(player, Items.DRAGONKIN_KEY_14471) && !hasRequirement(player, Quests.WHILE_GUTHIX_SLEEPS, false)) {
                sendDialogue(player, "The door is solid and resists all attempts to open it. There's no way past it at all, so best to ignore it.")
            } else {
                sendMessage(player, "The door opens easily.")
                setAttribute(player, "dragon_head_room", true)
                val destination = when (node.id) {
                    25341 -> location(1823, 5273, 0)
                    else -> location(1759, 5342, 1)
                }
                teleport(player, destination)
            }
            return true
        }
    }

    /*
     * Handles using the dragonkin key on mithril doors.
     */
    class MithrilDoorUseKeyHandler : UseWithHandler(Items.DRAGONKIN_KEY_14471) {
        override fun newInstance(arg: Any?): Plugin<Any> {
            MITHRIL_DOOR.forEach { doorId ->
                addHandler(doorId, OBJECT_TYPE, this)
            }
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            val node = event.usedWith ?: return false

            if (!inInventory(player, Items.DRAGONKIN_KEY_14471) && !hasRequirement(
                    player,
                    Quests.WHILE_GUTHIX_SLEEPS,
                    false
                )
            ) {
                sendDialogue(
                    player,
                    "The door is solid and resists all attempts to open it. There's no way past it at all, so best to ignore it."
                )
            } else {
                sendMessage(player, "The door opens easily.")
                val destination = when (node.id) {
                    25341 -> location(1823, 5273, 0)
                    else -> location(1759, 5342, 1)
                }
                teleport(player, destination)
            }
            return true
        }
    }

    /*
     * Handles using strange keys on mithril dragons to create the dragonkin key.
     */
    class DragonKeyCreationHandler : UseWithHandler(*STRANGE_KEYS) {
        override fun newInstance(arg: Any?): Plugin<Any> {
            MITHRIL_DRAGON_NPC.forEach { npcId ->
                addHandler(npcId, NPC_TYPE, this)
            }
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            val usedItem = event.usedItem ?: return false
            val npc = event.usedWith as? NPC ?: return false

            if (!hasRequirement(player, Quests.WHILE_GUTHIX_SLEEPS, false)) {
                sendMessage(player, "You cannot currently use any items on that dragon.")
                return true
            }

            if (!anyInEquipment(player, *REQUIRED_SHIELD)) {
                sendMessage(player, "You cannot currently use any items on that dragon.")
                return true
            }

            if (!anyInInventory(player, *STRANGE_KEYS)) {
                return true
            }

            if (removeAll(player, STRANGE_KEYS, Container.INVENTORY)) {
                visualize(npc, DRAGON_BREATH_ANIMATION, DRAGON_BREATH_GFX)
                sendItemDialogue(
                    player,
                    DRAGONKIN_KEY,
                    "The intense heat of the mithril dragon's breath fuses the key halves together."
                )
                addItem(player, DRAGONKIN_KEY.id, 1)
                runTask(player, 1) {
                    npc.attack(player)
                }
            }

            return true
        }
    }

    companion object {
        private const val DRAGON_ANVIL = 40200
        private val FUSION_HAMMER = Item(Items.BLAST_FUSION_HAMMER_14478, 1)
        private val RUINED_PIECES = intArrayOf(Items.RUINED_DRAGON_ARMOUR_LUMP_14472, Items.RUINED_DRAGON_ARMOUR_SLICE_14474, Items.RUINED_DRAGON_ARMOUR_SHARD_14476)
        private val DRAGON_PLATEBODY = Item(Items.DRAGON_PLATEBODY_14479, 1)
        private val REQUIRED_SHIELD = intArrayOf(Items.ANTI_DRAGON_SHIELD_1540, Items.DRAGONFIRE_SHIELD_11283, Items.DRAGONFIRE_SHIELD_11285)
        private val SMITHING_ANIMATION = Animation(Animations.HIT_WITH_BLAST_FUSION_HAMMER_10758, Animator.Priority.HIGH)
        private val MITHRIL_DOOR = intArrayOf(25341, 40208)
        private val MITHRIL_DRAGON_NPC = intArrayOf(NPCs.MITHRIL_DRAGON_5363, NPCs.MITHRIL_DRAGON_8424)
        private val STRANGE_KEYS = intArrayOf(Items.STRANGE_KEY_LOOP_14469, Items.STRANGE_KEY_TEETH_14470)
        private val DRAGONKIN_KEY = Item(Items.DRAGONKIN_KEY_14471, 1)
        private val DRAGON_BREATH_ANIMATION = Animation(Animations.DRAGON_BREATH_81, Animator.Priority.HIGH)
        private val DRAGON_BREATH_GFX = core.game.world.update.flag.context.Graphics(Graphics.DRAGON_FIRE_BREATH_DARKER_COLOR_953)
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return this
    }
}