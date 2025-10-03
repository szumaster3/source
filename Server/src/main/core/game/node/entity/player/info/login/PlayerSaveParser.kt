package core.game.node.entity.player.info.login

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import core.ServerConstants
import core.Util
import core.api.PersistPlayer
import core.api.log
import core.game.node.entity.combat.graves.GraveController
import core.game.node.entity.combat.graves.GraveType
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.entity.player.link.music.MusicEntry
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.Log
import java.io.File
import java.io.FileReader
import java.sql.Timestamp
import java.util.*

class PlayerSaveParser(val player: Player) {
    companion object {
        val contentHooks = ArrayList<PersistPlayer>()
    }

    var reader: FileReader? = null
    var saveFile: JsonObject? = null
    var read = true

    fun parse() {
        val file = File("${ServerConstants.PLAYER_SAVE_PATH}${player.name}.json")
        if (!file.exists()) {
            player.details.saveParsed = true
            read = false
            return
        }
        runCatching {
            FileReader(file).use { saveFile = JsonParser.parseReader(it).asJsonObject }
            parseData()
            read = true
            player.details.saveParsed = true
        }.onFailure {
            log(this::class.java, Log.ERR, "Error parsing save file for ${player.name}: ${it.message}")
            read = false
            player.details.saveParsed = false
        }
    }

    fun parseData() {
        parseCore()
        parseAttributes()
        parseSkills()
        parseSettings()
        parseQuests()
        parseAppearance()
        parseGrave()
        parseVarps()
        parseStates()
        parseSpellbook()
        parseSavedData()
        parseAutocastSpell()
        parseFarming()
        parseConfigs()
        parseMusic()
        parseFamiliars()
        parseBankPin()
        parseHouse()
        parseIronman()
        parseEmoteManager()
        parseStatistics()
        parseAchievements()
        parsePouches()
        parseHeadgear()
        parseBoltPouch()
        parseCostumeRoom()
        parseVersion()
    }

    fun runContentHooks() {
        if (read && saveFile != null) {
            contentHooks.forEach { it.parsePlayer(player, saveFile!!) }
            player.details.saveParsed = true
        }
    }

    fun parseVarps() {
        val varps = saveFile?.getAsJsonArray("varps")
        if (varps != null) {
            player.varpManager.parse(varps)
        }
    }

    fun parseAttributes() {
        val attrData = saveFile?.getAsJsonArray("attributes") ?: run {
            player.gameAttributes.parse(player.name + ".xml")
            return
        }

        for (elem in attrData) {
            val attr = elem.asJsonObject
            val key = attr.get("key")?.asString ?: continue
            val type = attr.get("type")?.asString ?: continue
            val isExpirable = attr.get("expirable")?.asBoolean ?: false

            if (isExpirable) {
                val expireTime = attr.get("expiration-time")?.asLong ?: 0L
                if (expireTime < System.currentTimeMillis()) continue
                player.gameAttributes.keyExpirations[key] = expireTime
            }

            player.gameAttributes.savedAttributes.add(key)

            player.gameAttributes.attributes[key] = when (type) {
                "int" -> attr.get("value")?.asInt
                "str" -> attr.get("value")?.asString
                "short" -> attr.get("value")?.asShort
                "long" -> attr.get("value")?.asLong
                "bool" -> attr.get("value")?.asBoolean
                "byte" -> Base64.getDecoder().decode(attr.get("value")?.asString ?: "")?.get(0)
                "location" -> Location.fromString(attr.get("value")?.asString ?: "")
                "time" -> Timestamp.valueOf(attr.get("value")?.asString ?: "")
                else -> {
                    log(this::class.java, Log.WARN, "Invalid data type for key: $key in PlayerSaveParser.kt")
                    null
                }
            }
        }
    }

    fun parseSkills() {
        val skillData = saveFile?.getAsJsonArray("skills") ?: return
        player.skills.parse(skillData)
        player.skills.experienceGained = saveFile?.get("totalEXP")?.asDouble ?: 0.0
        player.skills.experienceMultiplier = saveFile?.get("exp_multiplier")?.asDouble ?: 1.0

        if (GameWorld.settings?.default_xp_rate != 1.0) {
            player.skills.experienceMultiplier = GameWorld.settings?.default_xp_rate!!
        }

        if (saveFile?.has("milestone") == true) {
            val milestone = saveFile?.getAsJsonObject("milestone")
            player.skills.combatMilestone = milestone?.get("combatMilestone")?.asInt ?: 0
            player.skills.skillMilestone = milestone?.get("skillMilestone")?.asInt ?: 0
        }
    }

