package content.global.skill.crafting.pottery

import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.StringUtils
import shared.consts.Animations
import shared.consts.Items

/**
 * Handles crafting pottery pulse.
 */
class PotteryCraftingPulse(player: Player?, node: Item?, var amount: Int, val pottery: Pottery) : SkillPulse<Item?>(player, node) {
    var ticks = 0

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.CRAFTING) < pottery.level) {
            sendMessage(player, "You need a crafting level of " + pottery.level + " to make this.")
            return false
        }
        if (!inInventory(player, Items.SOFT_CLAY_1761, 1)) {
            sendMessage(player, "You have run out of clay.")
            return false
        }
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0) {
            animate(player, ANIMATION)
        }
    }

    override fun reward(): Boolean {
        if (!clockReady(player, Clocks.SKILLING)) return false
        delayClock(player, Clocks.SKILLING, 5)

        if (removeItem(player, SOFT_CLAY)) {
            if (pottery == Pottery.BOWL && withinDistance(player, Location(3086, 3410, 0))) {
                setAttribute(player, "/save:diary:varrock:spun-bowl", true)
            }

            val item = pottery.unfinished
            player.inventory.add(item)
            rewardXP(player, Skills.CRAFTING, pottery.exp)

            sendMessage(player, "You make the clay into " + (if (StringUtils.isPlusN(item.name)) "an" else "a") + " " + item.name.lowercase() + ".")
            if (pottery == Pottery.POT && withinDistance(player, Location(3086, 3410, 0))) {
                finishDiaryTask(player, DiaryType.LUMBRIDGE, 0, 7)
            }
        }
        amount--
        return amount < 1
    }

    companion object {
        private const val ANIMATION = Animations.SPIN_POTTER_WHEEL_883
        private const val SOFT_CLAY = Items.SOFT_CLAY_1761
    }
}