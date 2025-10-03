package content.region.other.dorgeshuun.quest.lotg

import core.api.rewardXP
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import shared.consts.Items
import shared.consts.Vars

// @Initializable
class LandOfTheGoblins :
    Quest("Land of the Goblins", 144, 143, 1, Vars.VARBIT_QUEST_LAND_OF_THE_GOBLIN_PROGRESS_4105, 0, 1, 18) {

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

        player.packetDispatch.sendItemZoomOnInterface(Items.ZANIK_8870, 230, 277, 5)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "3,000 Agility, Herblore,", ln++)
        drawReward(player, "Thieving,Fishing and Strength", ln++)
        drawReward(player, "XP", ln++)
        drawReward(player, "2,000 Prayer XP", ln++)

        rewardXP(player, Skills.AGILITY, 3000.0)
        rewardXP(player, Skills.HERBLORE, 3000.0)
        rewardXP(player, Skills.THIEVING, 3000.0)
        rewardXP(player, Skills.FISHING, 3000.0)
        rewardXP(player, Skills.STRENGTH, 3000.0)
        rewardXP(player, Skills.PRAYER, 2000.0)
        setVarbit(player, Vars.VARBIT_QUEST_LAND_OF_THE_GOBLIN_PROGRESS_4105, 18, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

}
//1 quest point
//3,000 Agility experience
//3,000 Herblore experience
//3,000 Thieving experience
//3,000 Fishing experience
//3,000 Strength experience
//2,000 Prayer experience
//Access to the Goblin Temple (with an altar to recharge prayer points).
//Access to Yu'biusk, using fairy rings.
//Ability to buy moving-over-distance spheres (Plain of Mud Sphere) that transports you to the goblin cave.
//Ability to make goblin potions.
//Access to the Bandos gravestone (talk to Blasidar the sculptor in Keldagrim).
//Ability to access the beacon located near the Goblin Village, west of Ice Mountain.
//2 Treasure Hunter keys (Ironman accounts will not receive these)