    fun parseSettings() {
        val settingsData = saveFile?.getAsJsonObject("settings") ?: return
        player.settings.parse(settingsData)
    }

    fun parseQuests() {
        val questData = saveFile?.getAsJsonObject("quests") ?: return
        player.questRepository.parse(questData)
    }

    fun parseAppearance() {
        val appearanceData = saveFile?.getAsJsonObject("appearance") ?: return
        player.appearance.parse(appearanceData)
    }

    fun parseGrave() {
        val graveData = saveFile?.get("grave_type")?.asString?.toIntOrNull() ?: return
        val type = GraveType.values()[graveData]
        GraveController.updateGraveType(player, type)
    }

    fun parseSpellbook() {
        val spellbookData = saveFile?.get("spellbook")?.asString?.toIntOrNull() ?: return
        SpellBookManager.SpellBook.forInterface(spellbookData)?.let { player.spellBookManager.setSpellBook(it) }
    }

    fun parseSavedData() {
        val activityData = saveFile?.getAsJsonObject("activityData") ?: return
        val questData = saveFile?.getAsJsonObject("questData") ?: return
        val globalData = saveFile?.getAsJsonObject("globalData") ?: return
        player.savedData.activityData.parse(activityData)
        player.savedData.questData.parse(questData)
        player.savedData.globalData.parse(globalData)
    }

    fun parseAutocastSpell() {
        val autocast = saveFile?.getAsJsonObject("autocastSpell") ?: return
        val book = autocast.get("book")?.asString?.toIntOrNull() ?: return
        val spellId = autocast.get("spellId")?.asString?.toIntOrNull() ?: return
        player.properties.autocastSpell = SpellBookManager.SpellBook.values()[book].getSpell(spellId) as CombatSpell
    }

    fun parseFarming() {
        // Implement if needed
    }

    fun parseConfigs() {
        val configs = saveFile?.getAsJsonArray("configs") ?: return
        for (config in configs) {
            val c = config.asJsonObject
            val index = c.get("index")?.asString?.toIntOrNull() ?: continue
            if (index == 1048) continue
            val value = c.get("value")?.asString?.toIntOrNull() ?: continue
            player.varpMap[index] = value
        }
    }

    fun parseMusic() {
        val unlockedSongs = saveFile?.getAsJsonArray("unlockedMusic") ?: return
        for (song in unlockedSongs) {
            val s = song.asString.toIntOrNull() ?: continue
            val entry = MusicEntry.forId(s) ?: continue
            player.musicPlayer.unlocked[entry.index] = entry
        }
    }

    fun parseStates() {
        player.states.clear()
        if (saveFile != null && saveFile!!.has("states")) {
            val states: JsonArray = saveFile!!.getAsJsonArray("states")
            for (element in states) {
                val s: JsonObject = element.asJsonObject
                val stateId = s.get("stateKey").asString
                if (player.states[stateId] != null) continue
                val stateClass = player.registerState(stateId)
                stateClass?.parse(s)
                stateClass?.init()
                player.states[stateId] = stateClass
            }
        }
    }

    fun parseFamiliars() {
        val familiarData = saveFile?.getAsJsonObject("familiarManager") ?: return
        player.familiarManager.parse(familiarData)
    }

    fun parseBankPin() {
        val bpData = saveFile?.getAsJsonObject("bankPinManager") ?: return
        player.bankPinManager.parse(bpData)
    }

    fun parseHouse() {
        val houseData = saveFile?.getAsJsonObject("houseData") ?: return
        player.houseManager.parse(houseData)
    }

    fun parseIronman() {
        val ironmanMode = saveFile?.get("ironManMode")?.asString?.toIntOrNull() ?: return
        player.ironmanManager.mode = IronmanMode.values()[ironmanMode]
    }

