package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.api.produceGroundItem
import core.api.queueScript
import core.api.sendMessage
import core.api.stopExecuting
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Items
import shared.consts.NPCs

@Initializable
class MacawFamiliar(owner: Player? = null, id: Int = NPCs.MACAW_6851) : Forager(owner, id, 3100, Items.MACAW_POUCH_12071, 12, *HERBS_IDS) {
    private var specialDelay = 0

    override fun construct(owner: Player, id: Int): Familiar = MacawFamiliar(owner, id)

    override fun newInstance(`object`: Any?): Plugin<Any> = super.newInstance(`object`)

    override fun getIds(): IntArray = intArrayOf(NPCs.MACAW_6851, NPCs.MACAW_6852)

    override fun specialMove(special: FamiliarSpecial): Boolean {
        if (specialDelay > GameWorld.ticks) {
            sendMessage(owner, "The macaw is too tired to fly for herbs. Try again shortly.")
            return false
        }
        val randomHerb = HERBS_IDS.random()
        animate(Animation.create(8013))
        graphics(Graphics.create(1321), 2)
        queueScript(owner, 5, QueueStrength.SOFT) {
            produceGroundItem(owner, randomHerb.id, 1, getLocation())
            return@queueScript stopExecuting(owner)
        }
        specialDelay = GameWorld.ticks + 100
        return true
    }

    companion object {
        private val HERBS_IDS =
            arrayOf(
                Item(Items.GRIMY_GUAM_199),
                Item(Items.GRIMY_RANARR_207),
                Item(Items.GRIMY_MARRENTILL_201),
                Item(Items.GRIMY_AVANTOE_211),
                Item(Items.GRIMY_IRIT_209),
                Item(Items.GRIMY_TARROMIN_203),
                Item(Items.GRIMY_HARRALANDER_205),
                Item(Items.GRIMY_KWUARM_213),
                Item(Items.GRIMY_DWARF_WEED_217),
                Item(Items.GRIMY_CADANTINE_215),
                Item(Items.GRIMY_LANTADYME_2485),
            )
    }
}
