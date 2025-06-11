package content.global.skill.construction.decoration.bedroom

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Scenery

class WardrobeSpacePlugin : OptionHandler() {

    private val wardrobeSpaceFurniture = setOf(
        Scenery.SHOE_BOX_13155,
        Scenery.OAK_DRAWERS_13156,
        Scenery.OAK_WARDROBE_13157,
        Scenery.TEAK_DRAWERS_13158,
        Scenery.TEAK_WARDROBE_13159,
        Scenery.MAHOGANY_WARDROBE_13160,
        Scenery.GILDED_WARDROBE_13161
    )

    override fun newInstance(arg: Any?): OptionHandler {
        wardrobeSpaceFurniture.forEach { id ->
            SceneryDefinition.forId(id).handlers["option:change-clothes"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (option != "change-clothes" || node.id !in wardrobeSpaceFurniture) return false

        lock(player, 3)
        GameWorld.Pulser.submit(object : Pulse(1) {
            var counter = 0
            override fun pulse(): Boolean {
                when (counter++) {
                    0 -> animate(player, Animations.OPEN_POH_WARDROBE_535)
                    1 -> {
                        if (node.id == Scenery.SHOE_BOX_13155) {
                            openInterface(player, Components.YRSA_SHOE_STORE_200)
                            sendString(player, node.name, Components.YRSA_SHOE_STORE_200, 13)
                        } else {
                            if (player.appearance.isMale) {
                                sendString(player, node.name, Components.YRSA_SHOE_STORE_200, 13)
                                setComponentVisibility(player, Components.THESSALIA_CLOTHES_MALE_591, 179, true)
                                openInterface(player, Components.THESSALIA_CLOTHES_MALE_591)
                            } else {
                                sendString(player, node.name, Components.THESSALIA_CLOTHES_FEMALE_594, 61)
                                setComponentVisibility(player, Components.THESSALIA_CLOTHES_FEMALE_594, 180, true)
                                openInterface(player, Components.THESSALIA_CLOTHES_FEMALE_594)
                            }
                        }
                        return true
                    }
                }
                return false
            }
        })

        return true
    }
}
