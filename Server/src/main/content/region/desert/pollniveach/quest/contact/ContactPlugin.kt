package content.region.desert.pollniveach.quest.contact

import core.api.openInterface
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Components
import shared.consts.Items

class ContactPlugin : InteractionListener {

    override fun defineListeners() {
        on(BLOODY_PARCHMENT, IntType.ITEM, "Read") { player, _ ->
            val PARCHMENT_TEXT =
                arrayOf(
                    "",
                    "Kaleef",
                    "Your mission is to contact our agent inside Menaphos.",
                    "Use the tunnels beneath the temple of the lesser gods.",
                    "Be vigilant of traps and the hostile natives.",
                    "They're about as welcoming as the Menaphites only",
                    "slightly better looking.",
                    "",
                    "Os",
                )
            openInterface(player, BLOODY_SCROLL).also {
                sendString(player, PARCHMENT_TEXT.joinToString("<br>"), BLOODY_SCROLL, 1)
            }
            return@on true
        }
    }

    companion object {
        private const val BLOODY_PARCHMENT = Items.PARCHMENT_10585
        private const val BLOODY_SCROLL = Components.CONTACT_SCROLL_BLOOD_498
    }
}
