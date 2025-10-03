package content.region.other.zanaris.quest.lostcity.plugin

import core.api.hasLevelStat
import core.api.sendItemZoomOnInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import shared.consts.Components
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Vars

@Initializable
class LostCity: Quest(Quests.LOST_CITY, 83, 82, 3, Vars.VARP_QUEST_LOST_CITY_PROGRESS_147, 0, 1, 6) {
    class SkillRequirement(
        val skill: Int?,
        val level: Int?,
    )

    val requirements = arrayListOf<SkillRequirement>()

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11

        if (stage == 0) {
            line(player, "I can start this quest by speaking to the !!Adventurers?? in", line++)
            line(player, "the !!Swamp?? just south of !!Lumbridge??.", line++)
            drawQuestRequirements(player)
            line++
        }

        if (stage == 10) {
            line(player, "According to one of the adventurers in Lumbridge Swamp", line++, true)
            line(player, "the entrance to Zanaris is somewhere around there.", line++, true)
            line(player, "Apparently there is a !!leprechaun?? hiding in a !!tree?? nearby", line++)
            line(player, "who can tell me how to enter the !!Lost City of Zanaris??", line++)
            line++
        }
        if (stage == 20) {


            line(player, "According to one of the adventurers in Lumbridge Swamp", line++, true)
            line(player, "the entrance to Zanaris is somewhere around there.", line++, true)
            line(player, "I found the Leprechaun hiding in a nearby tree.", line++, true)
            line(player, "He told me that the entrance to !!Zanaris?? is in the !!shed?? in", line++)
            line(player, "!!Lumbridge swamp?? but only if I am carrying a !!Dramen Staff??", line++)
            line(player, "I can find a !!Dramen Tree?? in a cave on !!Entrana?? somewhere", line++)
            line++
        }
        if (stage >= 21) {
            if (player.hasItem(Item(Items.DRAMEN_STAFF_772, 1))) {
                line(player, "According to one of the adventurers in Lumbridge Swamp", line++, true)
                line(player, "the entrance to Zanaris is somewhere around there.", line++, true)
                line(player, "I found the Leprechaun hiding in a nearby tree.", line++, true)
                line(player, "He told me that the entrance to Zanaris is in the shed in", line++, true)
                line(player, "Lumbridge swamp but only if I am carrying a Dramen Staff.", line++, true)
                line(player, "The Dramen Tree was guarded by a powerful Tree Spirit.", line++, true)
                line(player, "I cut a branch from the tree and crafted a Dramen Staff.", line++, true)
                line(player, "I should enter !!Zanaris?? by going to the !!shed?? in !!Lumbridge??", line++)
                line(player, "!!Swamp?? while keeping the !!Dramen staff?? with me", line++)
                line++
            } else {
                line(player, "According to one of the adventurers in Lumbridge Swamp", line++, true)
                line(player, "the entrance to Zanaris is somewhere around there.", line++, true)
                line(player, "I found the Leprechaun hiding in a nearby tree.", line++, true)
                line(player, "He told me that the entrance to Zanaris is in the shed in", line++, true)
                line(player, "Lumbridge swamp but only if I am carrying a Dramen Staff.", line++, true)
                line(player, "The Dramen Tree was guarded by a powerful Tree Spirit.", line++, true)
                line(player, "With the !!Spirit?? defeated I can cut a !!branch?? from the tree", line++, true)
                line++
            }
        }

        if (stage == 100) {
            line(player, "According to one of the adventurers in Lumbridge Swamp", line++, true)
            line(player, "the entrance to Zanaris is somewhere around there.", line++, true)
            line(player, "I found the Leprechaun hiding in a nearby tree.", line++, true)
            line(player, "He told me that the entrance to Zanaris is in the shed in", line++, true)
            line(player, "Lumbridge swamp but only if I am carrying a Dramen Staff.", line++, true)
            line(player, "The Dramen Tree was guarded by a powerful Tree Spirit.", line++, true)
            line(player, "I cut a branch from the tree and crafted a Dramen Staff.", line++, true)
            line(player, "With the mystical Dramen Staff in my possession I was", line++, true)
            line(player, "able to enter Zanaris through the shed in Lumbridge", line++, true)
            line(player, "swamp.", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++, false)
        }
    }

    private fun drawQuestRequirements(player: Player) {
        var line = 7 + 7
        val questRequirements = arrayOf("Level 31 Crafting", "Level 36 Woodcutting")
        val requireQuests = booleanArrayOf(hasLevelStat(player, Skills.CRAFTING, 31), hasLevelStat(player, Skills.WOODCUTTING, 36))
        line(player, "To complete this quest I will need:", line++)
        for (i in 0..1) {
            line(player, questRequirements[i], line++, requireQuests[i])
        }
        line(player, "and be able to defeat a !!Level 101 Spirit without weapons??.", line)
    }

    override fun finish(player: Player) {
        super.finish(player)
        var line = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.DRAMEN_STAFF_772, 235)
        drawReward(player, "3 Quest points", line++)
        drawReward(player, "Access to Zanaris", line)
    }

    override fun newInstance(`object`: Any?): Quest {
        requirements.add(SkillRequirement(Skills.WOODCUTTING, 36))
        requirements.add(SkillRequirement(Skills.CRAFTING, 31))
        return this
    }
}
