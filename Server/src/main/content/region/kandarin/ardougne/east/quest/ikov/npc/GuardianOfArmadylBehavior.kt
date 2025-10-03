package content.region.kandarin.ardougne.east.quest.ikov.npc

import core.api.isQuestComplete
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class GuardianOfArmadylBehavior : NPCBehavior(NPCs.GUARDIAN_OF_ARMADYL_274, NPCs.GUARDIAN_OF_ARMADYL_275) {
    override fun onDropTableRolled(self: NPC, killer: Entity, drops: ArrayList<Item>) {
        super.onDropTableRolled(self, killer, drops)
        if (killer is Player && isQuestComplete(killer, Quests.TEMPLE_OF_IKOV)) {
            if (RandomFunction.roll(4)) {
                drops.add(Item(Items.ARMADYL_PENDANT_87))
            }
        }
    }
}
