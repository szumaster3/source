package content.global.plugin.iface

import core.api.*
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Components

private const val HAIRDRESSER_MALE_COMPONENT_ID = Components.HAIRDRESSER_MALE_596
private const val HAIRDRESSER_FEMALE_COMPONENT_ID = Components.HAIRDRESSER_FEMALE_592

private val HAIR_COLORS =
    intArrayOf(20, 19, 10, 18, 4, 5, 15, 7, 0, 6, 21, 9, 22, 17, 8, 16, 11, 24, 23, 3, 2, 1, 14, 13, 12)
private val maleColorButtonRange = (229..253)
private val femaleColorButtonRange = (73..97)

private const val F_BALD = 45
private const val F_BUN = 46
private const val F_DREAD = 47
private const val F_LONG = 48
private const val F_SHORT = 49
private const val F_PIGTAILS = 50
private const val F_CREW = 51
private const val F_CLOSE_CROP = 52
private const val F_WILD_SPIKE = 53
private const val F_SPIKE = 54

private const val F_SIDE_PONY = 135
private const val F_CURLS = 136
private const val F_WIND_BRAIDS = 137
private const val F_PONYTAIL = 138
private const val F_RING_BRAIDS = 139
private const val F_FOUR_PONIES = 140
private const val F_FLIP = 141
private const val F_LAYERED_FLIP = 142
private const val F_STRAIGHT = 143
private const val F_LONG_BRAIDS = 144
private const val F_CURTAINS = 145

private const val F_EARMUFFS = 242
private const val F_FRINGE = 269
private const val F_PINNED_UP = 270
private const val F_LAYERED = 271
private const val F_TOP_KNOT = 272
private const val F_PINNED_BUN = 273
private const val F_PONYTAIL_SPIKED = 274
private const val F_PIXIE = 275
private const val F_ODANGO = 276
private const val F_BUN_WITH_FRINGE = 277
private const val F_PIGTAILS_WITH_FRINGE = 278
private const val F_FRENCH_TWIST = 279
private const val F_PRINCESS = 280

val FEMALE_HAIR_STYLES =
    intArrayOf(F_BALD, F_BUN, F_DREAD, F_LONG, F_SHORT, F_PIGTAILS, F_CREW, F_CLOSE_CROP, F_WILD_SPIKE, F_SPIKE, F_SIDE_PONY, F_CURLS, F_WIND_BRAIDS, F_PONYTAIL, F_RING_BRAIDS, F_FOUR_PONIES, F_FLIP, F_LAYERED_FLIP, F_STRAIGHT, F_LONG_BRAIDS, F_CURTAINS, F_EARMUFFS, F_FRINGE, F_PINNED_UP, F_LAYERED, F_TOP_KNOT, F_PINNED_BUN, F_PONYTAIL_SPIKED, F_PIXIE, F_ODANGO, F_BUN_WITH_FRINGE, F_PIGTAILS_WITH_FRINGE, F_FRENCH_TWIST, F_PRINCESS)

private const val M_BALD = 0
private const val M_DREAD = 1
private const val M_LONG = 2
private const val M_MEDIUM = 3
private const val M_MONK = 4
private const val M_SHORT = 5
private const val M_CLOSE_CROP = 6
private const val M_WILD_SPIKE = 7
private const val M_SPIKE = 8
private const val M_MOHAWK = 91
private const val M_WIND_BRAID = 92
private const val M_QUIFF = 93
private const val M_SAMURAI = 94
private const val M_PRINCE = 95
private const val M_CURTAINS = 96
private const val M_LONG_CURTAINS = 97
private const val M_SIDE_PART_SPIKE = 246
private const val M_TOP_KNOT = 262
private const val M_PONYTAIL_SPIKEY = 251
private const val M_LONG_SWEPT_FRINGE = 265
private const val M_EVANSTYLE = 252
private const val M_DRAGON = 257
private const val M_WARRIOR_CURTAINS = 247
private const val M_COMB_OVER = 253

