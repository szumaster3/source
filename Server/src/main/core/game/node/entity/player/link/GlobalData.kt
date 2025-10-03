package core.game.node.entity.player.link

import com.google.gson.JsonObject
import content.data.GodBook

class GlobalData {
    private var runReplenishCharges: Int = 0
    private var lowAlchemyCharges: Int = 0
    private var forgingUses: Int = 0
    private var ectoCharges: Int = 0
    private var playerTestStage: Int = 0
    private var barrowsLoots: Int = 0
    private var lootSharePoints: Int = 0
    private var tutorialStage: Int = 0

    @JvmField
    var karamjaBananas: Int = 0
    private var savedX: Int = 0
    private var savedY: Int = 0
    private var savedH: Int = 0
    private var taskAmount: Int = 0
    private var taskPoints: Int = 0
    var godBook: Int = -1
    private var zaffAmount: Int = 8
    private var essenceTeleporter: Int = 0
    private var recoilDamage: Int = 40
    private var hunterCapeCharges: Int = 0
    private var bossCounters: IntArray = IntArray(25)
    private val rcDecays: IntArray = IntArray(3)
    private var assistExperience: DoubleArray = DoubleArray(9)
    private var homeTeleportDelay: Long = 0
    private var assistTime: Long = 0
    private var chatPing: Long = 0
    private var tutorClaim: Long = 0
    private var magicSkillCapeDelay: Long = 0
    private var hunterCapeDelay: Long = 0
    private var charmingDelay: Long = 0
    private var overChargeDelay: Long = 0
    private var lootShareDelay: Long = 0
    private var doubleExp: Long = 0
    private var globalTeleporterDelay: Long = 0
    var starSpriteDelay: Long = 0
    private var runReplenishDelay: Long = 0
    private var lowAlchemyDelay: Long = 0
    private var minigameTeleportDelay: Long = 0
    private var silkSteal: Long = 0
    private var teaSteal: Long = 0
    private var zaffTime: Long = 0
    private var doubleExpDelay: Long = 0
    private var dropDelay: Long = 0
    private var luthasTask: Boolean = false
    private var lumbridgeRope: Boolean = false
    private var apprentice: Boolean = false
    private var fritzGlass: Boolean = false
    private var wydinEmployee: Boolean = false
    private var draynorRecording: Boolean = false
    private var geTutorial: Boolean = false
    private var joinedMonastery: Boolean = false
    private var disableDeathScreen: Boolean = false
    private var disableNews: Boolean = false
    private var enableCoinMachine: Boolean = false
    private var enableCharmCollector: Boolean = false
    private var macroDisabled: Boolean = false
    private var strongholdRewards: BooleanArray = BooleanArray(4)
    var readPlaques: BooleanArray = BooleanArray(7)
    private var abyssData: BooleanArray = BooleanArray(4)
    private var travelLogs: BooleanArray = BooleanArray(45)
    private var godBooks: BooleanArray = BooleanArray(3)
    var godPages: BooleanArray = BooleanArray(4)

