package content.data.items

import core.game.node.item.Item
import core.game.node.item.WeightedChanceItem
import core.tools.RandomFunction
import shared.consts.Items

/**
 * The [BrokenItem] object manages the logic of retrieving damaged
 * or broken items that can be repaired.
 */
object BrokenItem {
    /*
     * List of broken arrows with their respective weighted chances.
     */
    private val brokenArrows = ArrayList<WeightedChanceItem>()

    init {
        brokenArrows.add(WeightedChanceItem(Items.BRONZE_ARROW_882, 1, 40))
        brokenArrows.add(WeightedChanceItem(Items.IRON_ARROW_884, 1, 30))
        brokenArrows.add(WeightedChanceItem(Items.STEEL_ARROW_886, 1, 20))
        brokenArrows.add(WeightedChanceItem(Items.MITHRIL_ARROW_888, 1, 10))
    }

    /*
     * List of broken staves with their respective weighted chances.
     */
    private val brokenStaves = ArrayList<WeightedChanceItem>()

    init {
        brokenStaves.add(WeightedChanceItem(Items.STAFF_OF_AIR_1381, 1, 25))
        brokenStaves.add(WeightedChanceItem(Items.STAFF_OF_WATER_1383, 1, 25))
        brokenStaves.add(WeightedChanceItem(Items.STAFF_OF_EARTH_1385, 1, 25))
        brokenStaves.add(WeightedChanceItem(Items.STAFF_OF_FIRE_1387, 1, 25))
    }

    /*
     * List of rusty swords with their respective weighted chances.
     */
    private val rustySwords = ArrayList<WeightedChanceItem>()

    init {
        rustySwords.add(WeightedChanceItem(Items.BRONZE_SWORD_1277, 1, 12))
        rustySwords.add(WeightedChanceItem(Items.BRONZE_LONGSWORD_1291, 1, 12))
        rustySwords.add(WeightedChanceItem(Items.IRON_SWORD_1279, 1, 11))
        rustySwords.add(WeightedChanceItem(Items.IRON_LONGSWORD_1293, 1, 11))
        rustySwords.add(WeightedChanceItem(Items.STEEL_SWORD_1281, 1, 10))
        rustySwords.add(WeightedChanceItem(Items.STEEL_LONGSWORD_1295, 1, 10))
        rustySwords.add(WeightedChanceItem(Items.BLACK_SWORD_1283, 1, 9))
        rustySwords.add(WeightedChanceItem(Items.BLACK_LONGSWORD_1297, 1, 9))
        rustySwords.add(WeightedChanceItem(Items.MITHRIL_SWORD_1285, 1, 8))
        rustySwords.add(WeightedChanceItem(Items.MITHRIL_LONGSWORD_1299, 1, 8))
    }

    /*
     * List of rusty scimitars with their respective weighted chances.
     */
    private val rustyScimitars = ArrayList<WeightedChanceItem>()

    init {
        rustyScimitars.add(WeightedChanceItem(Items.BRONZE_SCIMITAR_1321, 1, 12))
        rustyScimitars.add(WeightedChanceItem(Items.IRON_SCIMITAR_1323, 1, 11))
        rustyScimitars.add(WeightedChanceItem(Items.STEEL_SCIMITAR_1325, 1, 10))
        rustyScimitars.add(WeightedChanceItem(Items.BLACK_SCIMITAR_1327, 1, 9))
        rustyScimitars.add(WeightedChanceItem(Items.MITHRIL_SCIMITAR_1329, 1, 8))
    }

    /*
     * List of damaged armor items with their respective weighted chances.
     */
    private val damagedArmour = ArrayList<WeightedChanceItem>()

    init {
        damagedArmour.add(WeightedChanceItem(Items.BRONZE_PLATEBODY_1117, 1, 40))
        damagedArmour.add(WeightedChanceItem(Items.IRON_PLATEBODY_1115, 1, 30))
        damagedArmour.add(WeightedChanceItem(Items.STEEL_PLATEBODY_1119, 1, 15))
        damagedArmour.add(WeightedChanceItem(Items.BLACK_PLATEBODY_1125, 1, 10))
        damagedArmour.add(WeightedChanceItem(Items.MITHRIL_PLATEBODY_1121, 1, 5))
    }

    /*
     * List of broken armor items with their respective weighted chances.
     */
    private val brokenArmour = ArrayList<WeightedChanceItem>()

    init {
        brokenArmour.add(WeightedChanceItem(Items.BRONZE_PLATELEGS_1075, 1, 40))
        brokenArmour.add(WeightedChanceItem(Items.IRON_PLATELEGS_1067, 1, 30))
        brokenArmour.add(WeightedChanceItem(Items.STEEL_PLATELEGS_1069, 1, 15))
        brokenArmour.add(WeightedChanceItem(Items.BLACK_PLATELEGS_1077, 1, 10))
        brokenArmour.add(WeightedChanceItem(Items.MITHRIL_PLATELEGS_1071, 1, 5))
    }

    /**
     * Returns a repaired item of the given equipment type, or null if none found.
     *
     * @param type The equipment type to repair.
     * @return The repaired item, or null if not found.
     */
    @JvmStatic
    fun getRepair(type: EquipmentType): Item? =
        when (type) {
            EquipmentType.ARROWS -> RandomFunction.rollWeightedChanceTable(brokenArrows)
            EquipmentType.STAVES -> RandomFunction.rollWeightedChanceTable(brokenStaves)
            EquipmentType.SWORDS -> RandomFunction.rollWeightedChanceTable(rustySwords)
            EquipmentType.SCIMITARS -> RandomFunction.rollWeightedChanceTable(rustyScimitars)
            EquipmentType.ARMOUR -> RandomFunction.rollWeightedChanceTable(damagedArmour)
            EquipmentType.LEGS -> RandomFunction.rollWeightedChanceTable(brokenArmour)
        }

    /**
     * Represents the different types of equipment that can be repaired.
     */
    enum class EquipmentType {
        ARROWS,
        STAVES,
        SWORDS,
        SCIMITARS,
        ARMOUR,
        LEGS,
    }
}
