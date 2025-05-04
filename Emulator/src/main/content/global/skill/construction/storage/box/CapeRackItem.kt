package content.global.skill.construction.storage.box

import org.rs.consts.Items

enum class CapeRackItem(
    val displayId: Int,
    vararg val takeId: Int
) {
    Legends_Cape (Items.CAPE_OF_LEGENDS_10635, Items.CAPE_OF_LEGENDS_1052),
    Obsidian_Cape (Items.OBSIDIAN_CAPE_10636, Items.OBSIDIAN_CAPE_6568),
    Fire_Cape (Items.FIRE_CAPE_10637, Items.FIRE_CAPE_6570),
    Team_Cape (Items.TEAM_1_CAPE_10638, Items.TEAM_10_CAPE_4333),
    Attack_Cape (Items.ATTACK_CAPE_10639, Items.ATTACK_CAPE_9747),
    Strength_Cape (Items.STRENGTH_CAPE_10640, Items.STRENGTH_CAPE_9750),
    Defence_Cape (Items.DEFENCE_CAPE_10641, Items.DEFENCE_CAPE_9753),
    Ranging_Cape (Items.RANGING_CAPE_10642, Items.RANGING_CAPE_9756),
    Prayer_Cape (Items.PRAYER_CAPE_10643, Items.PRAYER_CAPE_9759),
    Magic_Cape (Items.MAGIC_CAPE_10644, Items.MAGIC_CAPE_9762),
    Runecraft_Cape (Items.RUNECRAFT_CAPE_10645, Items.RUNECRAFT_CAPE_9765),
    Hunter_Cape (Items.HUNTER_CAPE_10646, Items.HUNTER_CAPE_9948),
    Hitpoints_Cape (Items.HITPOINTS_CAPE_10647, Items.HITPOINTS_CAPE_9768),
    Agility_Cape (Items.AGILITY_CAPE_10648, Items.AGILITY_CAPE_9771),
    Herblore_Cape (Items.HERBLORE_CAPE_10649, Items.HERBLORE_CAPE_9774),
    Thieving_Cape (Items.THIEVING_CAPE_10650, Items.THIEVING_CAPE_9777),
    Crafting_Cape (Items.CRAFTING_CAPE_10651, Items.CRAFTING_CAPE_9780),
    Fletching_Cape (Items.FLETCHING_CAPE_10652, Items.FLETCHING_CAPE_9783),
    Slayer_Cape (Items.SLAYER_CAPE_10653, Items.SLAYER_CAPE_9786),
    Construction_Cape (Items.CONSTRUCT_CAPE_10654, Items.CONSTRUCT_CAPE_9789),
    Mining_Cape (Items.MINING_CAPE_10655, Items.MINING_CAPE_9792),
    Smithing_Cape (Items.SMITHING_CAPE_10656, Items.SMITHING_CAPE_9795),
    Fishing_Cape (Items.FISHING_CAPE_10657, Items.FISHING_CAPE_9798),
    Cooking_Cape (Items.COOKING_CAPE_10658, Items.COOKING_CAPE_9801),
    Firemaking_Cape (Items.FIREMAKING_CAPE_10659, Items.FIREMAKING_CAPE_9804),
    Woodcutting_Cape (Items.WOODCUTTING_CAPE_10660, Items.WOODCUTTING_CAPE_9807),
    Farming_Cape (Items.FARMING_CAPE_10661, Items.FARMING_CAPE_9810),
    Quest_Cape (Items.QUEST_POINT_CAPE_10662, Items.QUEST_POINT_CAPE_9813),
    Spotted_Cape (Items.SPOTTED_CAPE_10663, Items.SPOTTED_CAPE_10663),
    Spottier_Cape (Items.SPOTTIER_CAPE_10664, Items.SPOTTIER_CAPE_10664);

    val labelId: Int get() = 55 +  (ordinal * 2)
    val iconId: Int get() = 165 +  (ordinal * 2)
}