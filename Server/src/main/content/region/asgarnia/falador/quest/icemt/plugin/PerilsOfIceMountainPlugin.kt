package content.region.asgarnia.falador.quest.icemt.plugin

import core.api.sendDialogueLines
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items

class PerilsOfIceMountainPlugin : InteractionListener {

    override fun defineListeners() {
        on(Items.LETTER_13229, IntType.ITEM, "read") { player, _ ->
            sendDialogueLines(player, "The letter is written in an impenetrable dwarven script.", "Only a dwarf would be able to decipher it.")
            return@on true
        }
    }
}