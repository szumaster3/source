package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import java.util.*

/**
 * The type Bloated leech dialogue.
 */
@Initializable
class BloatedLeechDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = BloatedLeechDialogue(player)

    /**
     * Instantiates a new Bloated leech dialogue.
     */
    constructor()

    /**
     * Instantiates a new Bloated leech dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (Random().nextInt(4)) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm afraid it's going to have to come off, " + player.username + ".")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You're in a critical condition.")
                stage = 3
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Let's get a look at that brain of yours.")
                stage = 6
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I think we're going to need to operate.")
                stage = 9
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.HALF_ASKING, "What is?")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Never mind. Trust me, I'm almost a doctor.")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "I think I'll get a second opinion.")
                stage = END_DIALOGUE
            }

            3 -> {
                playerl(FaceAnim.HALF_ASKING, "Is it terminal?")
                stage++
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Not yet. Let me get a better look and I'll see what I can do about it.")
                stage++
            }

            5 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "There are two ways to take that...and I think I'll err on the side of caution.",
                )
                stage = END_DIALOGUE
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "What? My brains stay inside my head, thanks.")
                stage++
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "That's ok, I can just drill a hole.")
                stage++
            }

            8 -> {
                playerl(FaceAnim.HALF_ASKING, "How about you don't and pretend you did?")
                stage = END_DIALOGUE
            }

            9 -> {
                playerl(FaceAnim.FRIENDLY, "I think we can skip that for now.")
                stage++
            }

            10 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Who's the doctor here?")
                stage++
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "Not you.")
                stage++
            }

            12 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I may not be a doctor, but I'm keen. Does that not count?")
                stage++
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "In most other fields, yes; in medicine, no.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BLOATED_LEECH_6843, NPCs.BLOATED_LEECH_6844)
}
