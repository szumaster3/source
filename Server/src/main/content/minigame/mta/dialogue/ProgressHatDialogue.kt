package content.minigame.mta.dialogue

import content.minigame.mta.plugin.MTAZone
import content.minigame.mta.plugin.ProgressHat
import core.api.getVarbit
import core.api.hasAnItem
import core.api.removeItem
import core.api.sendDialogueOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.splitLines
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Progress hat dialogue.
 */
@Initializable
class ProgressHatDialogue(player: Player? = null) : Dialogue(player) {

    private var progressHat: Item? = null

    private val randomGreetings = arrayOf(
        "Hello... Mr Progress Hat?",
        "Erm, excuse me Mr Progress Hat?",
        "Mr Progress Hat... sir?",
        "Mr Progress Hat? Hello?",
    )

    private val responses = arrayOf(
        arrayOf(
            "That's me, why are you bothering me human?",
            "Sorry, but do you think you could tell me my Pizazz Points?",
        ),
        arrayOf(
            "Can't you see I'm busy?", "But you're just a hat? Can you tell me my Pizazz Point totals?"
        ),
        arrayOf(
            "What do you want?", "Do you think you could tell me my Pizazz Points?"
        ),
        arrayOf(
            "I suppose you want to know your Pizazz Points.", "That would be nice, yes."
        ),
    )

    override fun open(vararg args: Any?): Boolean {
        if (args.size > 1) {
            progressHat = args[0] as? Item
            npc(FaceAnim.OLD_NORMAL, "How dare you destroy me? You'll lose your Pizazz Points!")
            stage = 4
            return true
        }
        player(randomGreetings.random())
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val randomIndex = (0..3).random()
        val response = responses[randomIndex]

        when (stage) {
            0 -> {
                npc(FaceAnim.OLD_NORMAL, *splitLines(response[0]))
                stage = 1
            }

            1 -> {
                player(FaceAnim.HALF_ASKING, *splitLines(response[1]))
                stage = 2
            }

            2 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Ok, I suppose it's my job. You have:",
                    "${MTAZone.getPoints(player,0)} Telekinetic, ${MTAZone.getPoints(player, 1)} Alchemist,",
                    "${MTAZone.getPoints(player, 2)} Enchantment, and ${MTAZone.getPoints(player, 3)} Graveyard Pizazz",
                    "Points."
                )
                stage = 3
            }

            3 -> {
                player("Thank you!")
                stage = END_DIALOGUE
            }

            4 -> {
                sendDialogueOptions(player, "Destroy Hat?", "Yes", "No")
                stage = 5
            }

            5 -> {
                when (buttonId) {
                    1 -> {
                        val hasHat = hasAnItem(player, *ProgressHat.hatIds).container != null
                        if (!hasHat) {
                            return true
                        }

                        progressHat?.let { hat ->
                            ProgressHat.resetProgress(player)
                            removeItem(player, hat)
                            return true
                        }

                        stage = END_DIALOGUE
                        return true
                    }

                    2 -> {
                        npc(FaceAnim.OLD_NORMAL, "I think so too!")
                        stage = END_DIALOGUE
                    }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.PIZZAZ_HAT_3096)
}
