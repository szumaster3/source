package content.region.misthalin.silvarea.quest.rag.npc

import core.api.isQuestInProgress
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class UnicornNPC : NPCBehavior(*unicornIds) {
    companion object {
        private val unicornIds = intArrayOf(NPCs.UNICORN_89, NPCs.UNICORN_987, NPCs.BLACK_UNICORN_133)
    }

    override fun onDropTableRolled(
        self: NPC,
        killer: Entity,
        drops: ArrayList<Item>,
    ) {
        super.onDropTableRolled(self, killer, drops)

        if (killer is Player && isQuestInProgress(killer, Quests.RAG_AND_BONE_MAN, 1, 99)) {
            if (RandomFunction.roll(4)) {
                drops.add(Item(Items.UNICORN_BONE_7821))
            }
        }
    }
}
