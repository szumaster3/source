package content.global.skill.construction.decoration.bedroom

import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.IntType
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import org.rs.consts.Scenery
import java.text.SimpleDateFormat
import java.util.*

class ClockSpacePlugin : OptionHandler() {

    private val clockSpaceFurniture = setOf(Scenery.CLOCK_13169, Scenery.CLOCK_13170, Scenery.CLOCK_13171)

    override fun newInstance(arg: Any?): OptionHandler {
        clockSpaceFurniture.forEach { id ->
            SceneryDefinition.forId(id).handlers["option:read"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (option != "read" || node.id !in clockSpaceFurniture) return false

        val format = SimpleDateFormat("mm")
        val minuteDisplay = format.format(Calendar.getInstance().time).toInt()
        val sb = StringBuilder("It's ")

        when (minuteDisplay) {
            0 -> sb.append("Rune o'clock.")
            15 -> sb.append("a quarter past Rune.")
            in 1..29 -> sb.append("$minuteDisplay past Rune.")
            45 -> sb.append("a quarter till Rune.")
            else -> sb.append("${60 - minuteDisplay} till Rune.")
        }

        sendMessage(player, sb.toString())
        return true
    }
}
