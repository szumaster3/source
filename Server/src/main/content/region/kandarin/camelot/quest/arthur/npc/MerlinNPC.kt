package content.region.kandarin.camelot.quest.arthur.npc

import core.api.getQuestStage
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Merlin NPC (Camelot Castle).
 */
@Initializable
class MerlinNPC : AbstractNPC {
    constructor() : super(0, null)

    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = MerlinNPC(id, location)

    override fun isHidden(player: Player): Boolean = getQuestStage(player, Quests.MERLINS_CRYSTAL) in 0..99

    override fun getIds(): IntArray = intArrayOf(NPCs.MERLIN_213)
}