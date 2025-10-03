package content.global.skill.farming

import core.api.getVarbit
import core.api.log
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.tools.Log
import core.tools.RandomFunction
import shared.consts.Items
import java.util.concurrent.TimeUnit
import kotlin.math.ceil
import kotlin.math.min

class Patch(
    val player: Player,
    val patch: FarmingPatch,
    var plantable: Plantable?,
    var currentGrowthStage: Int,
    var isDiseased: Boolean,
    var isDead: Boolean,
    var isWatered: Boolean,
    var nextGrowth: Long,
    var harvestAmt: Int,
    var isCheckHealth: Boolean,
) {
    constructor(player: Player, patch: FarmingPatch) : this(player, patch, null, 0, false, false, false, 0L, 0, false)

    var diseaseMod = 0
    var compost = CompostType.NONE
    var protectionPaid = false
    var cropLives = 3

    fun setNewHarvestAmount() {
        val compostMod =
            when (compost) {
                CompostType.NONE -> 0
                CompostType.COMPOST -> 1
                CompostType.SUPER_COMPOST -> 2
            }
        harvestAmt =
            when (plantable) {
                Plantable.LIMPWURT_SEED, Plantable.WOAD_SEED -> 3
                Plantable.MUSHROOM_SPORE -> 6
                Plantable.WILLOW_SAPLING -> 0
                else -> 1
            }
        if (plantable != null && plantable?.applicablePatch != PatchType.FLOWER_PATCH) {
            harvestAmt += compostMod
        }
        cropLives = 3 + compostMod
    }

    fun rollLivesDecrement(
        farmingLevel: Int,
        magicSecateurs: Boolean,
    ) {
        if (patch.type == PatchType.HERB_PATCH) {
            var herbSaveLow =
                when (plantable) {
                    Plantable.GUAM_SEED -> min(24 + farmingLevel, 80)
                    Plantable.MARRENTILL_SEED -> min(28 + farmingLevel, 80)
                    Plantable.TARROMIN_SEED -> min(31 + farmingLevel, 80)
                    Plantable.HARRALANDER_SEED -> min(36 + farmingLevel, 80)
                    Plantable.GOUT_TUBER -> min(39 + farmingLevel, 80)
                    Plantable.RANARR_SEED -> min(39 + farmingLevel, 80)
                    Plantable.SPIRIT_WEED_SEED -> min(43 + farmingLevel, 80)
                    Plantable.TOADFLAX_SEED -> min(43 + farmingLevel, 80)
                    Plantable.IRIT_SEED -> min(46 + farmingLevel, 80)
                    Plantable.AVANTOE_SEED -> min(50 + farmingLevel, 80)
                    Plantable.KWUARM_SEED -> min(54 + farmingLevel, 80)
                    Plantable.SNAPDRAGON_SEED -> min(57 + farmingLevel, 80)
                    Plantable.CADANTINE_SEED -> min(60 + farmingLevel, 80)
                    Plantable.LANTADYME_SEED -> min(64 + farmingLevel, 80)
                    Plantable.DWARF_WEED_SEED -> min(67 + farmingLevel, 80)
                    Plantable.TORSTOL_SEED -> min(71 + farmingLevel, 80)
                    else -> -1
                }

            if (magicSecateurs) herbSaveLow = ceil(1.10 * herbSaveLow).toInt()

            val rand = RandomFunction.random(256)

            if (rand > herbSaveLow) {
                cropLives -= 1
            }
        } else {
            var chance =
                when (patch.type) {
                    PatchType.ALLOTMENT -> 8
                    PatchType.HOPS_PATCH -> 6
                    PatchType.BELLADONNA_PATCH -> 2
                    PatchType.EVIL_TURNIP_PATCH -> 2
                    PatchType.CACTUS_PATCH -> 3
                    else -> 0
                }

            if (magicSecateurs) chance += ceil(1.10 * chance).toInt()

            if (RandomFunction.roll(chance)) cropLives -= 1
        }

        if (cropLives <= 0) clear()
    }

    fun isWeedy(): Boolean = getCurrentState() in 0..2

    fun isEmptyAndWeeded(): Boolean = getCurrentState() == 3

    fun getCurrentState(): Int = getVarbit(player, patch.varbit)

    fun setCurrentState(state: Int) {
        setVarbit(player, patch.varbit, state)
        updateBit()
    }

    fun setVisualState(state: Int) {
        val finalState = ensureStateSanity(state)
        setVarbit(player, patch.varbit, finalState)
    }

    fun ensureStateSanity(state: Int): Int {
        val patchDef = FarmingPatch.getSceneryDefByVarbit(patch.varbit) ?: return state
        val currentStateDef = patchDef.getChildObjectAtIndex(state)
        if (currentStateDef.name == patchDef.getChildObjectAtIndex(3).name) {
            if (state and 0x40 != 0) {
                isDead = false
                isWatered = false
                log(
                    this::class.java,
                    Log.DEBUG,
                    "Patch for ${player.username} at varbit ${patch.varbit} with plantable ${plantable?.name ?: "none"} was set to watered/dead at stage $currentGrowthStage, which isn't valid.",
                )
                return (state and (0x40.inv()))
            } else if (state and 0x80 != 0) {
                isDiseased = false
                log(
                    this::class.java,
                    Log.DEBUG,
                    "Patch for ${player.username} at varbit ${patch.varbit} with plantable ${plantable?.name ?: "none"} was set to diseased at stage $currentGrowthStage, which isn't valid.",
                )
                return (state and (0x80.inv()))
            } else if (state in listOf(0, 1, 2, 3)) {
            } else {
                log(
                    this::class.java,
                    Log.ERR,
                    "Patch for ${player.username} at varbit ${patch.varbit} with plantable ${plantable?.name ?: "none"} was set to state $state at growth stage $currentGrowthStage, which isn't valid. We're not sure why this is happening.",
                )
            }
        }
        return state
    }

    fun isFertilized(): Boolean = compost != CompostType.NONE

    fun isGrown(): Boolean = currentGrowthStage == (plantable?.stages ?: 0)

    fun updateBit() {
        if (isCheckHealth) {
            when (patch.type) {
                PatchType.FRUIT_TREE_PATCH ->
                    setVarbit(
                        player,
                        patch.varbit,
                        plantable!!.value + plantable!!.stages + 20,
                    )

                PatchType.BUSH_PATCH ->
                    setVarbit(
                        player,
                        patch.varbit,
                        250 + (plantable!!.ordinal - Plantable.REDBERRY_SEED.ordinal),
                    )

                PatchType.CACTUS_PATCH -> setVarbit(player, patch.varbit, 31)
                PatchType.TREE_PATCH -> setVarbit(player, patch.varbit, plantable!!.value + plantable!!.stages)
                else ->
                    log(
                        this::class.java,
                        Log.WARN,
                        "Invalid setting of isCheckHealth for patch type: " + patch.type.name,
                    )
            }
        } else {
            when (patch.type) {
                PatchType.ALLOTMENT, PatchType.FLOWER_PATCH, PatchType.HOPS_PATCH -> {
                    var state = getUnmodifiedValue()
                    if (isWatered || isDead) state = state or 0x40
                    if (isDiseased) state = state or 0x80

                    if (state != getVarbit(player, patch.varbit)) {
                        setVisualState(state)
                    }
                }

                PatchType.BUSH_PATCH -> {
                    if (isDead) {
                        setVisualState(getBushDeathValue())
                    } else if (isDiseased && !isDead) {
                        setVisualState(getBushDiseaseValue())
                    }
                }

                PatchType.TREE_PATCH -> {
                    var state = getVarbit(player, patch.varbit)

                    if (isDead) {
                        state = state or 0x80
                    } else if (isDiseased) {
                        state = state or 0x40
                    }

                    if (state != getVarbit(player, patch.varbit)) {
                        setVisualState(state)
                    }
                }

                PatchType.FRUIT_TREE_PATCH -> {
                    if (isDead) {
                        setVisualState(getFruitTreeDeathValue())
                    } else if (isDiseased && !isDead) {
                        setVisualState(getFruitTreeDiseaseValue())
                    }
                }

                PatchType.BELLADONNA_PATCH -> {
                    if (isDead) {
                        setVisualState(getBelladonnaDeathValue())
                    } else if (isDiseased && !isDead) {
                        setVisualState(getBelladonnaDiseaseValue())
                    } else {
                        setVisualState((plantable?.value ?: 0) + currentGrowthStage)
                    }
                }

                PatchType.CACTUS_PATCH -> {
                    if (isDead) {
                        setVisualState(getCactusDeathValue())
                    } else if (isDiseased && !isDead) {
                        setVisualState(getCactusDiseaseValue())
                    }
                }

                PatchType.HERB_PATCH -> {
                    if (isDead) {
                        setVisualState(getHerbDeathValue())
                    } else if (isDiseased && !isDead) {
                        setVisualState(getHerbDiseaseValue())
                    } else {
                        setVisualState((plantable?.value ?: 0) + currentGrowthStage)
                    }
                }

                else -> {}
            }
        }
    }

    fun cureDisease() {
        setVarbit(player, patch.varbit, (plantable?.value ?: 0) + currentGrowthStage)
        isDiseased = false
        updateBit()
    }

    fun water() {
        isWatered = true
        updateBit()
    }

    private fun getUnmodifiedValue(): Int = (plantable?.value ?: 0) + currentGrowthStage

    private fun getBushDiseaseValue(): Int {
        if (plantable == Plantable.POISON_IVY_SEED) {
            return (plantable?.value ?: 0) + currentGrowthStage + 12
        } else {
            return (plantable?.value ?: 0) + currentGrowthStage + 64
        }
    }

    private fun getBushDeathValue(): Int {
        if (plantable == Plantable.POISON_IVY_SEED) {
            return (plantable?.value ?: 0) + currentGrowthStage + 22
        } else {
            return (plantable?.value ?: 0) + currentGrowthStage + 126
        }
    }

    private fun getFruitTreeDiseaseValue(): Int = (plantable?.value ?: 0) + currentGrowthStage + 12

    private fun getFruitTreeDeathValue(): Int = (plantable?.value ?: 0) + currentGrowthStage + 18

    private fun getBelladonnaDiseaseValue(): Int = (plantable?.value ?: 0) + currentGrowthStage + 4

    private fun getBelladonnaDeathValue(): Int = (plantable?.value ?: 0) + currentGrowthStage + 7

    private fun getCactusDiseaseValue(): Int = (plantable?.value ?: 0) + currentGrowthStage + 10

    private fun getCactusDeathValue(): Int = (plantable?.value ?: 0) + currentGrowthStage + 16

    private fun getHerbDiseaseValue(): Int =
        if (plantable?.value ?: -1 <= 103) {
            128 + (((plantable?.ordinal ?: 0) - Plantable.GUAM_SEED.ordinal) * 3) + currentGrowthStage - 1
        } else if (plantable == Plantable.SPIRIT_WEED_SEED) {
            211 + currentGrowthStage - 1
        } else {
            198 + currentGrowthStage - 1
        }

    private fun getHerbDeathValue(): Int =
        if (plantable == Plantable.GOUT_TUBER) {
            201 + currentGrowthStage - 1
        } else {
            170 + currentGrowthStage - 1
        }

    private fun grow() {
        if ((isWeedy() || isEmptyAndWeeded()) && getCurrentState() > 0) {
            nextGrowth = System.currentTimeMillis() + 60000
            setCurrentState(getCurrentState() - 1)
            currentGrowthStage--
            return
        }

        if (isDiseased) {
            isDead = true
            return
        }

        diseaseMod =
            if (diseaseMod < 0) {
                -128
            } else {
                when (compost) {
                    CompostType.NONE -> 0
                    CompostType.COMPOST -> 8
                    CompostType.SUPER_COMPOST -> 13
                }
            }

        if (patch != FarmingPatch.TROLL_STRONGHOLD_HERB &&
            RandomFunction.random(128) <= (17 - diseaseMod) &&
            !isWatered &&
            !isGrown() &&
            !protectionPaid &&
            !isFlowerProtected() &&
            patch.type != PatchType.EVIL_TURNIP_PATCH &&
            currentGrowthStage != 0
        ) {
            isDiseased = true

            if (diseaseMod < 0) diseaseMod = 0
            return
        }

        if ((
                patch.type == PatchType.FRUIT_TREE_PATCH ||
                    patch.type == PatchType.TREE_PATCH ||
                    patch.type == PatchType.BUSH_PATCH ||
                    patch.type == PatchType.CACTUS_PATCH
            ) &&
            plantable != null &&
            plantable?.stages == currentGrowthStage + 1
        ) {
            isCheckHealth = true
        }

        if ((
                patch.type == PatchType.FRUIT_TREE_PATCH ||
                    patch.type == PatchType.BUSH_PATCH ||
                    patch.type == PatchType.CACTUS_PATCH
            ) &&
            plantable?.stages == currentGrowthStage
        ) {
            if ((patch.type == PatchType.BUSH_PATCH && getFruitOrBerryCount() < 4) ||
                (patch.type == PatchType.FRUIT_TREE_PATCH && getFruitOrBerryCount() < 6) ||
                (patch.type == PatchType.CACTUS_PATCH && getFruitOrBerryCount() < 3)
            ) {
                setCurrentState(getCurrentState() + 1)
            }
        }
        if (patch.type == PatchType.TREE_PATCH) {
            if (harvestAmt < 6) {
                harvestAmt++
            }
        }

        if (plantable?.stages ?: 0 > currentGrowthStage && !isGrown()) {
            currentGrowthStage++
            setCurrentState(getCurrentState() + 1)
            isWatered = false
        }

        regrowIfTreeStump()
    }

    fun regrowIfTreeStump() {
        if (patch.type == PatchType.TREE_PATCH && plantable != null) {
            if (getCurrentState() == plantable!!.value + plantable!!.stages + 2) {
                setCurrentState(getCurrentState() - 1)
                isWatered = false
            }
        }
    }

    fun update() {
        grow()
        updateBit()
    }

    fun plant(plantable: Plantable) {
        nextGrowth =
            System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(plantable.applicablePatch.stageGrowthTime.toLong())
        this.plantable = plantable
        isDead = false
        isDiseased = false
        currentGrowthStage = 0
        setCurrentState(plantable.value)
    }

    fun clear() {
        isCheckHealth = false
        isDiseased = false
        isDead = false
        plantable = null
        setVarbit(player, patch.varbit, 0)
        nextGrowth = 0L
        currentGrowthStage = 3
        setCurrentState(3)
        compost = CompostType.NONE
        protectionPaid = false
    }

    fun getFruitOrBerryCount(): Int {
        plantable ?: return 0
        return getCurrentState() - plantable!!.value - plantable!!.stages
    }

    fun getStageGrowthMinutes(): Int {
        var minutes = patch.type.stageGrowthTime
        if (patch.type == PatchType.FRUIT_TREE_PATCH && isGrown()) {
            minutes = 40
        }
        return minutes
    }

    fun isFlowerProtected(): Boolean {
        if (patch.type != PatchType.ALLOTMENT) return false

        val fpatch =
            when (patch) {
                FarmingPatch.S_FALADOR_ALLOTMENT_SE, FarmingPatch.S_FALADOR_ALLOTMENT_NW -> FarmingPatch.S_FALADOR_FLOWER_C
                FarmingPatch.ARDOUGNE_ALLOTMENT_S, FarmingPatch.ARDOUGNE_ALLOTMENT_N -> FarmingPatch.ARDOUGNE_FLOWER_C
                FarmingPatch.CATHERBY_ALLOTMENT_S, FarmingPatch.CATHERBY_ALLOTMENT_N -> FarmingPatch.CATHERBY_FLOWER_C
                FarmingPatch.PORT_PHAS_ALLOTMENT_SE, FarmingPatch.PORT_PHAS_ALLOTMENT_NW -> FarmingPatch.PORT_PHAS_FLOWER_C
                else -> return false
            }.getPatchFor(player, false)

        return (
            fpatch.plantable != null &&
                (
                    fpatch.plantable == plantable?.protectionFlower ||
                        fpatch.plantable ==
                        Plantable.forItemID(
                            Items.WHITE_LILY_SEED_14589,
                        )
                ) &&
                fpatch.isGrown()
        )
    }
}