    fun parse(data: JsonObject) {
        tutorialStage = data["tutorialStage"]?.asInt ?: 0
        homeTeleportDelay = data["homeTeleportDelay"]?.asLong ?: 0L
        lumbridgeRope = data["lumbridgeRope"]?.asBoolean ?: false
        apprentice = data["apprentice"]?.asBoolean ?: false
        assistTime = data["assistTime"]?.asLong ?: 0L

        val ae = data["assistExperience"]?.asJsonArray
        if (ae != null) {
            for (i in 0 until ae.size()) {
                assistExperience[i] = ae[i].asDouble
            }
        }

        val sr = data["strongHoldRewards"]?.asJsonArray
        if (sr != null) {
            for (i in 0 until sr.size()) {
                strongholdRewards[i] = sr[i].asBoolean
            }
        }

        chatPing = data["chatPing"]?.asLong ?: 0L
        tutorClaim = data["tutorClaim"]?.asLong ?: 0L
        luthasTask = data["luthasTask"]?.asBoolean ?: false
        karamjaBananas = data["karamjaBananas"]?.asInt ?: 0
        silkSteal = data["silkSteal"]?.asLong ?: 0L
        teaSteal = data["teaSteal"]?.asLong ?: 0L
        zaffAmount = data["zafAmount"]?.asInt ?: 0
        zaffTime = data["zafTime"]?.asLong ?: 0L
        fritzGlass = data["fritzGlass"]?.asBoolean ?: false
        wydinEmployee = data["wydinEmployee"]?.asBoolean ?: false
        draynorRecording = data["draynorRecording"]?.asBoolean ?: false
        geTutorial = data["geTutorial"]?.asBoolean ?: false
        essenceTeleporter = data["essenceTeleporter"]?.asInt ?: 0
        recoilDamage = data["recoilDamage"]?.asInt ?: 0
        doubleExpDelay = data["doubleExpDelay"]?.asLong ?: 0L
        joinedMonastery = data["joinedMonastery"]?.asBoolean ?: false

        val rp = data["readPlaques"]?.asJsonArray
        if (rp != null) {
            for (i in 0 until rp.size()) {
                readPlaques[i] = rp[i].asBoolean
            }
        }

        forgingUses = data["forgingUses"]?.asInt ?: 0
        ectoCharges = data["ectoCharges"]?.asInt ?: 0
        dropDelay = data["dropDelay"]?.asLong ?: 0L

        val ad = data["abyssData"]?.asJsonArray
        if (ad != null) {
            for (i in 0 until ad.size()) {
                abyssData[i] = ad[i].asBoolean
            }
        }

        val rd = data["rcDecays"]?.asJsonArray
        if (rd != null) {
            for (i in 0 until rd.size()) {
                rcDecays[i] = rd[i].asInt
            }
        }

        disableDeathScreen = data["disableDeathScreen"]?.asBoolean ?: false
        playerTestStage = data["playerTestStage"]?.asInt ?: 0
        charmingDelay = data["charmingDelay"]?.asLong ?: 0L

        val tl = data["travelLogs"]?.asJsonArray
        if (tl != null) {
            for (i in 0 until tl.size()) {
                travelLogs[i] = tl[i].asBoolean
            }
        }

        val gb = data["godBooks"]?.asJsonArray
        if (gb != null) {
            for (i in 0 until gb.size()) {
                godBooks[i] = gb[i].asBoolean
            }
        }

        disableNews = data["disableNews"]?.asBoolean ?: false

        val gp = data["godPages"]?.asJsonArray
        if (gp != null) {
            for (i in 0 until gp.size()) {
                godPages[i] = gp[i].asBoolean
            }
        }

        overChargeDelay = data["overChargeDelay"]?.asLong ?: 0L

        val bc = data["bossCounters"]?.asJsonArray
        if (bc != null) {
            for (i in 0 until bc.size()) {
                bossCounters[i] = bc[i].asInt
            }
        }

        barrowsLoots = data["barrowsLoots"]?.asInt ?: 0
        lootShareDelay = data["lootShareDelay"]?.asLong ?: 0L
        lootSharePoints = data["lootSharePoints"]?.asInt ?: 0
        doubleExp = data["doubleExp"]?.asLong ?: 0L
        globalTeleporterDelay = data["globalTeleporterDelay"]?.asLong ?: 0L
        starSpriteDelay = data["starSpriteDelay"]?.asLong ?: 0L
        runReplenishDelay = data["runReplenishDelay"]?.asLong ?: 0L
        runReplenishCharges = data["runReplenishCharges"]?.asInt ?: 0
        lowAlchemyCharges = data["lowAlchemyCharges"]?.asInt ?: 0
        lowAlchemyDelay = data["lowAlchemyDelay"]?.asLong ?: 0L
        magicSkillCapeDelay = data["magicSkillCapeDelay"]?.asLong ?: 0L
        hunterCapeDelay = data["hunterCapeDelay"]?.asLong ?: 0L
        hunterCapeCharges = data["hunterCapeCharges"]?.asInt ?: 0
        taskAmount = data["taskAmount"]?.asInt ?: 0
        taskPoints = data["taskPoints"]?.asInt ?: 0
        macroDisabled = data["macroDisabled"]?.asBoolean ?: false
    }

    fun setSavedLocation(
        x: Int,
        y: Int,
        z: Int,
    ) {
        savedX = x
        savedY = y
        savedH = z
    }

    fun getTravelLogs(): BooleanArray = travelLogs

    fun removeTravelLog(index: Int) {
        travelLogs[index] = false
    }

    fun hasTravelLog(index: Int): Boolean = travelLogs[index]

    fun setTravelLog(index: Int) {
        travelLogs[index] = true
    }

    fun setCharmingDelay(delay: Long) {
        charmingDelay = delay
    }

    fun getCharmingDelay(): Long = charmingDelay

    fun getTestStage(): Int = playerTestStage

    fun setTestStage(stage: Int) {
        playerTestStage = stage
    }

    fun getTutorialStage(): Int = tutorialStage

    fun setTutorialStage(tutorialStage: Int) {
        this.tutorialStage = tutorialStage
    }

    fun getHomeTeleportDelay(): Long = homeTeleportDelay

    fun setHomeTeleportDelay(homeTeleportDelay: Long) {
        this.homeTeleportDelay = homeTeleportDelay
    }

    fun hasTiedLumbridgeRope(): Boolean = lumbridgeRope

    fun setLumbridgeRope(lumbridgeRope: Boolean) {
        this.lumbridgeRope = lumbridgeRope
    }

