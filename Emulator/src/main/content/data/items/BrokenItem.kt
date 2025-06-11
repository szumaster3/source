package content.data.items

import core.game.node.item.Item
import core.game.node.item.WeightedChanceItem
import core.tools.RandomFunction
import org.rs.consts.Items

/**
 * The [BrokenItem] object manages the logic of retrieving damaged
 * or broken items that can be repaired.
 */
object BrokenItem {
    /*
     * List of broken arrows with their respective weighted chances.
     */
    private val brokenArrows = listOf(
        WeightedChanceItem(Items.BRONZE_ARROW_882, 1, 40),
        WeightedChanceItem(Items.IRON_ARROW_884, 1, 30),
        WeightedChanceItem(Items.STEEL_ARROW_886, 1, 20),
        WeightedChanceItem(Items.MITHRIL_ARROW_888, 1, 10),
    )
    /*
     * List of broken staves with their respective weighted chances.
     */
    private val brokenStaves = listOf(
        WeightedChanceItem(Items.STAFF_OF_AIR_1381, 1, 25),
        WeightedChanceItem(Items.STAFF_OF_WATER_1383, 1, 25),
        WeightedChanceItem(Items.STAFF_OF_EARTH_1385, 1, 25),
        WeightedChanceItem(Items.STAFF_OF_FIRE_1387, 1, 25),
    )
    /*
     * List of rusty swords with their respective weighted chances.
     */
    private val rustySwords = listOf(
        WeightedChanceItem(Items.BRONZE_SWORD_1277, 1, 12),
        WeightedChanceItem(Items.BRONZE_LONGSWORD_1291, 1, 12),
        WeightedChanceItem(Items.IRON_SWORD_1279, 1, 11),
        WeightedChanceItem(Items.IRON_LONGSWORD_1293, 1, 11),
        WeightedChanceItem(Items.STEEL_SWORD_1281, 1, 10),
        WeightedChanceItem(Items.STEEL_LONGSWORD_1295, 1, 10),
        WeightedChanceItem(Items.BLACK_SWORD_1283, 1, 9),
        WeightedChanceItem(Items.BLACK_LONGSWORD_1297, 1, 9),
        WeightedChanceItem(Items.MITHRIL_SWORD_1285, 1, 8),
        WeightedChanceItem(Items.MITHRIL_LONGSWORD_1299, 1, 8),
    )
    /*
     * List of rusty scimitars with their respective weighted chances.
     */
    private val rustyScimitars = listOf(
        WeightedChanceItem(Items.BRONZE_SCIMITAR_1321, 1, 12),
        WeightedChanceItem(Items.IRON_SCIMITAR_1323, 1, 11),
        WeightedChanceItem(Items.STEEL_SCIMITAR_1325, 1, 10),
        WeightedChanceItem(Items.BLACK_SCIMITAR_1327, 1, 9),
        WeightedChanceItem(Items.MITHRIL_SCIMITAR_1329, 1, 8),
    )
    /*
     * List of damaged armor items with their respective weighted chances.
     */
    private val damagedArmour = listOf(
        WeightedChanceItem(Items.BRONZE_PLATEBODY_1117, 1, 40),
        WeightedChanceItem(Items.IRON_PLATEBODY_1115, 1, 30),
        WeightedChanceItem(Items.STEEL_PLATEBODY_1119, 1, 15),
        WeightedChanceItem(Items.BLACK_PLATEBODY_1125, 1, 10),
        WeightedChanceItem(Items.MITHRIL_PLATEBODY_1121, 1, 5),
    )
    /*
     * List of broken armor items with their respective weighted chances.
     */
    private val brokenArmour = listOf(
        WeightedChanceItem(Items.BRONZE_PLATELEGS_1075, 1, 40),
        WeightedChanceItem(Items.IRON_PLATELEGS_1067, 1, 30),
        WeightedChanceItem(Items.STEEL_PLATELEGS_1069, 1, 15),
        WeightedChanceItem(Items.BLACK_PLATELEGS_1077, 1, 10),
        WeightedChanceItem(Items.MITHRIL_PLATELEGS_1071, 1, 5),
    )

    private val repairMap = mapOf(
        EquipmentType.ARROWS to brokenArrows,
        EquipmentType.STAVES to brokenStaves,
        EquipmentType.SWORDS to rustySwords,
        EquipmentType.SCIMITARS to rustyScimitars,
        EquipmentType.ARMOUR to damagedArmour,
        EquipmentType.LEGS to brokenArmour,
    )

    /**
     * Gets a repaired item for the given equipment type.
     */
    fun getRepair(type: EquipmentType): Item? =
        repairMap[type]?.let { RandomFunction.rollWeightedChanceTable(it) }

    /**
     * Types of equipment that can be repaired.
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