val MALE_HAIR_STYLES =
    intArrayOf(M_BALD, M_DREAD, M_LONG, M_MEDIUM, M_MONK, M_SHORT, M_CLOSE_CROP, M_WILD_SPIKE, M_SPIKE, M_MOHAWK, M_WIND_BRAID, M_QUIFF, M_SAMURAI, M_PRINCE, M_CURTAINS, M_LONG_CURTAINS, M_SIDE_PART_SPIKE, M_TOP_KNOT, M_PONYTAIL_SPIKEY, M_LONG_SWEPT_FRINGE, M_EVANSTYLE, M_DRAGON, M_WARRIOR_CURTAINS, M_COMB_OVER)

private const val MF_GOATEE = 10
private const val MF_LONG_BEARD = 11
private const val MF_MEDIUM_BEARD = 12
private const val MF_MOUSTACHE = 13
private const val MF_CLEAN_SHAVEN = 14
private const val MF_SHORT_BEARD = 15
private const val MF_SHORT_FULL = 16
private const val MF_SPLIT_BEARD = 17
private const val MF_HANDLEBAR = 98
private const val MF_MUTTON = 99
private const val MF_FULL_MUTTON = 100
private const val MF_FULL_MOUSTACHE = 101
private const val MF_WAXED = 102
private const val MF_DALI = 103
private const val MF_VIZIER = 104
private const val MF_HALF_GOATEE = 305
private const val MF_SIDEBURNS = 306
private const val MF_IMPERIAL = 307
private const val MF_SENSEI = 308

val MALE_FACIAL_STYLES =
    intArrayOf(MF_GOATEE, MF_LONG_BEARD, MF_MEDIUM_BEARD, MF_MOUSTACHE, MF_CLEAN_SHAVEN, MF_SHORT_BEARD, MF_SHORT_FULL, MF_SPLIT_BEARD, MF_HANDLEBAR, MF_MUTTON, MF_FULL_MUTTON, MF_FULL_MOUSTACHE, MF_WAXED, MF_DALI, MF_VIZIER, MF_HALF_GOATEE, MF_SIDEBURNS, MF_IMPERIAL, MF_SENSEI)

private val maleStyleButtonRange = (65..90)
private val femaleStyleButtonRange = (148..181)

private val COINS = Item(995, 2000)

/**
 * Handles the reworked hairdresser interface with full functionality.
 * @author Ceikry
 */
@Initializable
class HairdresserInterface : ComponentPlugin() {
    override fun open(
        player: Player?,
        component: Component?,
    ) {
        player ?: return
        super.open(player, component)
        setAttribute(player, "original-hair", player.appearance.hair.look)
        setAttribute(player, "original-beard", player.appearance.beard.look)
        setAttribute(player, "original-color", player.appearance.hair.color)
        val usedInterface = if (player.isMale) HAIRDRESSER_MALE_COMPONENT_ID else HAIRDRESSER_FEMALE_COMPONENT_ID

        val player_model_child =
            when (usedInterface) {
                HAIRDRESSER_FEMALE_COMPONENT_ID -> 17
                HAIRDRESSER_MALE_COMPONENT_ID -> 62
                else -> 0
            }
        val player_head_child =
            when (usedInterface) {
                HAIRDRESSER_FEMALE_COMPONENT_ID -> 146
                HAIRDRESSER_MALE_COMPONENT_ID -> 61
                else -> 0
            }
        sendPlayerOnInterface(player, usedInterface, player_model_child)
        sendPlayerOnInterface(player, usedInterface, player_head_child)
        sendAnimationOnInterface(
            player,
            core.game.dialogue.FaceAnim.HAPPY.animationId,
            usedInterface,
            player_head_child,
        )
        player.toggleWardrobe(true)

        component?.setUncloseEvent { pl, _ ->
            player.toggleWardrobe(false)
            setAttribute(player, "beard-setting", false)
            if (!pl.getAttribute("hairdresser-paid", false)) {
                val originalHair = player.getAttribute("original-hair", 0)
                val originalBeard = player.getAttribute("original-beard", -1)
                val originalColor = player.getAttribute("original-color", 0)
                pl.appearance.hair.changeLook(originalHair)
                pl.appearance.hair.changeColor(originalColor)
                if (originalBeard != -1) {
                    pl.appearance.beard.changeLook(originalBeard)
                }
                refreshAppearance(pl)
            }
            pl.removeAttribute("hairdresser-paid")
            true
        }
        refreshAppearance(player)
    }

