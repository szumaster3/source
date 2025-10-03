package content.region.fremennik.rellekka.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Represents the Rellekka citizens dialogue.
 */
@Initializable
class RellekkaCitizenDialogue(player: Player? = null) : Dialogue(player) {

    val stages = intArrayOf(0, 100, 200, 300)

    override fun open(vararg args: Any?): Boolean {
        if(npc.id in intArrayOf(NPCs.FRIDGEIR_1277, NPCs.OSPAK_1274, NPCs.STYRMIR_1275)) {
            npc(FaceAnim.ANNOYED, "Shhh! I'm waiting for the show!")
            stage = 400
        } else {
            stage = stages.random()
            handle(0, 0)
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.NEUTRAL, "It's a bit grey round here, isn't it?").also { stage++ }
            1 -> npcl(FaceAnim.NEUTRAL, "It gets you down after a while, you know. There are 273 shades of grey, you know, and we have them all.").also { stage++ }
            2 -> playerl(FaceAnim.NEUTRAL, "That's grey-t.").also { stage++ }
            3 -> npcl(FaceAnim.SAD, "That attempt at humour merely made me more depressed. Leave me alone.").also { stage = 400 }
            100 -> playerl(FaceAnim.NEUTRAL, "Cheer up! It's not the end of the world.").also { stage++ }
            101 -> npcl(FaceAnim.SAD, "I'd prefer that, if it meant I didn't have to talk to people as inanely happy as you.").also { stage++ }
            102 -> playerl(FaceAnim.AMAZED, "Whoa! I think you need to get out more.").also { stage = 400 }
            200 -> playerl(FaceAnim.NEUTRAL, "How's the King treating you then?").also { stage++ }
            201 -> npcl(FaceAnim.SAD, "Like serfs.").also { stage++ }
            202 -> playerl(FaceAnim.HALF_THINKING, "Serf?").also { stage++ }
            203 -> npcl(FaceAnim.SAD, "Yes, you know - peons, plebs, the downtrodden. He treats us like his own personal possessions.").also { stage++ }
            204 -> playerl(FaceAnim.NEUTRAL, "You should leave this place.").also { stage++ }
            205 -> npcl(FaceAnim.SAD, "I keep trying to save up enough to leave, but the King keeps taxing us! We have no money left.").also { stage++ }
            206 -> playerl(FaceAnim.SAD, "Oh dear.").also { stage = 400 }
            300 -> playerl(FaceAnim.HALF_THINKING, "How are you today?").also { stage++ }
            301 -> npcl(FaceAnim.SAD, "**sigh**").also { stage++ }
            302 -> playerl(FaceAnim.ASKING, "That good? Everyone around here seems a little depressed. ").also { stage++ }
            303 -> npcl(FaceAnim.SAD, "**sigh**").also { stage++ }
            304 -> playerl(FaceAnim.HALF_THINKING, "And not particularly talkative.").also { stage++ }
            305 -> npcl(FaceAnim.SAD, "**sigh**").also { stage++ }
            306 -> playerl(FaceAnim.HALF_THINKING, "I'll leave you to your sighing. It looks like you have plenty to do.").also { stage = 400 }
            400 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = RellekkaCitizenDialogue(player)

    override fun getIds(): IntArray = intArrayOf(
        NPCs.OSPAK_1274,
        NPCs.STYRMIR_1275,
        NPCs.FRIDGEIR_1277,
        NPCs.FREYGERD_1310,
        NPCs.FREYGERD_5493,
        NPCs.LENSA_5494,
        NPCs.SASSILIK_5496
    )
}
