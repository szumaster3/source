package content.region.karamja.shilo.npc

import content.region.asgarnia.burthope.quest.hero.HeroesQuest
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class GripNPC : NPCBehavior(NPCs.GRIP_792) {

    override fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean): Boolean {
        if (attacker is Player && HeroesQuest.isBlackArm(attacker)) {
            openDialogue(
                attacker,
                object : DialogueFile() {
                    override fun handle(componentID: Int, buttonID: Int) {
                        when (stage) {
                            START_DIALOGUE -> sendPlayerDialogue(attacker, "I can't attack the head guard here! There are too many witnesses around to see me do it! I'd have the whole of Brimhaven after me! Besides, if he dies I want the promotion!").also { stage++ }
                            1 -> sendDialogueLines(attacker, "Perhaps you need another player's help...?").also { stage = END_DIALOGUE }
                        }
                    }
                },
            )
            return false
        }
        return true
    }

    override fun onDeathFinished(self: NPC, killer: Entity) {
        if (killer is Player) {
            if (getQuestStage(killer, Quests.HEROES_QUEST) == 4) {
                setQuestStage(killer, Quests.HEROES_QUEST, 5)
                val gi = GroundItemManager.create(GroundItem(Item(Items.GRIPS_KEY_RING_1588), self.location, killer),)
                gi!!.forceVisible = true
                gi.isRemainPrivate = false
            }
        }
    }
}