package content.region.kandarin.witch.quest.seaslug.dialogue

import content.region.kandarin.witch.plugin.PlatformHelper
import content.region.kandarin.witch.quest.seaslug.cutscene.HolgartCutscene
import core.api.Container
import core.api.inInventory
import core.api.getQuestStage
import core.api.setQuestStage
import core.api.removeItem
import core.api.sendItemDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Holgart dialogue extension.
 *
 * # Relations
 * - [Sea Slug quest][content.region.kandarin.witch.quest.seaslug.SeaSlug]
 */
class HolgartDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.SEA_SLUG)
        npc = NPC(NPCs.HOLGART_4866)
        when (questStage) {
            // Only a Few Holes.
            in 2..3 -> {
                when (stage) {
                    0 -> player(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
                    1 -> npc(FaceAnim.FRIENDLY, "Hello m'hearty.").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "I would like a ride on your boat to the fishing platform.").also { stage++ }
                    3 -> npc(FaceAnim.AFRAID, "I'm afraid it isn't sea worthy. It's full of holes. To fill the", "holes I'll need some swamp paste.").also { stage++ }
                    4 -> player(FaceAnim.ASKING, "Swamp paste?").also { stage++ }
                    5 -> npc(FaceAnim.FRIENDLY, "Yes, swamp tar mixed with flour and heated over a fire.").also { stage++ }
                    6 -> {
                        if (!inInventory(player!!, Items.SWAMP_PASTE_1941)) {
                            player(FaceAnim.HALF_ASKING, "Where can I find swamp paste?").also { stage++ }
                        } else {
                            npc("In fact, unless me nose be mistaken, you've got some in", "yer pack.").also { stage = 10 }
                        }
                    }
                    7 -> npc(FaceAnim.FRIENDLY, "Unfortunately the only supply of swamp tar is in", "the swamps below Lumbridge. It's too far for an old man", "like me to travel.").also { stage++ }
                    8 -> npc(FaceAnim.NEUTRAL, "If you make me some swamp paste I'll give you a ride", "in my boat.").also { stage++ }
                    9 -> player(FaceAnim.FRIENDLY, "I'll see what I can do.").also { stage = 20 }
                    10 -> player(FaceAnim.NOD_YES, "Oh yes, I forgot about that stuff. Can you use it?").also { stage++ }
                    11 -> npc("Aye lad. That be perfect.").also { stage++ }
                    12 -> {
                        sendItemDialogue(player!!, Items.SWAMP_PASTE_1941, "You give Holgart the swamp paste.")
                        removeItem(player!!, Items.SWAMP_PASTE_1941)
                        stage = 13
                    }

                    13 -> {
                        end()
                        HolgartCutscene(player!!).start()
                    }

                    20 -> {
                        end()
                        setQuestStage(player!!, Quests.SEA_SLUG, 4)
                    }
                }
            }

            // The Leaky Tub.
            4 -> {
                when (stage) {
                    0 -> player(FaceAnim.FRIENDLY, "Hello Holgart.").also { stage++ }
                    1 -> npc(FaceAnim.HALF_ASKING, "Hello m'hearty. Did you manage to make some swamp", "paste?").also { stage++ }
                    2 -> {
                        if (!removeItem(player!!, Items.SWAMP_PASTE_1941, Container.INVENTORY)) {
                            player(FaceAnim.NEUTRAL, "I'm afraid not.").also { stage = END_DIALOGUE }
                        } else {
                            player(FaceAnim.FRIENDLY, "Yes, I have some here.").also { stage++ }
                        }
                    }
                    3 -> sendItemDialogue(player!!, Items.SWAMP_PASTE_1941, "You give Holgart the swamp paste.").also { stage++ }
                    4 -> {
                        end()
                        HolgartCutscene(player!!).start()
                    }
                }
            }

            // During quest dialogue.
            in 5..99 -> {
                when (stage) {
                    0 -> player(FaceAnim.FRIENDLY, "Hello Holgart.").also { stage++ }
                    1 -> npc(FaceAnim.NEUTRAL, "Hello again land lover. There's some strange goings on,", "on that platform, I tell you.").also { stage++ }

                    2 -> options("Will you take me there?", "I'm keeping away from there.").also { stage++ }
                    3 -> when (buttonID) {
                        1 -> player(FaceAnim.HALF_ASKING, "Will you take me there?").also { stage++ }
                        2 -> player("I'm keeping away from there.").also { stage = 6 }
                    }

                    4 -> npc(FaceAnim.NEUTRAL,"Of course m'hearty. If that's what you want.").also { stage++ }
                    5 -> {
                        end()
                        PlatformHelper.sail(player!!, PlatformHelper.Travel.WITCHAVEN_TO_FISHING_PLATFORM)
                    }

                    6 -> npc("Fair enough m'hearty.").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}
