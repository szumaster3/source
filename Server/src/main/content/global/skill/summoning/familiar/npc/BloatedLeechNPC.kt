package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.curePoison
import core.api.removeTimer
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.timer.impl.Disease
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs
import kotlin.math.ceil

@Initializable
class BloatedLeechNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.BLOATED_LEECH_6843) :
    Familiar(owner, id, 3400, Items.BLOATED_LEECH_POUCH_12061, 6, WeaponInterface.STYLE_ACCURATE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return BloatedLeechNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        curePoison(owner)
        removeTimer<Disease>(owner)

        for (i in Skills.SKILL_NAME.indices) {
            if (i == Skills.PRAYER) continue

            val current = owner.skills.getLevel(i)
            val base = owner.skills.getStaticLevel(i)
            if (current < base) {
                val restored = ceil(base * 0.2).toInt()
                owner.skills.setLevel(i, minOf(current + restored, base))
            }
        }

        val damage = RandomFunction.random(1, 5)
        owner.impactHandler.manualHit(owner, damage, HitsplatType.NORMAL)
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BLOATED_LEECH_6843, NPCs.BLOATED_LEECH_6844)
    }
}