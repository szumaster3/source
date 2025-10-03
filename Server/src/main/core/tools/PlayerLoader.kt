package core.tools

import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.player.info.login.PlayerParser

/**
 * Represents a class that is used to load a player, or details of it.
 * @author Vexia
 */
object PlayerLoader {
    @JvmStatic
    fun getPlayerFile(name: String?): Player {
        val playerDetails = PlayerDetails(name)
        // playerDetails.parse();
        val player = Player(playerDetails)
        PlayerParser.parse(player)
        // GameWorld.getWorld().getAccountService().loadPlayer(player);
        return player
    }

    @JvmStatic
    fun getPlayerDetailFile(name: String?): PlayerDetails {
        val playerDetails = PlayerDetails(name)
        // playerDetails.parse();
        return playerDetails
    }
}