    fun parseEmoteManager() {
        val emoteData = saveFile?.getAsJsonArray("emoteData") ?: return
        for (emote in emoteData) {
            val eIndex = emote.asString.toIntOrNull() ?: continue
            val e = Emotes.values().getOrNull(eIndex) ?: continue
            if (!player.emoteManager.emotes.contains(e)) {
                player.emoteManager.emotes.add(e)
            }
        }
    }

    fun parseStatistics() {
        val stats = saveFile?.getAsJsonArray("statistics") ?: return
        for (stat in stats) {
            val s = stat.asJsonObject
            val index = s.get("index")?.asString?.toIntOrNull() ?: continue
            val value = s.get("value")?.asString?.toIntOrNull() ?: continue
            // TODO: process stats as needed
        }
    }

    fun parseAchievements() {
        val achvData = saveFile?.getAsJsonArray("achievementDiaries")
        if (achvData != null) {
            player.achievementDiaryManager.parse(achvData)
        } else {
            player.achievementDiaryManager.resetRewards()
        }
    }

    fun parsePouches() {
        val pouchData = saveFile?.getAsJsonArray("pouches") ?: return
        player.pouchManager.parse(pouchData)
    }

    fun parseHeadgear() {
        val headgearData = saveFile?.getAsJsonArray("summon_ench_helm") ?: return
        player.enchgearManager.parse(headgearData)
    }

    fun parseBoltPouch() {
        val boltPouchData = saveFile?.getAsJsonArray("bolt_pouch") ?: return
        player.boltPouchManager.parse(boltPouchData)
    }

    fun parseCostumeRoom() {
        val costumeRaw = saveFile?.get("costumeRoom") ?: return
        val costumeJson = costumeRaw.asJsonObject ?: return
        player.getCostumeRoomState().readJson(costumeJson)
    }

    fun parseVersion() {
        player.version = 0
        player.version = saveFile?.get("version")?.asString?.toIntOrNull() ?: 0
    }

    fun parseCore() {
        val coreData = saveFile?.getAsJsonObject("core_data") ?: return

        val inventory = coreData.getAsJsonArray("inventory")
        val bank = coreData.getAsJsonArray("bank")
        val bankSecondary = coreData.getAsJsonArray("bankSecondary") ?: JsonArray()
        val equipment = coreData.getAsJsonArray("equipment")
        val bBars = coreData.getAsJsonArray("blastBars")
        val bOre = coreData.getAsJsonArray("blastOre")
        val bCoal = coreData.getAsJsonArray("blastCoal")
        val varpData = coreData.getAsJsonArray("varp")
        val timerData = coreData.getAsJsonObject("timers")
        val location = coreData.get("location")?.asString ?: ""

        val bankTabData = coreData.getAsJsonArray("bankTabs")
        if (bankTabData != null) {
            for (i in bankTabData) {
                val tab = i.asJsonObject
                val index = tab.get("index")?.asString?.toIntOrNull() ?: continue
                val startSlot = tab.get("startSlot")?.asString?.toIntOrNull() ?: continue
                player.bankPrimary.tabStartSlot[index] = startSlot
            }
        }

        val bankTabSecondaryData = coreData.getAsJsonArray("bankTabsSecondary")
        if (bankTabSecondaryData != null) {
            for (i in bankTabSecondaryData) {
                val tab = i.asJsonObject
                val index = tab.get("index")?.asString?.toIntOrNull() ?: continue
                val startSlot = tab.get("startSlot")?.asString?.toIntOrNull() ?: continue
                player.bankSecondary.tabStartSlot[index] = startSlot
            }
        }

        player.useSecondaryBank = coreData.get("useSecondaryBank")?.asBoolean ?: false

        player.inventory.parse(inventory)
        player.bankPrimary.parse(bank)
        player.bankSecondary.parse(bankSecondary)
        player.equipment.parse(equipment)
        bBars?.let { player.blastBars.parse(it) }
        bOre?.let { player.blastOre.parse(it) }
        bCoal?.let { player.blastCoal.parse(it) }
        player.location = Util.parseLocation(location)

        if (varpData != null) {
            for (e in varpData) {
                val varp = e.asJsonObject
                val index = varp.get("index")?.asString?.toIntOrNull() ?: continue
                val value = varp.get("value")?.asString?.toIntOrNull() ?: continue
                player.varpMap[index] = value
                player.saveVarp[index] = true
            }
        }

        timerData?.let { player.timers.parseTimers(it) }
    }
}
