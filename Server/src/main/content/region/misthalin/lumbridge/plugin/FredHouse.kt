package content.region.misthalin.lumbridge.plugin

import core.game.node.entity.Entity
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class FredHouse :
    MapZone("freds-farm-house", true),
    Plugin<Any?> {
    override fun configure() {
        register(ZoneBorders(3188, 3275, 3192, 3270))
    }

    override fun enter(entity: Entity): Boolean {
        return super.enter(entity)
    }

    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? = null
}
