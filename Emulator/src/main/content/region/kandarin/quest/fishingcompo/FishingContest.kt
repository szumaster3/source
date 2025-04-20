package content.region.kandarin.quest.fishingcompo

import content.data.GameAttributes
import core.api.hasLevelStat
import core.api.removeAttributes
import core.api.rewardXP
import core.api.sendItemZoomOnInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests

/**
 * Represents the Fishing Contest quest.
 *
 * Authors: [Woah](https://gitlab.com/woahscam), szu.
 */
@Initializable
class FishingContest : Quest(Quests.FISHING_CONTEST, 62, 61, 1, 11, 0, 1, 5) {

    /**
     * Draws the quest journal.
     */
    @OptIn(ExperimentalStdlibApi::class)
    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11

        when (stage) {
            0 -> {
                line(player, "I can start this quest by speaking to the !!Dwarves?? at the", line++, false)
                line(player, "tunnel entrances on either side of !!White Wolf mountain??.", line++, false)
                line(player, if (hasLevelStat(player, Skills.FISHING, 10)) "---I must have level 10 fishing./--" else "!!I must have level 10 fishing??.", line++)
            }

            in 1..<100 -> {
                line(player, "The Dwarves will let me use the tunnel through White Wolf", line++, true)
                line(player, "Mountain if I can win the Hemenster Fishing Competition.", line++, true)

                if (stage >= 20) {
                    line(player, "I easily won the contest by catching some Giant Carp.", line++, false)
                } else if (stage >= 10) {
                    line(player, "They gave me a !!Fishing Contest Pass?? to enter the contest.", line++, false)
                    line(player, "I need to bring them back the !!Hemenster Fishing Trophy??.", line++, false)
                }

                if (stage >= 20) {
                    line(player, "I should take the !!Trophy?? back to the !!Dwarf?? at the side of", line++, false)
                    line(player, "!!White Wolf Mountain?? and claim my !!reward??.", line++, false)
                }
            }

            else -> {
                line(player, "The Dwarves wanted me to earn their friendship by winning", line++, true)
                line(player, "the Hemenster Fishing Competition.", line++, true)
                line(player, "I scared away a vampyre with some garlic and easily won the", line++, true)
                line(player, "contest by catching some Giant Carp.", line++, true)
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++, false)
                line++
                line(player, "As a reward for getting the Fishing Competition Trophy the", line++, false)
                line(player, "Dwarves will let me use their tunnel to travel quickly and", line++, false)
                line(player, "safely under White Wolf Mountain anytime I wish.", line++, false)
            }
        }
    }

    /**
     * Finishes the quest.
     *
     * @param player the player completing the quest.
     */
    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.FISHING_TROPHY_26, 230)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "2437 Fishing XP.", ln++)
        drawReward(player, "Access to the White Wolf Mountain shortcut.", ln)
        rewardXP(player, Skills.FISHING, 2437.0)
        removeAttributes(player, GameAttributes.QUEST_FISHINGCOMPO_STASH_GARLIC, GameAttributes.QUEST_FISHINGCOMPO_WON)
    }

    override fun newInstance(`object`: Any?): Quest = this

}
