package content.global.skill.construction.storage.box

import org.rs.consts.Items

enum class BookcaseItem (val takeId: Int) {
    Arena_Book (Items.ARENA_BOOK_6891),
    My_Notes (Items.MY_NOTES_11339),
    Crumbling_Tome (Items.CRUMBLING_TOME_4707),
    Pie_Recipe_Book (Items.PIE_RECIPE_BOOK_7162),
    Giannes_Cook_Book (Items.GIANNES_COOK_BOOK_2167),
    Game_Book (Items.GAME_BOOK_7681),
    Stronghold_Notes (Items.STRONGHOLD_NOTES_9004),
    Cocktail_Guide (Items.COCKTAIL_GUIDE_2023),
    Tarns_Diary (Items.TARNS_DIARY_10587),
    Construction_Guide (Items.CONSTRUCTION_GUIDE_8463),
    Glassblowing_Book (Items.GLASSBLOWING_BOOK_11656),
    Brewin_Guide (Items.BREWIN_GUIDE_8989),
    Security_Book (Items.SECURITY_BOOK_9003),
    Queen_Help_Book (Items.QUEEN_HELP_BOOK_10562),
    Abyssal_Book (Items.ABYSSAL_BOOK_5520),
    Explorers_Notes (Items.EXPLORERS_NOTES_11677),
    Goblin_Book (Items.GOBLIN_BOOK_10999),
    Dwarven_Lore (Items.DWARVEN_LORE_4568),
    Book_O_Piracy (Items.BOOK_O_PIRACY_7144),
    Clockwork_Book (Items.CLOCKWORK_BOOK_10594),
    Scabarite_Notes (Items.SCABARITE_NOTES_11975),
    Translation (Items.TRANSLATION_4655),
    Book_on_Chemicals (Items.BOOK_ON_CHEMICALS_711),
    Instruction_Manual (Items.INSTRUCTION_MANUAL_5),
    Bird_Book (Items.BIRD_BOOK_10173),
    Feathered_Journal (Items.FEATHERED_JOURNAL_10179),
    Battered_Book (Items.BATTERED_BOOK_2886),
    Beaten_Book (Items.BEATEN_BOOK_9717),
    A_Handwritten_Book (Items.A_HANDWRITTEN_BOOK_9627),
    Varmens_Notes (Items.VARMENS_NOTES_4616);

    val labelId: Int get() = 55 +  (ordinal * 2)
}