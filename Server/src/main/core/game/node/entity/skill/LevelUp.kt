package core.game.node.entity.skill

import content.data.GameAttributes
import core.api.*
import core.game.global.Skillcape
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.world.GameWorld
import core.game.world.update.flag.context.Graphics
import core.tools.DARK_RED
import shared.consts.Components
import shared.consts.Sounds
import shared.consts.Vars
import shared.consts.Graphics as Graphic

/**
 * Handles skill level-up logic for players.
 *
 * Manages:
 *  - Displaying level-up messages and graphics
 *  - Playing jingles and sounds for level-ups
 *  - Handling skill, combat, and total level milestones
 *  - Flashing skill icons in the stats tab
 *  - Awarding skillcapes and emotes
 */
object LevelUp {

    /**
     * Bitmask for flashing individual skill icons in the stats tab.
     */
    private val SKILL_ICON = intArrayOf(
        67108864, 335544320, 134217728, 402653184, 201326592, 469762048,
        268435456, 1073741824, 1207959552, 1275068416, 1006632960, 1140850688,
        738197504, 939524096, 872415232, 603979776, 536870912, 671088640,
        1342177280, 1409286144, 805306368, 1543503872, 1476395008, 1610612736, 1677721600
    )

    /**
     * Bitmask used for flashing icons per skill level-up.
     */
    private val FLASH_ICONS = intArrayOf(
        1, 4, 2, 64, 8, 16, 32, 32768, 131072, 2048,
        16384, 65536, 1024, 8192, 4096, 256, 128, 512,
        524288, 1048576, 262144, 4194304, 2097152, 8388608, 16777216
    )

    /**
     * Config values used when updating skill advance visuals.
     */
    private val ADVANCE_CONFIGS = intArrayOf(
        9, 40, 17, 49, 25, 57, 33, 641, 659, 664,
        121, 649, 89, 114, 107, 72, 64, 80, 673, 680,
        99, 698, 689, 705
    )

    /**
     * Skill level milestones used for milestone visuals and notifications.
     */
    private val SKILL_MILESTONES = intArrayOf(1, 50, 75, 100)

    /**
     * Combat level milestones used for milestone notifications.
     */
    private val COMBAT_MILESTONES = intArrayOf(3, 5, 10, 15, 25, 75, 90, 100, 110, 120, 126, 130, 138)

    /**
     * Total level milestones for milestone notifications.
     */
    private val TOTAL_LEVEL_MILESTONES = intArrayOf(
        50, 75, 100, 150, 200, 300, 400, 500, 600, 700,
        800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600,
        1700, 1800, 1900, 2000, 2100, 2200, 2300
    )

    /**
     * Range of fireworks graphics used for milestone celebration visuals.
     */
    private val GRAPHIC = Graphic.LEVEL_99_GFX_1630..Graphic.LEVEL_99_GFX_1637

    /**
     * Called when a player levels up a skill.
     *
     * @param player The player who leveled up.
     * @param slot The skill id that leveled up.
     * @param amount The number of levels gained.
     */
    @JvmStatic
    fun levelup(player: Player, slot: Int, amount: Int) {
        if (!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) return

        Graphics.send(Graphics(shared.consts.Graphics.FIREWORKS_WHEN_A_LVL_IS_GAINED_199, 100), player.location)
        playJingle(player, getSkillJingle(player, slot))

        handleMilestones(player, slot, amount)

        val skillName = Skills.SKILL_NAME[slot]
        sendString(player, "<col=00008B>Congratulations, you've just advanced a $skillName level!", Components.GAME_INTERFACE_740, 0)
        sendString(player, "Your $skillName level is now ${player.getSkills().getStaticLevel(slot)}.", Components.GAME_INTERFACE_740, 1)
        sendMessage(player, "You've just advanced a $skillName level! You have reached level ${player.getSkills().getStaticLevel(slot)}.")

        if (slot == Skills.PRAYER) {
            player.getSkills().incrementPrayerPoints(1.0)
        }

        if (getStatLevel(player, slot) == 99 && !player.isArtificial) {
            Skillcape.trim(player)
            if (!hasEmote(player, Emotes.SKILLCAPE)) {
                unlockEmote(player, 39)
            }
            sendMessage(player, core.tools.RED + "Well done! You've achieved the highest possible level in this skill!")
            Graphics.send(Graphics(GRAPHIC.random(), 100, 50), player.location)
        }

        if (TOTAL_LEVEL_MILESTONES.contains(player.skills.getTotalLevel())) {
            playAudio(player, Sounds.FIREWORK_2396, 1)
            Graphics.send(Graphics(GRAPHIC.random(), 100, 50), player.location)
            sendMessage(player, DARK_RED + "Well done! You've reached the total level ${player.skills.getTotalLevel()} milestone!")
        }

        if (player.skills.getTotalLevel() == 2376) {
            Graphics.send(Graphics(GRAPHIC.random(), 100, 50), player.location)
            sendMessage(player, DARK_RED + "Congratulations! You've reached the maximum Total level possible!")
        }

        setAttribute(player, "/save:levelup:$slot", true)
        sendFlashingIcons(player, slot)
    }

    /**
     * Handles combat, total level, and skill-level milestones.
     *
     * @param player The player who leveled up.
     * @param slot The skill that was leveled.
     * @param amount The number of levels gained.
     */
    private fun handleMilestones(player: Player, slot: Int, amount: Int) {
        var value = ADVANCE_CONFIGS[slot]

        for (i in COMBAT_MILESTONES.indices) {
            if (player.properties.currentCombatLevel < COMBAT_MILESTONES[i]) {
                if (i > player.getSkills().combatMilestone) {
                    value = value or 0x2
                    player.getSkills().combatMilestone = i
                }
                break
            }

            if (i == 126 && !GameWorld.settings!!.isMembers) {
                Graphics.send(Graphics(GRAPHIC.random(), 100, 50), player.location)
                sendMessage(player, "Congratulations! Your Combat level is now 126 - the highest possible Combat level on free worlds!")
                break
            }

            if (i == 138) {
                Graphics.send(Graphics(GRAPHIC.random(), 100, 50), player.location)
                sendMessage(player, "Congratulations! Your Combat level is now 138! You've achieved the highest Combat level possible!")
                break
            }
        }

        val totalLevel = player.getSkills().getTotalLevel()
        for (i in SKILL_MILESTONES.indices) {
            if (totalLevel < SKILL_MILESTONES[i]) {
                if (i > player.getSkills().skillMilestone) {
                    value = value or 0x4
                    player.getSkills().skillMilestone = i
                }
                break
            }
        }

        value = value or (player.getSkills().combatMilestone shl 23)
        value = value or (player.getSkills().skillMilestone shl 27)
        setVarp(player, Vars.VARP_STATS_TAB_1230, value)
    }

    /**
     * Sends flashing icons in the stats tab and opens the level-up interface.
     *
     * @param player The player.
     * @param slot The skill slot that was leveled.
     */
    @JvmStatic
    fun sendFlashingIcons(player: Player, slot: Int) {
        var value = 0
        for (i in Skills.SKILL_NAME.indices) {
            if (player.getAttribute("levelup:$i", false)) {
                value = value or FLASH_ICONS[i]
            }
        }
        if (slot > -1) {
            value = value or SKILL_ICON[slot]
            openChatbox(player, Components.GAME_INTERFACE_740)
        }
        setVarp(player, Vars.VARP_IFACE_SKILL_FLASH_ICONS_1179, value)
    }
}