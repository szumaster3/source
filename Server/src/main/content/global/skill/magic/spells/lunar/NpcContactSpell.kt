package content.global.skill.magic.spells.lunar

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.openInterface
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items

class NpcContactSpell : SpellListener("lunar") {
    override fun defineListeners() {
        onCast(LunarSpells.NPC_CONTACT, NONE) { player, _ ->
            requires(
                player,
                67,
                arrayOf(Item(Items.ASTRAL_RUNE_9075), Item(Items.COSMIC_RUNE_564), Item(Items.AIR_RUNE_556, 2)),
            )
            openInterface(player, Components.NPC_CONTACT_429)
            player.setAttribute("contact-caller") {
                removeRunes(player)
                addXP(player, 63.0)
                setDelay(player, false)
                visualizeSpell(player, Animations.LUNAR_NPC_CONTACT_4413, 728, 130, 3618)
            }
        }
    }
}
