package content.region.misthalin.varrock.quest.soulbane.plugin

import core.api.sendDialogueLines
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Scenery

class ASoulsBanePlugin : InteractionListener {
    override fun defineListeners() {
        on(Scenery.WARNING_SIGN_14002, IntType.SCENERY, "read") { player, _ ->
            sendDialogueLines(player, "Here lies the Dungeon of Tolna! Since the rescue of", "Tolna from this rift, the monsters below have improved their", "combat strengths - so beware! Just because you found the monsters", "easy to kill last time, doesn't mean they will be easy this time!")
            return@on true
        }
    }
}
