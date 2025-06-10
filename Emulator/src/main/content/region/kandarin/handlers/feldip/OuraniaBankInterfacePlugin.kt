package content.region.kandarin.handlers.feldip

import core.api.getAttribute
import core.api.interaction.openBankAccount
import core.api.interaction.openGrandExchangeCollectionBox
import core.api.removeItem
import core.api.sendNPCDialogue
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class OuraniaBankInterfacePlugin : ComponentPlugin() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.BANK_CHARGE_ZMI_619, this)
        return this
    }

    override fun handle(
        player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int
    ): Boolean {
        if (player == null || component == null) return false
        if (component.id != Components.BANK_CHARGE_ZMI_619) return false

        val runeItemId = BUTTON_TO_RUNE[button] ?: return true

        if (!removeItem(player, Item(runeItemId, 20))) {
            sendNPCDialogue(
                player,
                NPCs.ENIOLA_6362,
                "You need at least 20 of that rune type to access your bank.",
                FaceAnim.NEUTRAL
            )
            return true
        }

        when (getAttribute(player, "zmi:bankaction", "")) {
            "open" -> openBankAccount(player)
            "collect" -> openGrandExchangeCollectionBox(player)
        }
        return true
    }

    companion object {
        private val BUTTON_TO_RUNE = mapOf(
            28 to Items.AIR_RUNE_556,
            29 to Items.MIND_RUNE_558,
            30 to Items.WATER_RUNE_555,
            31 to Items.EARTH_RUNE_557,
            32 to Items.FIRE_RUNE_554,
            33 to Items.BODY_RUNE_559,
            34 to Items.COSMIC_RUNE_564,
            35 to Items.CHAOS_RUNE_562,
            36 to Items.ASTRAL_RUNE_9075,
            37 to Items.LAW_RUNE_563,
            38 to Items.DEATH_RUNE_560,
            39 to Items.BLOOD_RUNE_565,
            40 to Items.NATURE_RUNE_561,
            41 to Items.SOUL_RUNE_566,
        )
    }
}