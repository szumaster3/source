package content.data

import core.ServerConstants
import core.api.setAttribute
import core.game.node.entity.player.Player
import core.game.world.map.Location

/**
 * Enum representing different respawn points.
 */
enum class RespawnPoint(
    val location: Location,
) {
    LUMBRIDGE(ServerConstants.HOME_LOCATION!!.location),
    FALADOR(Location(2972, 3337, 0)),
    CAMELOT(Location(2757, 3477, 0)),
}

/**
 * Function to set the respawn location for a player.
 *
 * @param respawnPoint The [RespawnPoint] representing the desired respawn location.
 */
fun Player.setRespawnLocation(respawnPoint: RespawnPoint) {
    val newLocation =
        when (respawnPoint) {
            RespawnPoint.FALADOR -> RespawnPoint.FALADOR.location
            RespawnPoint.CAMELOT -> RespawnPoint.CAMELOT.location
            else -> RespawnPoint.LUMBRIDGE.location
        }

    setAttribute(this, "/save:spawnLocation", newLocation)
    this.properties.spawnLocation = newLocation
}

/**
 * Get the respawn location for a player.
 */
fun Player.getRespawnLocation(): Location {
    return this.getAttribute("/save:spawnLocation") as? Location
        ?: RespawnPoint.LUMBRIDGE.location
}

/**
 * Example.
 *
 * Possible to test in-game via command: `::respawn <point>`
 */
fun testRespawnPoint(player: Player) {
    player.setRespawnLocation(RespawnPoint.FALADOR)
    player.setRespawnLocation(RespawnPoint.CAMELOT)
    player.setRespawnLocation(RespawnPoint.LUMBRIDGE)
}
