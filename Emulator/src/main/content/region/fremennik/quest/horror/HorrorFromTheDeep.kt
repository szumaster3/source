package content.region.fremennik.quest.horror

import content.data.GameAttributes
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class HorrorFromTheDeep : Quest(Quests.HORROR_FROM_THE_DEEP, 77, 76, 2, Vars.VARBIT_QUEST_HORROR_FROM_THE_DEEP_PROGRESS_34, 0, 1, 10) {

    /*
     * Temporary constants values.
     */
    companion object {
        val STRANGE_DOOR_UNLOCKED = 35
        val BRIDGE_LEFT_REPAIRED = 36
        val BRIDGE_RIGHT_REPAIRED = 37

        val LIGHTHOUSE_KEY_OBTAINED = 38
        val LIGHTHOUSE_UNLOCKED = 39

        val STRANGE_WALL_FIRE_RUNE = 40
        val STRANGE_WALL_WATER_RUNE = 41
        val STRANGE_WALL_EARTH_RUNE = 42
        val STRANGE_WALL_AIR_RUNE = 43
        val STRANGE_WALL_SWORD = 44
        val STRANGE_WALL_ARROW = 45

        val LIGHTING_MECHANISM_FIRST_FIX = 46
        val LIGHTING_MECHANISM_SECOND_FIX = 47
        val LIGHTING_MECHANISM_THIRD_FIX = 48
    }

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        val progress = getVarbit(player, Vars.VARBIT_QUEST_HORROR_FROM_THE_DEEP_PROGRESS_34)
        val bridgeRepaired = getVarbit(player, BRIDGE_LEFT_REPAIRED) == 1 && getVarbit(player, BRIDGE_RIGHT_REPAIRED) == 1
        val lightHouseUnlocked = getVarbit(player, LIGHTHOUSE_UNLOCKED) == 1
        val lighthouseFix = getVarbit(player, LIGHTING_MECHANISM_THIRD_FIX)

        val godBooks = getAttribute(player, GameAttributes.GOD_BOOKS, false)
        var line = 11
        if (progress == 0) {
            line(player, "I can start this quest by speaking to !!Larrissa?? at the", line++)
            line(player, "!!Lighthouse?? to the !!North?? of the !!Barbarian Outpost??.", line++)
            line(player, "To complete this quest I need:", line++)
            line(
                player,
                if (hasLevelStat(player, Skills.AGILITY, 35)) "---Level 35 agility/--" else "!!Level 35 agility??",
                line++,
            )
            line(
                player,
                if (hasLevelStat(player, Skills.MAGIC, 13)) {
                    "---Level 13 or higher magic will be an advantage/--"
                } else {
                    "!!Level 13 or higher magic will be an advantage??"
                },
                line++,
            )
            line(player, "!!I must also be able to defeat strong level 100 enemies??", line++)
            limitScrolling(player, line, true)
            line++
        }
        if (progress == 1) {
            line(player, "I travelled to an isolated !!Lighthouse?? north of the !!Barbarian outpost??,", line++, true)
            line(player, "to find a !!Fremennik?? girl called !!Larrissa?? locked outside, and worried", line++, true)
            line(player, "about her boyfriend !!Jossik??.", line++, true)
            line++
            line(player, "I need to !!repair the bridge?? leading to Rellekka.", line++, bridgeRepaired)

            line(
                player,
                if (getVarbit(player, LIGHTHOUSE_KEY_OBTAINED) == 1 && inInventory(player, Items.LIGHTHOUSE_KEY_3848))
                    "I also need to use the key I got from Gunnjorn to enter the lighthouse."
                else
                    "I also need to get the !!lighthouse key?? from her cousin !!Gunnjorn??.",
                line++
            )
            line++
        }
        if (lightHouseUnlocked && stage < 100) {
            line(player, "I recovered a !!spare key?? from Larrissa's cousin !!Gunnjorn??", line++, true)
            line(player, "and repaired the !!bridge?? to Rellekka with some planks.", line++, true)
            line(player, "I also need to use the key I got from Gunnjorn", line++, true)
            line(player, "to enter the lighthouse.", line++, true)
            line(player, if (lighthouseFix == 1) "---I managed to repair the lighthouse light with some molten glass, some swamp tar and a tinderbox./--" else "Now I need to find some way of !!fixing?? the !!lighthouse lamp??.", line++)

            if (getAttribute(player, GameAttributes.QUEST_HFTD_STRANGE_WALL_DISCOVER, false)) {
                line(player, "After I entered the !!lighthouse??, and repaired the !!lighting mechanism??,", line++, stage >= 55)
                line(player, "I discovered a !!strange wall?? that blocked the entrance to an underground", line++, stage >= 55)
                line(player, "cavern, where !!Jossik?? was.", line++, stage >= 55)
                line++
            }

            if (getVarbit(player, STRANGE_DOOR_UNLOCKED) == 1) {
                line(player, "I found Jossik in an underground cavern, behind a strange wall", line++, stage >= 55)
                line(player, "where he had been attacked by some sea creatures.", line++, stage >= 55)
                line(player, "I must defeat these sea monsters to save him!", line++, stage >= 60)
                line++
            }
            if (stage >= 60) {
                line(player, "After I killed some strange sea monsters, I managed", line++, true)
                line(player, "to get !!Jossik?? out of the cavern and back to the", line++, true)
                line(player, "lighthouse.", line++, true)
            }
        } else {
            line(player, "I repaired the bridge leading to Rellekka and got a key from Gunnjorn", line++, true)
            line(player, "so I could enter the lighthouse.", line++, true)
            line(player, "I managed to repair the lighthouse light with some molten glass,", line++, true)
            line(player, "some swamp tar and a tinderbox.", line++, true)

            line(player, "I found Jossik in an underground cavern, behind a strange wall", line++, true)
            line(player, "where he had been attacked by some sea creatures.", line++, true)
            line(player, "After I entered the lighthouse, and repaired the lighting mechanism,", line++, true)
            line(player, "I discovered a strange wall that blocked the entrance to an underground cavern,", line++, true)
            line(player, "where Jossik was.", line++, true)

            line(player, "After I killed some strange sea monsters,", line++, true)
            line(player, "I managed to get Jossik out of the cavern and back to the lighthouse.", line++, true)
            line(player, "I found a strange casket on the dead body of a sea monster,", line++, true)
            line(player, "which Jossik said he could tell me about.", line++, true)

            line(player, "I found a !!strange casket?? on the dead body of the !!sea monster??,", line++, godBooks)
            line(player, "which !!Jossik?? said he could tell me about.", line++, godBooks)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
            limitScrolling(player, line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        /*
         * Trivia: The only one interface that has different text in journal.
         */
        sendString(player, "You have survived the ${Quests.HORROR_FROM_THE_DEEP}!", Components.QUEST_COMPLETE_SCROLL_277, 4)
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.RUSTY_CASKET_3849)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "4662 XP in each of: Ranged,", ln++)
        drawReward(player, "Magic, Strength", ln)
        rewardXP(player, Skills.RANGE, 4662.0)
        rewardXP(player, Skills.MAGIC, 4662.0)
        rewardXP(player, Skills.STRENGTH, 4662.0)
    }

    override fun newInstance(`object`: Any?): Quest = this
}