    fun hasSpokenToApprentice(): Boolean = apprentice

    fun setApprentice(apprentice: Boolean) {
        this.apprentice = apprentice
    }

    fun getAssistTime(): Long = assistTime

    fun setAssistTime(assistTime: Long) {
        this.assistTime = assistTime
    }

    fun getAssistExperience(): DoubleArray = assistExperience

    fun setAssistExperience(assistExperience: DoubleArray) {
        this.assistExperience = assistExperience
    }

    fun getStrongHoldRewards(): BooleanArray = strongholdRewards

    fun hasStrongholdReward(reward: Int): Boolean = strongholdRewards[reward - 1]

    fun getChatPing(): Long = chatPing

    fun setChatPing(chatPing: Long) {
        this.chatPing = chatPing
    }

    fun getTutorClaim(): Long = tutorClaim

    fun setTutorClaim(tutorClaim: Long) {
        this.tutorClaim = tutorClaim
    }

    fun isLuthasTask(): Boolean = luthasTask

    fun setLuthasTask(luthasTask: Boolean) {
        this.luthasTask = luthasTask
    }

    fun getKaramjaBananas(): Int = karamjaBananas

    fun setKaramjaBannanas(karamjaBannanas: Int) {
        this.karamjaBananas = karamjaBannanas
    }

    fun getTeaSteal(): Long = teaSteal

    fun setTeaSteal(teaSteal: Long) {
        this.teaSteal = teaSteal
    }

    fun getSilkSteal(): Long = silkSteal

    fun setSilkSteal(silkSteal: Long) {
        this.silkSteal = silkSteal
    }

    fun getZaffAmount(): Int = zaffAmount

    fun setZaffAmount(zaffAmount: Int) {
        this.zaffAmount = zaffAmount
    }

    fun getZaffTime(): Long = zaffTime

    fun isDraynorRecording(): Boolean = draynorRecording

    fun setDraynorRecording(draynorRecording: Boolean) {
        this.draynorRecording = draynorRecording
    }

    fun isWydinEmployee(): Boolean = wydinEmployee

    fun setWydinEmployee(wydinEmployee: Boolean) {
        this.wydinEmployee = wydinEmployee
    }

    fun setZaffTime(zaffTime: Long) {
        this.zaffTime = zaffTime
    }

    fun isFritzGlass(): Boolean = fritzGlass

    fun setFritzGlass(frizGlass: Boolean) {
        this.fritzGlass = frizGlass
    }

    fun isGeTutorial(): Boolean = geTutorial

    fun setGeTutorial(geTutorial: Boolean) {
        this.geTutorial = geTutorial
    }

    fun getEssenceTeleporter(): Int = essenceTeleporter

    fun setEssenceTeleporter(essenceTeleporter: Int) {
        this.essenceTeleporter = essenceTeleporter
    }

    fun getRecoilDamage(): Int = recoilDamage

    fun setRecoilDamage(recoilDamage: Int) {
        this.recoilDamage = recoilDamage
    }

    fun getDoubleExpDelay(): Long = doubleExpDelay

    fun setDoubleExpDelay(doubleExpDelay: Long) {
        this.doubleExpDelay = doubleExpDelay
    }

    fun isJoinedMonastery(): Boolean = joinedMonastery

    fun setJoinedMonastery(joinedMonastery: Boolean) {
        this.joinedMonastery = joinedMonastery
    }

    fun hasReadPlaques(): Boolean {
        for (i in readPlaques.indices) {
            if (!readPlaques[i]) {
                return false
            }
        }
        return true
    }

    fun setGodBook(book: GodBook) {
        godBooks[book.ordinal] = true
    }

    fun hasCompletedGodBook(book: GodBook): Boolean = godBooks[book.ordinal]

    fun getForgingUses(): Int = forgingUses

    fun setForgingUses(forgingUses: Int) {
        this.forgingUses = forgingUses
    }

    fun getEctoCharges(): Int = ectoCharges

    fun setEctoCharges(ectoCharges: Int) {
        this.ectoCharges = ectoCharges
    }

    fun resetAbyss() {
        for (i in abyssData.indices) {
            abyssData[i] = false
        }
    }

    fun setAbyssCharge(ordinal: Int) {
        abyssData[ordinal] = true
    }

    fun hasAbyssCharge(ordinal: Int): Boolean = abyssData[ordinal]

    fun getDropDelay(): Long = dropDelay

    fun setDropDelay(dropDelay: Long) {
        this.dropDelay = dropDelay
    }

    fun getRcDecay(ordinal: Int): Int {
        if (ordinal < 0) {
            return 0
        }
        return rcDecays[ordinal]
    }

    fun getRcDecays(): IntArray = rcDecays

    fun isDeathScreenDisabled(): Boolean = disableDeathScreen

