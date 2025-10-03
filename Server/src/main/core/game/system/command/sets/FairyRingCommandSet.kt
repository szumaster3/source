package core.game.system.command.sets

import core.api.closeInterface
import core.api.removeAttribute
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.system.command.CommandPlugin
import core.game.system.command.CommandSet
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class FairyRingCommandSet : CommandPlugin() {

    val RING_CODES =
        mapOf(
            "AIQ" to arrayOf(Location.create(2996, 3114, 0), "Asgarnia: Mudskipper Point", 15),
            "AIR" to arrayOf(Location.create(2700, 3247, 0), "Islands: South of Witchaven", 16),
            "AJQ" to arrayOf(Location.create(2735, 5221, 0), "Dungeons: Dark cave south of Dorgesh-Kaann", 19),
            "ALR" to arrayOf(Location.create(3059, 4875, 0), "Other realms: Abyssal Area", 28),
            "AJR" to arrayOf(Location.create(2780, 3613, 0), "Kandarin: Slayer cave south-east of Rellekka", 20),
            "AJS" to arrayOf(Location.create(2500, 3896, 0), "Islands: Penguins near Miscellania", 21),
            "AKQ" to arrayOf(Location.create(2319, 3619, 0), "Kandarin: Piscatoris Hunter area", 23),
            "AKS" to arrayOf(Location.create(2571, 2956, 0), "Feldip Hills: Jungle Hunter area", 25),
            "ALQ" to arrayOf(Location.create(3597, 3495, 0), "Morytania: Haunted Woods east of Canifis", 27),
            "ALS" to arrayOf(Location.create(2644, 3495, 0), "Kandarin: McGrubor's Wood", 29),
            "BIP" to arrayOf(Location.create(3410, 3324, 0), "Islands: River Salve", 30),
            "BIQ" to arrayOf(Location.create(3251, 3095, 0), "Kharidian Desert: Near Kalphite hive", 31),
            "BIS" to arrayOf(Location.create(2635, 3266, 0), "Kandarin: Ardougne Zoo unicorns", 33),
            // "BJR" to arrayOf(null, "Other Realms: Realm of the Fisher King", 36),
            "BKP" to arrayOf(Location.create(2385, 3035, 0), "Feldip Hills: South of Castle Wars", 38),
            "BKQ" to arrayOf(Location.create(3041, 4532, 0), "Other realms: Enchanted Valley", 39),
            "BKR" to arrayOf(Location.create(3469, 3431, 0), "Morytania: Mort Myre, south of Canifis", 40),
            "BLR" to arrayOf(Location.create(2740, 3351, 0), "Kandarin: Legends' Guild", 44),
            "CIP" to arrayOf(Location.create(2513, 3884, 0), "Islands: Miscellania", 46),
            "CIQ" to arrayOf(Location.create(2528, 3127, 0), "Kandarin: North-west of Yanille", 47),
            "CJR" to arrayOf(Location.create(2705, 3576, 0), "Kandarin: Sinclair Mansion", 52),
            "CKP" to arrayOf(Location.create(2075, 4848, 0), "Other realms: Cosmic Entity's plane", 54),
            "CKR" to arrayOf(Location.create(2801, 3003, 0), "Karamja: South of Tai Bwo Wannai Village", 56),
            "CKS" to arrayOf(Location.create(3447, 3470, 0), "Morytania: Canifis", 57),
            "CLP" to arrayOf(Location.create(3082, 3206, 0), "Islands: South of Draynor Village", 58),
            "CLS" to arrayOf(Location.create(2682, 3081, 0), "Islands: Jungle spiders near Yanille", 61),
            "DIR" to arrayOf(Location.create(3038, 5348, 0), "Other realms: Goraks' Plane", 64),
            "DIS" to arrayOf(Location.create(3108, 3149, 0), "Misthalin: Wizards' Tower", 65),
            "DJP" to arrayOf(Location.create(2658, 3230, 0), "Kandarin: Tower of Life", 66),
            "DJR" to arrayOf(Location.create(2676, 3587, 0), "Kandarin: Sinclair Mansion (west)", 68),
            "DKP" to arrayOf(Location.create(2900, 3111, 0), "Karamja: South of Musa Point", 70),
            "DKR" to arrayOf(Location.create(3129, 3496, 0), "Misthalin: Edgeville", 72),
            "DKS" to arrayOf(Location.create(2744, 3719, 0), "Kandarin: Snowy Hunter area", 73),
            "DLQ" to arrayOf(Location.create(3423, 3016, 0), "Kharidian Desert: North of Nardah", 75),
            "DLR" to arrayOf(Location.create(2213, 3099, 0), "Islands: Poison Waste south of Isafdar", 76),
            "DIQ" to arrayOf(null, "Player owned home if it exists", 69),
        )

    override fun parse(player: Player?, name: String?, arguments: Array<String?>?): Boolean {
        when (name) {
            "f" -> {
                if (arguments!!.size < 2) {
                    sendMessage(player!!, "${core.tools.DARK_PURPLE}Syntax: ::f [Teleport code]")
                    return true
                }
                val ringCode = arguments[1]!!.uppercase()
                if (RING_CODES.containsKey(ringCode)) {
                    if (ringCode == "DIQ") {
                        sendMessage(
                            player!!,
                            "${core.tools.DARK_PURPLE}Sorry, Player home rings are not yet implemented in fairycli.",
                        )
                        return true
                    } else {
                        sendMessage(player!!, "Teleporting to: $ringCode")
                        removeAttribute(player, "fairy-delay")
                        removeAttribute(player, "fairy_location_combo")
                        closeInterface(player)
                        player.teleporter.send(
                            (
                                RING_CODES[ringCode]
                                    ?: error("RING CODES IMPROPERLY SET. Please fix fairycli.kt")
                            )[0] as Location?,
                            TeleportManager.TeleportType.FAIRY_RING,
                        )
                    }
                } else {
                    sendMessage(
                        player!!,
                        "${core.tools.DARK_PURPLE}Invalid teleport code: $ringCode. It may be unimplemented",
                    )
                }
            }
        }
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any?> {
        link(CommandSet.ADMINISTRATOR)
        return this
    }
}
