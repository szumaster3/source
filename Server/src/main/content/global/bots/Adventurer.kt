package content.global.bots

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import core.ServerConstants
import core.game.bots.AIRepository
import core.game.bots.CombatBotAssembler
import core.game.bots.Script
import core.game.interaction.DestinationFlag
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.interaction.MovementPulse
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.ZoneBorders
import java.io.File
import java.io.FileReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class Adventurer(
    val style: CombatStyle,
) : Script() {
    var city: Location = lumbridge
    var poiloc: Location = karamja
    var geSocialLoc: Location = getRandomGESocialLocation()
    var geClerkLoc: Location = getRandomGELocation()
    var geClerksloc: Location = neGEClerk

    var freshspawn: Boolean = true
    var sold: Boolean = false
    var poi: Boolean = false

    val chance: Int = if (cityLocationsGE.contains(city)) 3500 else 3000
    var ticks: Int = 0
    var counter: Int = 0
    val waitTotal: Int = 8
    var returnToAdventure: Int = 0
    var geWait: Int = 0
    var geLongWait: Int = 0

    val type =
        when (style) {
            CombatStyle.MELEE -> CombatBotAssembler.Type.MELEE
            CombatStyle.MAGIC -> CombatBotAssembler.Type.MAGE
            CombatStyle.RANGE -> CombatBotAssembler.Type.RANGE
        }

    init {
        skills[Skills.AGILITY] = 99
        inventory.add(Item(1359))
        skills[Skills.WOODCUTTING] = 95
        inventory.add(Item(590))
        skills[Skills.FISHING] = 90
        inventory.add(Item(1271))
        skills[Skills.MINING] = 90
        skills[Skills.SLAYER] = 90
    }

    override fun toString(): String =
        "${bot.username} is an Adventurer bot " +
                "at ${bot.location}! " +
                "State: $state - " +
                "City: $city - " +
                "Ticks: $ticks - " +
                "Freshspawn: $freshspawn - " +
                "Sold: $sold - " +
                "Counter: $counter"

    var state = State.START

    private fun getRandomCity(): Location = cities.random()

    private fun getRandomPoi(): Location = pois.random()

    private fun getRandomGESocialLocation(): Location = socialLocationsGE.random()

    private fun getRandomGELocation(): Location = cityLocationsGE.random()

    private fun randomNumberFromOne(maxInt: Int): Int = Random.nextInt(0, maxInt)

    private fun otherPlayersNearby(): Boolean {
        val localPlayers = RegionManager.getLocalPlayers(bot)
        val otherPlayers = localPlayers.filter { it.name != bot.name }
        return otherPlayers.isNotEmpty()
    }

    private fun checkNearBank() {
        if (bankMap[city] == null) {
            scriptAPI.teleport(getRandomCity().also { city = it })
        } else {
            if (bankMap[city]?.insideBorder(bot) == true) {
                state = State.FIND_BANK
            } else {
                bankMap[city]?.let { scriptAPI.walkTo(it.randomLoc) }
            }
        }
    }

    private fun checkCounter(maxCounter: Int) {
        if (counter++ >= maxCounter) {
            state = State.TELEPORTING
        }
    }

    private fun teleportToRandomCity() {
        city = getRandomCity()
        when (city) {
            neGEClerk -> {
                scriptAPI.teleport(scriptAPI.randomizeLocationInRanges(city, -3, 2, 0, 1, 0))
            }

            swGEClerk -> {
                scriptAPI.teleport(scriptAPI.randomizeLocationInRanges(city, -2, 3, -1, 0, 0))
            }

            nwGEBanker -> {
                scriptAPI.teleport(scriptAPI.randomizeLocationInRanges(city, -2, 0, -3, 2, 0))
            }

            seGEBanker -> {
                scriptAPI.teleport(scriptAPI.randomizeLocationInRanges(city, 0, 2, -2, 3, 0))
            }

            else -> {
                scriptAPI.teleport(scriptAPI.randomizeLocationInRanges(city, -1, 1, -1, 1, 0))
            }
        }
    }

    val resources =
        listOf(
            "Rocks",
            "Tree",
            "Oak",
            "Willow",
            "Maple tree",
            "Yew",
            "Magic tree",
            "Teak",
            "Mahogany",
        )

    fun immerse() {
        if (counter++ >= Random.nextInt(150, 300)) {
            state = State.TELEPORTING
        }
        val items = AIRepository.groundItems[bot]
        if (Random.nextBoolean()) {
            if (items.isNullOrEmpty()) {
                scriptAPI.attackNpcsInRadius(bot, 8)
                state = State.LOOT_DELAY
            }
            if (bot.inventory.isFull) {
                checkNearBank()
            }
        } else {
            if (bot.inventory.isFull) {
                checkNearBank()
            } else {
                val resource = scriptAPI.getNearestNodeFromList(resources, true)
                if (resource != null) {
                    if (resource.name.contains("ocks")) {
                        InteractionListeners.run(
                            resource.id,
                            IntType.SCENERY,
                            "mine",
                            bot,
                            resource,
                        )
                    } else {
                        InteractionListeners.run(resource.id, IntType.SCENERY, "chop down", bot, resource)
                    }
                }
            }
        }
        return
    }

    fun refresh() {
        scriptAPI.teleport(lumbridge)
        state = State.START
    }

    override fun tick() {
        ticks++
        if (ticks >= 1000) {
            ticks = 0
            refresh()
            return
        }

        if (ticks % 30 == 0) {
            for ((zone, resolution) in common_stuck_locations) {
                if (zone.insideBorder(bot)) {
                    resolution(this)
                    return
                }
            }
        }

        when (state) {
            State.LOOT_DELAY -> {
                bot.pulseManager.run(
                    object : Pulse() {
                        var counter1 = 0

                        override fun pulse(): Boolean {
                            when (counter1++) {
                                7 -> return true.also { state = State.LOOT }
                            }
                            return false
                        }
                    },
                )
            }

            State.LOOT -> {
                val items = AIRepository.groundItems[bot]
                if (items?.isNotEmpty() == true && !bot.inventory.isFull) {
                    items.toTypedArray().forEach {
                        scriptAPI.takeNearestGroundItem(it.id)
                    }
                    return
                } else {
                    state = State.ADVENTURE
                }
            }

            State.START -> {
                if (freshspawn) {
                    freshspawn = false
                    scriptAPI.randomWalkTo(lumbridge, randomNumberFromOne(25))
                } else {
                    state = State.TELEPORTING
                }
            }

            State.TELEPORTING -> {
                if (freshspawn) {
                    freshspawn = false
                }
                teleportToRandomCity()
                poi = false
                sold = false
                ticks = 0
                counter = 0
                state = State.ADVENTURE
                return
            }

            State.ADVENTURE -> {
                checkCounter(800)
                if (randomNumberFromOne(chance) <= 10) {
                    if (otherPlayersNearby()) {
                        ticks = 0
                        dialogue()
                    }
                }

                if (!poi && randomNumberFromOne(1000) <= 75) {
                    val roamDistance = if (!cityLocationsGE.contains(city)) 225 else randomNumberFromOne(5)
                    if (cityLocationsGE.contains(city) && randomNumberFromOne(100) < 90) {
                        if (!bot.bank.isEmpty) {
                            state = State.FIND_GE
                        }
                        return
                    }
                    scriptAPI.randomWalkTo(city, roamDistance)
                    return
                }

                if (poi && randomNumberFromOne(1000) <= 100) {
                    immerse()
                    return
                }

                if (poi && randomNumberFromOne(1000) <= 25) {
                    dialogue()
                }

                if (poi && randomNumberFromOne(1000) <= 50) {
                    val roamDistancePoi =
                        when (poiloc) {
                            gemrocks, chaosnpc, chaosnpc2 -> 1
                            magics, coalTrucks -> 7
                            miningguild, teakfarm, crawlinghands -> 5
                            varLumberYard -> 20
                            keldagrimout, teak1 -> 30
                            eaglespeek, isafdar -> 40
                            treegnome -> 50
                            else -> 60
                        }
                    scriptAPI.randomWalkTo(poiloc, roamDistancePoi)
                    return
                }

                if (randomNumberFromOne(1000) <= 75) {
                    if (!cityLocationsGE.contains(city)) {
                        ticks = 0
                        immerse()
                        return
                    } else if (randomNumberFromOne(chance) <= 55 && otherPlayersNearby()) {
                        ticks = 0
                        dialogue()
                    }
                }

                if (cityLocationsGE.contains(city) && randomNumberFromOne(1000) <= 50) {
                    state = State.IDLE_GE
                }

                if (!poi && randomNumberFromOne(1000) <= 5) {
                    poiloc = getRandomPoi()
                    city = teak1
                    poi = true
                    scriptAPI.teleport(poiloc)
                    return
                }

                if (cityLocationsGE.contains(city) && randomNumberFromOne(1000) <= 100) {
                    state = State.TELEPORTING
                    return
                }

                if (cityLocationsGE.contains(city)) {
                    return
                }

                if (poi && randomNumberFromOne(1000) <= 20) {
                    state = State.TELEPORTING
                    return
                }

                if (counter++ >= 750 && randomNumberFromOne(100) <= 50) {
                    city = getRandomCity()
                    if (randomNumberFromOne(100) % 2 == 0) {
                        state = State.TELEPORTING
                    } else {
                        if (citygroupA.contains(city)) {
                            city = citygroupA.random()
                        } else {
                            city = citygroupB.random()
                        }
                        counter = 0
                        ticks = 0
                        state = State.FIND_CITY
                    }
                    counter = 0
                    return
                }
                return
            }

            State.IDLE_GE -> {
                returnToAdventure = Random.nextInt(350, 750)
                if (counter++ >= returnToAdventure) {
                    if (randomNumberFromOne(100) <= 25) {
                        ticks = 0
                        counter = 0
                        poiloc = getRandomPoi()
                        city = teak1
                        poi = true
                        scriptAPI.teleport(poiloc)
                        state = State.ADVENTURE
                        return
                    } else {
                        counter = 0
                        ticks = 0
                        state = State.TELEPORTING
                        return
                    }
                }
                if (cityLocationsGE.contains(city)) {
                    if (randomNumberFromOne(1000) <= 5) {
                        ticks = 0
                        geSocialLoc = scriptAPI.randomizeLocationInRanges(getRandomGESocialLocation(), -1, 1, -1, 1, 0)
                    } else if (randomNumberFromOne(1000) <= 10) {
                        ticks = 0
                        scriptAPI.randomWalkTo(geSocialLoc, randomNumberFromOne(5))
                        return
                    }
                    if (randomNumberFromOne(1000) <= 5 && otherPlayersNearby()) {
                        ticks = 0
                        dialogue()
                    } else if (randomNumberFromOne(1000) <= 250) {
                        return
                    }
                }
                return
            }

            State.FIND_GE -> {
                sold = false
                val ge: Scenery? = scriptAPI.getNearestNode("Desk", true) as Scenery?
                if (ge == null || bot.bank.isEmpty) state = State.ADVENTURE

                class GEPulse : MovementPulse(bot, ge, DestinationFlag.OBJECT) {
                    override fun pulse(): Boolean {
                        bot.faceLocation(ge?.location)
                        return true.also { state = State.GE }
                    }
                }
                if (ge == null || bot.bank.isEmpty) state = State.ADVENTURE
                if (ge != null && !bot.bank.isEmpty) {
                    if (randomNumberFromOne(1000) <= 25 && otherPlayersNearby()) {
                        dialogue()
                        scriptAPI.randomWalkTo(geSocialLoc, randomNumberFromOne(5))
                    } else if (randomNumberFromOne(500) <= 50) {
                        GameWorld.Pulser.submit(GEPulse())
                    }
                }
                checkCounter(500)
                return
            }

            State.GE -> {
                geClerksloc = clerkLocationsGe.random()
                geWait = Random.nextInt(35, 100)
                geLongWait = Random.nextInt(350, 750)
                if (!sold) {
                    if (randomNumberFromOne(500) <= 25) {
                        scriptAPI.randomWalkTo(geClerksloc, randomNumberFromOne(4))
                    }
                    if (counter++ >= geWait) {
                        scriptAPI.randomWalkTo(geClerksloc, randomNumberFromOne(1))
                        sold = true
                        counter = 0
                        ticks = 0
                        scriptAPI.sellAllOnGeAdv()
                        state = State.TELEPORTING
                        return
                    }
                } else if (counter++ >= geLongWait) {
                    state = State.TELEPORTING
                    return
                }
                checkCounter(1000)
                return
            }

            State.FIND_BANK -> {
                val bank: Scenery? = scriptAPI.getNearestNode("Bank booth", true) as Scenery?
                if (bank == null) {
                    state = State.TELEPORTING
                }
                if (bank != null && randomNumberFromOne(100) <= 5) {
                    scriptAPI.depositAtBank()
                } else if (bank != null && randomNumberFromOne(100) <= 5) {
                    scriptAPI.randomWalkTo(bank.location, 3)
                }
                checkCounter(500)
                return
            }

            State.FIND_CITY -> {
                if (counter++ >= 500 || cityLocationsGE.contains(city)) {
                    scriptAPI.teleport(getRandomCity().also { city = it })
                    state = State.ADVENTURE
                }
                if (bot.location.equals(city)) {
                    state = State.ADVENTURE
                } else {
                    scriptAPI.randomWalkTo(city, randomNumberFromOne(10))
                }
                checkCounter(600)
                return
            }
        }
    }

    fun dialogue() {
        val until = 1225 - dateCode
        val lineStd = dialogue.getLines("standard")?.rand()
        var lineAlt = ""

        fun dialogue() {
            val until = 1225 - dateCode
            val lineStd = dialogue.getLines("standard")?.rand() ?: ""
            var lineAlt = ""

            when {
                dateCode == 1031 -> lineAlt = dialogue.getLines("halloween")?.rand() ?: ""
                until in 2..23 -> lineAlt = dialogue.getLines("approaching_christmas")?.rand() ?: ""
                dateCode == 1225 -> lineAlt = dialogue.getLines("christmas_day")?.rand() ?: ""
                dateCode == 1224 -> lineAlt = dialogue.getLines("christmas_eve")?.rand() ?: ""
                dateCode == 1231 -> lineAlt = dialogue.getLines("new_years_eve")?.rand() ?: ""
                dateCode == 101 -> lineAlt = dialogue.getLines("new_years")?.rand() ?: ""
                dateCode == 214 -> lineAlt = dialogue.getLines("valentines")?.rand() ?: ""
                dateCode == 404 -> lineAlt = dialogue.getLines("easter")?.rand() ?: ""
            }

        }

        var localPlayers = RegionManager.getLocalPlayers(bot)
        if (localPlayers.isNotEmpty()) {
            val localPlayer =
                localPlayers
                    .filter { it.name != bot.name }
                    .randomOrNull()
            if (localPlayer != null) {
                val chat =
                    if (lineAlt.isNotEmpty() && Random.nextBoolean()) {
                        lineAlt
                    } else {
                        lineStd
                    }?.replace("@name", localPlayer.username)
                        ?.replace("@timer", until.toString())
                if (chat != null) {
                    scriptAPI.sendChat(chat)
                }
            } else {
                val chat =
                    if (lineAlt.isNotEmpty() && Random.nextBoolean()) {
                        lineAlt
                    } else {
                        lineStd
                    }
                if (chat != null) {
                    scriptAPI.sendChat(chat)
                }
            }
        }
    }

    enum class State {
        START,
        ADVENTURE,
        FIND_BANK,
        FIND_CITY,
        IDLE_GE,
        GE,
        TELEPORTING,
        LOOT,
        LOOT_DELAY,
        FIND_GE,
    }

    override fun newInstance(): Script {
        val script = Adventurer(style)
        script.state = State.START
        val tier = CombatBotAssembler.Tier.MED
        if (type == CombatBotAssembler.Type.RANGE) {
            script.bot = CombatBotAssembler().RangeAdventurer(tier, bot.startLocation)
        } else {
            script.bot = CombatBotAssembler().MeleeAdventurer(tier, bot.startLocation)
        }
        return script
    }

    companion object {
        val yanille: Location = Location.create(2615, 3104, 0)
        val ardougne: Location = Location.create(2662, 3304, 0)
        val seers: Location = Location.create(2726, 3485, 0)
        val edgeville: Location = Location.create(3088, 3486, 0)
        val catherby: Location = Location.create(2809, 3435, 0)
        val falador: Location = Location.create(2965, 3380, 0)
        val varrock: Location = Location.create(3213, 3428, 0)
        val draynor: Location = Location.create(3080, 3250, 0)
        val rimmington: Location = Location.create(2977, 3239, 0)
        val lumbridge: Location = Location.create(3222, 3219, 0)
        val karamja: Location = Location.create(2849, 3033, 0)
        val alkharid: Location = Location.create(3297, 3219, 0)

        val feldiphills: Location = Location.create(2535, 2919, 0)
        val isafdar: Location = Location.create(2241, 3217, 0)
        val eaglespeek: Location = Location.create(2333, 3579, 0)
        val canafis: Location = Location.create(3492, 3485, 0)
        val treegnome: Location = Location.create(2437, 3441, 0)
        val teak1: Location = Location.create(2334, 3048, 0)
        val teakfarm: Location = Location.create(2825, 3085, 0)
        val keldagrimout: Location = Location.create(2724, 3692, 0)
        val miningguild: Location = Location.create(3046, 9740, 0)
        val magics: Location = Location.create(2285, 3146, 0)
        val coalTrucks: Location = Location.create(2581, 3481, 0)
        val crawlinghands: Location = Location.create(3422, 3548, 0)
        val gemrocks: Location = Location.create(2825, 2997, 0)
        val chaosnpc: Location = Location.create(2612, 9484, 0)
        val chaosnpc2: Location = Location.create(2586, 9501, 0)
        val varLumberYard: Location = Location.create(3289, 3482, 0)
        val taverly: Location = Location.create(2909, 3436, 0)

        val swGEClerk: Location = Location.create(3164, 3487, 0)
        val neGEClerk: Location = Location.create(3165, 3492, 0)
        val nwGEBanker: Location = Location.create(3162, 3490, 0)
        val seGEBanker: Location = Location.create(3167, 3489, 0)

        val badedge = ZoneBorders(3094, 3494, 3096, 3497)
        val badedge2: Location = Location.create(3094, 3492, 0)
        val badedge3: Location = Location.create(3094, 3490, 0)
        val badedge4: Location = Location.create(3094, 3494, 0)

        var citygroupA = listOf(falador, varrock, draynor, rimmington, lumbridge, edgeville)
        var citygroupB = listOf(yanille, ardougne, seers, catherby)

        val cities =
            listOf(
                swGEClerk,
                neGEClerk,
                nwGEBanker,
                seGEBanker,
                yanille,
                ardougne,
                seers,
                catherby,
                falador,
                varrock,
                draynor,
                rimmington,
                lumbridge,
                edgeville,
            )

        val pois =
            listOf(
                karamja,
                karamja,
                alkharid,
                alkharid,
                feldiphills,
                feldiphills,
                isafdar,
                eaglespeek,
                eaglespeek,
                canafis,
                treegnome,
                treegnome,
                teak1,
                teakfarm,
                keldagrimout,
                miningguild,
                coalTrucks,
                crawlinghands,
                magics,
                gemrocks,
                chaosnpc,
                chaosnpc,
                chaosnpc2,
                taverly,
                varLumberYard,
            )

        val cityLocationsGE = listOf(swGEClerk, neGEClerk, nwGEBanker, seGEBanker)

        val socialLocationsGE =
            listOf(
                Location.create(3158, 3483, 0),
                Location.create(3165, 3480, 0),
                Location.create(3172, 3483, 0),
                Location.create(3174, 3489, 0),
                Location.create(3171, 3497, 0),
                Location.create(3164, 3499, 0),
                Location.create(3157, 3497, 0),
                Location.create(3155, 3489, 0),
                Location.create(3167, 3492, 0),
                Location.create(3162, 3492, 0),
                Location.create(3162, 3487, 0),
                Location.create(3167, 3487, 0),
            )

        val clerkLocationsGe =
            listOf(
                Location.create(3165, 3492, 0),
                Location.create(3164, 3492, 0),
                Location.create(3164, 3487, 0),
                Location.create(3165, 3487, 0),
            )

        var bankMap =
            mapOf<Location, ZoneBorders>(
                falador to ZoneBorders(2950, 3374, 2943, 3368),
                varrock to ZoneBorders(3182, 3435, 3189, 3446),
                draynor to ZoneBorders(3092, 3240, 3095, 3246),
                edgeville to ZoneBorders(3093, 3498, 3092, 3489),
                yanille to ZoneBorders(2610, 3089, 2613, 3095),
                ardougne to ZoneBorders(2649, 3281, 2655, 3286),
                seers to ZoneBorders(2729, 3493, 2722, 3490),
                catherby to ZoneBorders(2807, 3438, 2811, 3441),
            )

        private val whiteWolfMountainTop = Location(2850, 3496, 0)
        private val catherbyToTopOfWhiteWolf =
            arrayOf(Location(2856, 3442, 0), Location(2848, 3455, 0), Location(2848, 3471, 0), Location(2848, 3487, 0))
        private val tavleryToTopOfWhiteWolf =
            arrayOf(
                Location(2872, 3425, 0),
                Location(2863, 3440, 0),
                Location(2863, 3459, 0),
                Location(2854, 3475, 0),
                Location(2859, 3488, 0),
            )

        val common_stuck_locations =
            mapOf(
                ZoneBorders(2878, 3386, 2884, 3395) to { it: Adventurer ->
                    it.scriptAPI.walkArray(
                        tavleryToTopOfWhiteWolf + whiteWolfMountainTop + catherbyToTopOfWhiteWolf.reversedArray(),
                    )
                },
                ZoneBorders(2874, 3390, 2880, 3401) to { it: Adventurer ->
                    it.scriptAPI.walkArray(
                        tavleryToTopOfWhiteWolf + whiteWolfMountainTop + catherbyToTopOfWhiteWolf.reversedArray(),
                    )
                },
                ZoneBorders(2865, 3408, 2874, 3423) to { it: Adventurer ->
                    it.scriptAPI.walkArray(
                        tavleryToTopOfWhiteWolf + whiteWolfMountainTop + catherbyToTopOfWhiteWolf.reversedArray(),
                    )
                },
                ZoneBorders(2855, 3454, 2852, 3450) to { it: Adventurer ->
                    it.scriptAPI.walkArray(
                        tavleryToTopOfWhiteWolf + whiteWolfMountainTop + catherbyToTopOfWhiteWolf.reversedArray(),
                    )
                },
                ZoneBorders(2861, 3425, 2867, 3432) to { it: Adventurer ->
                    it.scriptAPI.walkArray(
                        catherbyToTopOfWhiteWolf + whiteWolfMountainTop + tavleryToTopOfWhiteWolf.reversedArray(),
                    )
                },
                ZoneBorders(2863, 3441, 2859, 3438) to { it: Adventurer ->
                    it.scriptAPI.walkArray(
                        catherbyToTopOfWhiteWolf + whiteWolfMountainTop + tavleryToTopOfWhiteWolf.reversedArray(),
                    )
                },
                ZoneBorders(2937, 3356, 2936, 3353) to { it: Adventurer ->
                    val wall = it.scriptAPI.getNearestNode("Crumbling wall", true)
                    if (wall == null) {
                        it.refresh()
                        it.ticks = 0
                        return@to
                    }
                    it.scriptAPI.interact(it.bot, wall, "Climb-over")
                },
                ZoneBorders(3092, 3246, 3091, 3247) to { it: Adventurer ->
                    it.scriptAPI.walkTo(Location(3093, 3243, 0))
                },
                ZoneBorders(3140, 3468, 3140, 3468) to { it: Adventurer ->
                    it.scriptAPI.walkArray(
                        arrayOf(
                            Location.create(3135, 3516, 0),
                            Location.create(3103, 3489, 0),
                            Location.create(3082, 3423, 0),
                        ),
                    )
                },
            )

        val dialogue: JsonObject
        val dateCode: Int

        init {
            val reader = FileReader(ServerConstants.BOT_DATA_PATH + File.separator + "bot_dialogue.json")
            val gson = Gson()
            dialogue = gson.fromJson(reader, JsonObject::class.java)
            reader.close()

            val formatter = DateTimeFormatter.ofPattern("MMdd")
            val current = LocalDateTime.now()
            val formatted: String = current.format(formatter)
            dateCode = formatted.toInt()
        }

        private fun JsonObject.getLines(category: String): JsonArray? = this.getAsJsonArray(category)

        private fun JsonArray.rand(): String? {
            if (this.size() == 0) return null
            val index = (0 until this.size()).random()
            val element = this.get(index)
            return if (element.isJsonPrimitive && element.asJsonPrimitive.isString) element.asString else element.toString()
        }
    }
}
