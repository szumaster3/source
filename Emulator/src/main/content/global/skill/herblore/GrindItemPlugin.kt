package content.global.skill.herblore

import core.api.addItem
import core.api.amountInInventory
import core.api.removeItem
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.skill.SkillPulse
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.net.packet.PacketRepository
import core.net.packet.context.ChildPositionContext
import core.net.packet.out.RepositionChild
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items
import kotlin.math.ceil
import kotlin.math.roundToInt

@Initializable
class GrindItemPlugin : UseWithHandler(Items.PESTLE_AND_MORTAR_233) {

    override fun newInstance(arg: Any?): Plugin<Any>? {
        for (grind in GrindItem.values()) {
            for (i in grind.items) {
                addHandler(i, ITEM_TYPE, this)
            }
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val grind = GrindItem.forID(if (event.usedItem.id == 233) event.baseItem.id else event.usedItem.id)
        val handler = object : SkillDialogueHandler(event.player, SkillDialogue.ONE_OPTION, grind!!.product) {
            override fun create(amount: Int, index: Int) {
                player.pulseManager.run(object : SkillPulse<Item>(player, event.usedItem) {
                    var amt = 0

                    init {
                        amt = amount
                        if (amt > amountInInventory(player, node.id)) {
                            amt = amountInInventory(player, node.id)
                        }
                        if (node.id == FISHING_BAIT) {
                            if (amt > (amountInInventory(player, node.id) / 10)) {
                                amt = ceil(amountInInventory(player, node.id).toDouble() / 10).roundToInt()
                            }
                        }
                        super.setDelay(2)
                    }

                    override fun checkRequirements(): Boolean {
                        return true
                    }

                    override fun animate() {
                        player.animator.animate(ANIMATION)
                    }

                    override fun reward(): Boolean {
                        if (node.id == Items.FISHING_BAIT_313) {
                            var quantity = 0
                            quantity = if (amountInInventory(player, FISHING_BAIT) >= 10) {
                                10
                            } else {
                                amountInInventory(player, FISHING_BAIT)
                            }
                            if (removeItem(player, Item(node.id, quantity))) {
                                addItem(player, GrindItem.forID(node.id)!!.product, quantity)
                            }
                        } else {
                            if (removeItem(player, Item(node.id, 1))) {
                                addItem(player, GrindItem.forID(node.id)!!.product)
                            }
                        }
                        amt--
                        return amt <= 0
                    }
                })

            }

            override fun getAll(index: Int): Int {
                return amountInInventory(player, event.usedItem.id)
            }
        }
        handler.open()
        PacketRepository.send(RepositionChild::class.java, ChildPositionContext(event.player, 309, 2, 210, 15))
        return true
    }


    companion object {
        private val ANIMATION = Animation(Animations.PESTLE_MORTAR_364)
        private const val FISHING_BAIT = Items.FISHING_BAIT_313
    }
}