    fun setDisableDeathScreen(b: Boolean) {
        this.disableDeathScreen = b
    }

    fun getGodBooks(): BooleanArray = godBooks

    fun setGodBooks(godBooks: BooleanArray) {
        this.godBooks = godBooks
    }

    fun isDisableNews(): Boolean = disableNews

    fun setDisableNews(disableNews: Boolean) {
        this.disableNews = disableNews
    }

    fun getOverChargeDelay(): Long = overChargeDelay

    fun setOverChargeDelay(overChargeDelay: Long) {
        this.overChargeDelay = overChargeDelay
    }

    fun getBossCounters(): IntArray = bossCounters

    fun setBossCounters(bossCounters: IntArray) {
        this.bossCounters = bossCounters
    }

    fun getBarrowsLoots(): Int = barrowsLoots

    fun setBarrowsLoots(barrowsLoots: Int) {
        this.barrowsLoots = barrowsLoots
    }

    fun getLootSharePoints(): Int = lootSharePoints

    fun setLootSharePoints(lootSharePoints: Int) {
        this.lootSharePoints = lootSharePoints
    }

    fun getLootShareDelay(): Long = lootShareDelay

    fun setLootShareDelay(lootShareDelay: Long) {
        this.lootShareDelay = lootShareDelay
    }

    fun getDoubleExp(): Long = doubleExp

    fun setDoubleExp(doubleExp: Long) {
        this.doubleExp = doubleExp
    }

    fun hasDoubleExp(): Boolean = doubleExp > System.currentTimeMillis()

    fun getGlobalTeleporterDelay(): Long = globalTeleporterDelay

    fun setGlobalTeleporterDelay(globalTeleporterDelay: Long) {
        this.globalTeleporterDelay = globalTeleporterDelay
    }

    fun getRunReplenishDelay(): Long = runReplenishDelay

    fun setRunReplenishDelay(runReplenishDelay: Long) {
        this.runReplenishDelay = runReplenishDelay
    }

    fun getRunReplenishCharges(): Int = runReplenishCharges

    fun setRunReplenishCharges(runReplenishCharges: Int) {
        this.runReplenishCharges = runReplenishCharges
    }

    fun getLowAlchemyCharges(): Int = lowAlchemyCharges

    fun setLowAlchemyCharges(lowAlchemyCharges: Int) {
        this.lowAlchemyCharges = lowAlchemyCharges
    }

    fun getLowAlchemyDelay(): Long = lowAlchemyDelay

    fun setLowAlchemyDelay(lowAlchemyDelay: Long) {
        this.lowAlchemyDelay = lowAlchemyDelay
    }

    fun isEnableCoinMachine(): Boolean = enableCoinMachine

    fun setEnableCoinMachine(enableCoinMachine: Boolean) {
        this.enableCoinMachine = enableCoinMachine
    }

    fun getMagicSkillCapeDelay(): Long = magicSkillCapeDelay

    fun setMagicSkillCapeDelay(magicSkillCapeDelay: Long) {
        this.magicSkillCapeDelay = magicSkillCapeDelay
    }

    fun getHunterCapeDelay(): Long = hunterCapeDelay

    fun setHunterCapeDelay(hunterCapeDelay: Long) {
        this.hunterCapeDelay = hunterCapeDelay
    }

    fun getHunterCapeCharges(): Int = hunterCapeCharges

    fun setHunterCapeCharges(hunterCapeCharges: Int) {
        this.hunterCapeCharges = hunterCapeCharges
    }

    fun isEnableCharmCollector(): Boolean = enableCharmCollector

    fun setEnableCharmCollector(enableCharmCollector: Boolean) {
        this.enableCharmCollector = enableCharmCollector
    }

    fun getMinigameTeleportDelay(): Long = minigameTeleportDelay

    fun setMinigameTeleportDelay(delay: Long) {
        this.minigameTeleportDelay = delay
    }

    fun getSavedH(): Int = savedH

    fun setSavedH(savedH: Int) {
        this.savedH = savedH
    }

    fun getSavedY(): Int = savedY

    fun setSavedY(savedY: Int) {
        this.savedY = savedY
    }

    fun getSavedX(): Int = savedX

    fun setSavedX(savedX: Int) {
        this.savedX = savedX
    }

    fun getTaskAmount(): Int = taskAmount

    fun setTaskAmount(taskAmount: Int) {
        this.taskAmount = taskAmount
    }

    fun getTaskPoints(): Int = taskPoints

    fun setTaskPoints(taskPoints: Int) {
        this.taskPoints = taskPoints
    }

    fun setMacroDisabled(disabled: Boolean) {
        this.macroDisabled = disabled
    }

    fun getMacroDisabled(): Boolean = this.macroDisabled

    fun getAbyssData(): BooleanArray = abyssData
}
