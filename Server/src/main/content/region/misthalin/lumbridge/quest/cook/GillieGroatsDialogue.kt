package content.region.misthalin.lumbridge.quest.cook

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

@Initializable
class GillieGroatsDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        var milk = false
        if (args.size == 2) milk = true
        if (milk) {
            npc(FaceAnim.LAUGH, "Tee hee! You've never milked a cow before, have you?")
            stage = 100
            return true
        }
        npc(FaceAnim.HAPPY, "Hello, I'm Gillie the Milkmaid. What can I do for you?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Who are you?", "Can you tell me how to milk a cow?", "I'm fine, thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 ->
                        npc(
                            FaceAnim.HAPPY,
                            "My name's Gillie Groats. My father is a farmer and I",
                            "milk the cows for him.",
                        ).also { stage = 10 }

                    2 -> player(FaceAnim.ASKING, "So how do you get milk from a cow then?").also { stage = 20 }
                    3 -> player(FaceAnim.NEUTRAL, "I'm fine, thanks.").also { stage = 1000 }
                }

            10 -> player(FaceAnim.ASKING, "Do you have any buckets of milk spare?").also { stage++ }
            11 ->
                npc(
                    FaceAnim.SUSPICIOUS,
                    "I'm afraid not. We need all of our milk to sell to",
                    "market, but you can milk the cow yourself if you need",
                    "milk.",
                ).also { stage++ }

            12 -> player(FaceAnim.HAPPY, "Thanks.").also { stage = 1000 }
            20 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "It's very easy. First you need an empty bucket to hold",
                    "the milk.",
                ).also { stage++ }

            21 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Then find a dairy cow to milk - you can't milk just",
                    "any cow.",
                ).also { stage++ }

            22 -> player(FaceAnim.ASKING, "How do I find a dairy cow?").also { stage++ }
            23 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "They are easy to spot - they are dark brown and",
                    "white, unlike beef cows, which are light brown and white.",
                    "We also tether them to a post to stop them wandering",
                    "around all over the place.",
                ).also { stage++ }

            24 -> npc(FaceAnim.FRIENDLY, "There are a couple very near, in this field.").also { stage++ }
            25 ->
                npc(
                    FaceAnim.HAPPY,
                    "Then just milk the cow and your bucket will fill with",
                    "tasty, nutritious milk.",
                ).also { stage = 1000 }

            100 -> player(FaceAnim.ASKING, "Erm... No. How could you tell?").also { stage++ }
            101 ->
                npc(
                    FaceAnim.LAUGH,
                    "Because you're spilling milk all over the floor. What a",
                    "waste ! You need something to hold the milk.",
                ).also { stage++ }

            102 ->
                player(
                    FaceAnim.NEUTRAL,
                    "Ah yes, I really should have guessed that one, shouldn't",
                    "I?",
                ).also { stage++ }

            103 ->
                npc(
                    FaceAnim.LAUGH,
                    "You're from the city, aren't you? Try it again with an",
                    "empty bucket.",
                ).also { stage++ }

            104 -> player(FaceAnim.NEUTRAL, "Right, I'll do that.").also { stage = 1000 }
            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GILLIE_GROATS_3807)
}
