package content.global.skill.construction

import core.api.amountInInventory
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.Log
import shared.consts.Components
import core.api.log
import core.api.sendString
import shared.consts.Items

@Initializable
class BuildInterfacePlugin : ComponentPlugin() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.POH_BUILD_FURNITURE_396, this)
        ComponentDefinition.put(Components.POH_HOUSE_OPTIONS_398, this)
        ComponentDefinition.put(Components.POH_BUILD_SCREEN_402, this)
        return this
    }

    override fun handle(player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        when (component.id) {
            Components.POH_BUILD_FURNITURE_396 -> when (button) {
                132 -> {
                    player.interfaceManager.close()
                    val hotspot: Hotspot? = player.getAttribute("con:hotspot")
                    val obj: Scenery? = player.getAttribute("con:hsobject")

                    if (hotspot?.hotspot != BuildHotspot.FLATPACK) {
                        if (hotspot == null || obj == null) {
                            log(javaClass, Log.ERR, "Failed building decoration $hotspot : $obj")
                            return true
                        }
                    }

                    var slots = (if (slot % 2 != 0) 4 else 0) + (slot shr 1)
                    if (slots >= hotspot!!.hotspot.decorations.size) {
                        log(javaClass, Log.ERR, "Failed building decoration $slots/${hotspot.hotspot.decorations.size}")
                        return true
                    }

                    val debug = player.isStaff
                    var deco = hotspot.hotspot.decorations[slots]

                    if (hotspot.hotspot == BuildHotspot.FLATPACK) {
                        deco = Decoration.forInterfaceItemId(itemId)
                        if (debug || checkRequirements(player, deco)) {
                            BuildingUtils.createFlatpack(player, deco, debug)
                        }
                        return true
                    }

                    if (!debug) {
                        if (player.skills.getLevel(Skills.CONSTRUCTION) < deco.level) {
                            player.sendMessage("You need to have a Construction level of ${deco.level} to build that.")
                            return true
                        }
                        if (!player.inventory.containsItems(*deco.items)) {
                            player.sendMessage("You don't have the right materials.")
                            return true
                        }
                        for (tool in deco.tools) {
                            if (tool == BuildingUtils.WATERING_CAN) {
                                var hasWateringCan = false
                                for (i in 0..7) {
                                    if (player.inventory.contains(tool - i, 1)) {
                                        hasWateringCan = true
                                        break
                                    }
                                }
                                if (!hasWateringCan) {
                                    player.sendMessage("You need a watering can to plant this.")
                                    return true
                                }
                                continue
                            }
                            if (!player.inventory.contains(tool, 1)) {
                                player.sendMessage("You need a ${ItemDefinition.forId(tool).name} to build this.")
                                return true
                            }
                        }
                    }
                    BuildingUtils.buildDecoration(player, hotspot, deco, obj!!)
                    return true
                }
            }

            Components.POH_HOUSE_OPTIONS_398 -> when (button) {
                14 -> {
                    player.houseManager.toggleBuildingMode(player, true)
                    return true
                }
                1 -> {
                    player.houseManager.toggleBuildingMode(player, false)
                    return true
                }
                15 -> {
                    player.houseManager.expelGuests(player)
                    return true
                }
                13 -> {
                    if (!player.houseManager.isInHouse(player)) {
                        player.sendMessage("You can't do this outside of your house.")
                        return true
                    }
                    HouseManager.leave(player)
                    return true
                }
            }

            Components.POH_BUILD_SCREEN_402 -> {
                val coins = amountInInventory(player, Items.COINS_995)
                for ((amount, childId) in COINS_VALUE_TO_CHILD) {
                    if (coins < amount) break
                    sendString(player, core.tools.YELLOW + "$amount coins", Components.POH_BUILD_SCREEN_402, childId)
                }

                val index = button - 160
                log(javaClass, Log.FINE, "BuildRoom Interface Index: $index")
                if (index in RoomProperties.values().indices) {
                    player.dialogueInterpreter.open("con:room", RoomProperties.values()[index])
                    return true
                }
            }
        }
        return false
    }

    private fun checkRequirements(player: Player, deco: Decoration): Boolean {
        if (player.skills.getLevel(Skills.CONSTRUCTION) < deco.level) {
            player.sendMessage("You need to have a Construction level of ${deco.level} to build that.")
            return false
        }
        if (!player.inventory.containsItems(*deco.items)) {
            player.sendMessage("You don't have the right materials.")
            return false
        }
        for (tool in deco.tools) {
            if (tool == BuildingUtils.WATERING_CAN) {
                var hasWateringCan = false
                for (i in 0..7) {
                    if (player.inventory.contains(tool - i, 1)) {
                        hasWateringCan = true
                        break
                    }
                }
                if (!hasWateringCan) {
                    player.sendMessage("You need a watering can to plant this.")
                    return false
                }
                continue
            }
            if (!player.inventory.contains(tool, 1)) {
                player.sendMessage("You need a ${ItemDefinition.forId(tool).name} to build this.")
                return false
            }
        }
        return true
    }

    companion object {
        private val COINS_VALUE_TO_CHILD = arrayOf(
            100 to 138, 5000 to 139, 7500 to 147, 7500 to 155, 7500 to 156, 7500 to 157,
            10000 to 140, 15000 to 141, 25000 to 142, 50000 to 143, 50000 to 149, 50000 to 150,
            75000 to 145, 75000 to 152, 100000 to 144, 100000 to 151, 150000 to 146, 150000 to 153,
            150000 to 154, 250000 to 148, 250000 to 159
        )
    }
}
