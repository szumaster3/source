package content.region.misthalin.varrock.quest.surok.plugin

import core.api.amountInBank
import core.api.amountInInventory
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class OutlawNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = OutlawNPC(id, location)

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.OUTLAW_5842,
            NPCs.OUTLAW_5843,
            NPCs.OUTLAW_5844,
            NPCs.OUTLAW_5845,
            NPCs.OUTLAW_5846,
            NPCs.OUTLAW_5847,
            NPCs.OUTLAW_5848,
            NPCs.OUTLAW_5849,
            NPCs.OUTLAW_5850,
        )

    override fun onAttack(e: Entity) {
        sendChat("Stand and deliver!")
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            val player = killer.asPlayer()
            val quest = player.getQuestRepository().getQuest(Quests.WHAT_LIES_BELOW)
            if (quest.getStage(player) == 10) {
                val amount =
                    amountInInventory(player, WhatLiesBelowListener.RATS_PAPER) +
                        amountInBank(
                            player,
                            WhatLiesBelowListener.RATS_PAPER,
                        )
                if (amount < 5) {
                    GroundItemManager.create(Item(WhatLiesBelowListener.RATS_PAPER), getLocation())
                }
            }
        }
    }
}
