package content.region.kandarin.quest.itwatchtower.dialogue

import core.game.dialogue.DialogueFile

/**
 * Represents the Enclave guard dialogue.
 *
 * Relations
 * - [Watchtower Quest][content.region.kandarin.quest.itwatchtower.Watchtower]
 */
class EnclaveGuardDialogue : DialogueFile() {
    // The guard is occupied at the moment
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage) {
            0 -> return
        }
    }
}