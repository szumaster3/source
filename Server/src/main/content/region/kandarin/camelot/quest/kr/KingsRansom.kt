package content.region.kandarin.camelot.quest.kr

import core.api.addItemOrDrop
import core.api.rewardXP
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Vars

// @Initializable
class KingsRansom : Quest(Quests.KINGS_RANSOM, 141, 140, 1, Vars.VARBIT_QUEST_KING_RANSOM_PROGRESS_3888, 0, 1, 90) {
    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        player ?: return

        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10

        player.packetDispatch.sendItemZoomOnInterface(Items.ANTIQUE_LAMP_11137, 230, 277, 5)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "33,000 Defence XP", ln++)
        drawReward(player, "5,000 Magic XP", ln++)

        rewardXP(player, Skills.ATTACK, 3075.0)
        rewardXP(player, Skills.DEFENCE, 3075.0)

        addItemOrDrop(player, Items.ANTIQUE_LAMP_11137)
        setVarbit(player, Vars.VARBIT_QUEST_KING_RANSOM_PROGRESS_3888, 90, true)
    }


    override fun newInstance(`object`: Any?): Quest {
        return this
    }

}
// 1 quest point
// 33,000 Defence experience
// 5,000 Magic experience
// An antique lamp granting 5,000 experience in any skill that is level 50 or above
// The hair clip (a more durable version of the lockpick) if obtained during quest
// Access to the Knight Waves Training Grounds miniquest
// Access to the Court Cases Distraction and Diversion
// 2 Treasure Hunter keys (Ironman accounts will not receive these)