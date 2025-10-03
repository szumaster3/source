package content.global.skill.thieving

import core.api.lockInteractions
import core.api.sendDialogue
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.PulseType
import core.game.node.scenery.Scenery

class ThievingOptionPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles thieving interaction options.
         */

        on(IntType.SCENERY, "steal-from", "steal from", "steal") { player, node ->
            submitIndividualPulse(player, StallThiefPulse(player, node as Scenery, Stall.forScenery(node)), type = PulseType.STANDARD)
            lockInteractions(player, 6)
            return@on true
        }

        /*
         * Handles clothes stall interaction in Keldagrim.
         */

        on(shared.consts.Scenery.CLOTHES_STALL_6165, IntType.SCENERY, "steal-from") { player, _ ->
            sendDialogue(player, "You don't really see anything you'd want to steal from this stall.")
            return@on true
        }
    }
}
