package content.region.morytania.phas.quest.ahoy.plugin

import core.api.*
import core.game.component.Component
import core.game.event.EventHook
import core.game.event.TickEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.Entity
import core.game.world.map.zone.ZoneBorders
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.Components
import shared.consts.Scenery

@Initializable
class WindSpeedInterface : InterfaceListener, InteractionListener, EventHook<TickEvent>, MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> =
        arrayOf(ZoneBorders(3616, 3545, 3622, 3539, 2))

    override fun areaEnter(entity: Entity) {
        val player = entity.asPlayer()
        openInterface(player, Components.AHOY_WINDSPEED_10)
        setAttribute(player, GhostsAhoyUtils.windSpeed, 0)
        player.hook(Event.Tick, this)
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        val player = entity.asPlayer()
        player.unhook(this)
        player.interfaceManager.close(Component(Components.AHOY_WINDSPEED_10))
        removeAttribute(player, GhostsAhoyUtils.windSpeed)
    }

    override fun defineInterfaceListeners() {
        on(Components.AHOY_WINDSPEED_10) { _, _, _, _, _, _ -> true }
    }

    override fun defineListeners() {
        on(Scenery.MAST_5274, IntType.SCENERY, "search") { player, _ ->
            when (getAttribute(player, GhostsAhoyUtils.windSpeed, 0)) {
                0 -> sendDialogue(player, "You can see a tattered flag blowing in the wind. The wind is blowing too hard to make out any details.")
                1 -> sendDialogue(player, "You can see a tattered flag blowing in the wind. The top half of the flag is coloured red.")
                2 -> sendDialogue(player, "You can see a tattered flag blowing in the wind. The bottom half of the flag is coloured blue.")
                3 -> sendDialogue(player, "You can see a tattered flag blowing in the wind. The skull emblem is coloured blue.")
            }
            return@on true
        }
    }

    override fun process(entity: Entity, event: TickEvent) {
        val player = entity.asPlayer()
        val wind = RandomFunction.random(0, 3)
        setAttribute(player, GhostsAhoyUtils.windSpeed, wind)
        val windText = when (wind) {
            0 -> "High"
            1 -> "Low"
            2 -> "Medium"
            3 -> "Very High"
            else -> "High"
        }
        sendString(player, windText, Components.AHOY_WINDSPEED_10, 2)
    }
}
