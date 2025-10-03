package content.global.skill.runecrafting

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Vars

class RunecraftingListener : InteractionListener {

    private val pouchIDs = (5509..5515).toIntArray()
    private val tiara = IntArray(Tiara.values().size) { Tiara.values()[it].item.id }
    private val staves = IntArray(Staves.values().size) { Staves.values()[it].item }
    private val stavesMap = HashMap<Int, Int>()
    private val tiaraMap = HashMap<Int, Int>()

    init {
        for (i in 13630..13641) {
            stavesMap[i] = 1 shl (i - 13630)
        }
        for (index in tiara.indices) {
            tiaraMap[tiara[index]] = 1 shl index
        }
    }

    override fun defineListeners() {

        on(pouchIDs, IntType.ITEM, "fill", "empty", "check", "drop") { player, node ->
            val option = getUsedOption(player)
            val runeEssenceAmount = amountInInventory(player, Items.RUNE_ESSENCE_1436)
            val pureEssenceAmount = amountInInventory(player, Items.PURE_ESSENCE_7936)

            if (runeEssenceAmount == pureEssenceAmount && option == "fill") return@on true

            val essence = checkAmount(runeEssenceAmount, pureEssenceAmount)

            when (option) {
                "fill" -> player.pouchManager.addToPouch(node.id, essence.amount, essence.id)
                "empty" -> player.pouchManager.withdrawFromPouch(node.id)
                "check" -> player.pouchManager.checkAmount(node.id)
                "drop" -> openDialogue(player, 9878, Item(node.id))
            }

            return@on true
        }

        TalismanStaff.values().forEach { item ->
            val altar = map(item)
            altar?.let {
                onUseWith(IntType.SCENERY, item.items.id, it.scenery) { player, used, _ ->
                    setTitle(player, 2)
                    sendDialogueOptions(player, "Do you want to enchant a tiara or staff?", "Tiara.", "Staff.")
                    addDialogueAction(player) { p, button ->
                        if (button == 2 && !inInventory(p, Items.TIARA_5525, 1)) {
                            return@addDialogueAction sendMessage(p, "You need a tiara.")
                        }
                        if (button == 3 && !inInventory(p, Items.RUNECRAFTING_STAFF_13629, 1)) {
                            return@addDialogueAction sendMessage(p, "You need a runecrafting staff.")
                        }
                        enchant(p, used.asItem(), button, item)
                    }
                    return@onUseWith true
                }
            }
        }

        onEquip(tiara + staves) { player, n ->
            val num = tiaraMap[n.id] ?: stavesMap[n.id] ?: 0
            setVarp(player, Vars.VARP_SCENERY_ABYSS_491, num)
            return@onEquip true
        }

        onUnequip(tiara + staves) { player, _ ->
            setVarp(player, Vars.VARP_SCENERY_ABYSS_491, 0)
            return@onUnequip true
        }
    }

    fun map(staff: TalismanStaff): Altar? =
        when (staff) {
            TalismanStaff.AIR -> Altar.AIR
            TalismanStaff.MIND -> Altar.MIND
            TalismanStaff.WATER -> Altar.WATER
            TalismanStaff.EARTH -> Altar.EARTH
            TalismanStaff.FIRE -> Altar.FIRE
            TalismanStaff.BODY -> Altar.BODY
            TalismanStaff.COSMIC -> Altar.COSMIC
            TalismanStaff.CHAOS -> Altar.CHAOS
            TalismanStaff.NATURE -> Altar.NATURE
            TalismanStaff.LAW -> Altar.LAW
            TalismanStaff.DEATH -> Altar.DEATH
            TalismanStaff.BLOOD -> Altar.BLOOD
            else -> null
        }

    private fun checkAmount(
        runeEssenceAmount: Int,
        pureEssenceAmount: Int,
    ): Item {
        val isRunePreferred = runeEssenceAmount >= pureEssenceAmount
        val id = if (isRunePreferred) Items.RUNE_ESSENCE_1436 else Items.PURE_ESSENCE_7936
        val amount = if (isRunePreferred) runeEssenceAmount else pureEssenceAmount

        return Item(id, amount)
    }

    private fun enchant(
        player: Player,
        itemId: Item,
        buttonId: Int,
        product: TalismanStaff,
    ) {
        closeDialogue(player)
        removeItem(player, if (buttonId == 3) Items.RUNECRAFTING_STAFF_13629 else Items.TIARA_5525)
        replaceSlot(player, itemId.index, if (buttonId == 3) Item(product.staves.item, 1) else Item(product.tiara, 1))
        rewardXP(player, Skills.RUNECRAFTING, product.staves.experience)
        sendMessage(player, "You bind the power of the talisman into your ${if (buttonId == 3) "staff" else "tiara"}.")
    }
}