    override fun handle(
        player: Player?,
        component: Component?,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int,
    ): Boolean {
        player ?: return false
        val isBeard = player.getAttribute("beard-setting", false)

        when (button) {
            199 -> setAttribute(player, "beard-setting", false)
            200 -> setAttribute(player, "beard-setting", true)
            196, 274 -> pay(player)
            else ->
                when (component?.id) {
                    592 -> {
                        if (femaleColorButtonRange.contains(button)) {
                            updateColor(player, button)
                        }
                        if (femaleStyleButtonRange.contains(button)) {
                            updateHair(player, button)
                        }
                    }

                    596 -> {
                        if (isBeard && !maleColorButtonRange.contains(button)) {
                            updateBeard(player, button)
                            return true
                        }
                        if (maleColorButtonRange.contains(button)) {
                            updateColor(player, button)
                            return true
                        }
                        if (maleStyleButtonRange.contains(button)) {
                            updateHair(player, button)
                        }
                    }
                }
        }
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(HAIRDRESSER_MALE_COMPONENT_ID, this)
        ComponentDefinition.put(HAIRDRESSER_FEMALE_COMPONENT_ID, this)
        return this
    }

    private fun pay(player: Player) {
        if (!player.houseManager.isInHouse(player)) {
            if (!removeItem(player, COINS)) {
                sendDialogue(player, "You can not afford that.")
            } else {
                setAttribute(player, "hairdresser-paid", true)
                closeInterface(player)
            }
        } else {
            setAttribute(player, "hairdresser-paid", true)
            closeInterface(player)
        }
    }

    private fun updateBeard(
        player: Player,
        button: Int,
    ) {
        var subtractor = 105
        when (button) {
            123 -> subtractor += 2
            126 -> subtractor += 4
            129 -> subtractor += 6
        }
        player.appearance.beard.changeLook(MALE_FACIAL_STYLES[button - subtractor])
        refreshAppearance(player)
    }

    private fun updateHair(
        player: Player,
        button: Int,
    ) {
        val usedInterface = if (player.isMale) HAIRDRESSER_MALE_COMPONENT_ID else HAIRDRESSER_FEMALE_COMPONENT_ID

        var subtractor =
            when (usedInterface) {
                HAIRDRESSER_MALE_COMPONENT_ID -> 65
                HAIRDRESSER_FEMALE_COMPONENT_ID -> 148
                else -> 0
            }

        val hairArray =
            when (usedInterface) {
                HAIRDRESSER_MALE_COMPONENT_ID -> MALE_HAIR_STYLES
                HAIRDRESSER_FEMALE_COMPONENT_ID -> FEMALE_HAIR_STYLES
                else -> intArrayOf(0)
            }

        when (button) {
            89, 90 -> subtractor += 2
        }

        player.appearance.hair.changeLook(hairArray[button - subtractor])
        refreshAppearance(player)
    }

    private fun updateColor(
        player: Player,
        button: Int,
    ) {
        val usedInterface = if (player.isMale) HAIRDRESSER_MALE_COMPONENT_ID else HAIRDRESSER_FEMALE_COMPONENT_ID

        var subtractor =
            when (usedInterface) {
                HAIRDRESSER_MALE_COMPONENT_ID -> 229
                HAIRDRESSER_FEMALE_COMPONENT_ID -> 73
                else -> 0
            }
        player.appearance.hair.changeColor(HAIR_COLORS[button - subtractor])
        refreshAppearance(player)
    }
}
