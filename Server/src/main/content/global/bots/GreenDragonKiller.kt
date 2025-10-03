package content.global.bots

import core.game.bots.*
import core.game.interaction.DestinationFlag
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.interaction.MovementPulse
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.InteractionType
import core.game.node.entity.combat.MeleeSwingHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.impl.WildernessZone
import core.tools.RandomFunction
import kotlin.random.Random
import shared.consts.Items

class GreenDragonKiller(val style: CombatStyle) : Script() {

    companion object {
        val westDragons = ZoneBorders(2971, 3606, 2991, 3628)
        val wildernessLine = ZoneBorders(3078, 3523, 3096, 3523)
        val edgevilleLine = ZoneBorders(3078, 3520, 3096, 3520)
        val bankZone = ZoneBorders(3092, 3489, 3094, 3493)
        val trashTalkLines = arrayOf(
            "Bro, seriously?", "Ffs.", "Jesus christ.", "????", "Friendly!", "Get a life dude",
            "Do you mind??? lol", "Lol.", "Kek.", "One sec burying all the bones.", "Yikes.", "Yeet",
            "Ah shit, here we go again.", "Cmonnnn", "Plz", "Do you have nothing better to do?",
            "Cmon bro pls", "I just need to get my prayer up bro jesus", "Reeeeeee", "I cant believe you've done this",
            "Really m8", "Zomg", "Aaaaaaaaaaaaaaaaaaaaa", "Rofl.", "Oh god oh fuck oh shit", "....", ":|",
            "A q p", "Hcim btw", "I hope the revenants kill your mum", "Wrap your ass titties", "Why do this",
            "Bruh", "Straight sussin no cap fr fr", "This ain't bussin dawg", "Really bro?"
        )
    }

    enum class State {
        KILLING, RUNNING, LOOTING, LOOT_DELAYER, BANKING, TO_BANK, TO_DRAGONS, TO_GE,
        SELL_GE, REFRESHING, BUYING_FOOD
    }

    var state = State.TO_BANK
    var handler: CombatSwingHandler? = null
    var lootDelay = 0
    var trashTalkDelay = 0
    var offerMade = false
    var avoidPlayer = false
    var food =
        if (Random.nextBoolean()) Items.LOBSTER_379
        else if (Random.nextBoolean()) Items.SWORDFISH_373 else Items.SHARK_385
    var myBorders: ZoneBorders? = westDragons
    val type = CombatBotAssembler.Type.MELEE

    init {
        handler = MeleeSwinger(this)
        equipment.add(Item(Items.ANTI_DRAGON_SHIELD_1540))
        myBorders = westDragons
        skills[Skills.AGILITY] = 99
        bankZone.addException(ZoneBorders(3094, 3492, 3094, 3492))
        bankZone.addException(ZoneBorders(3094, 3490, 3094, 3490))
    }

    override fun tick() {
        if (!bot.isActive || avoidPlayer) state = State.RUNNING

        checkFoodStockAndEat()
        sendTrashTalk()
        attemptToBuryBone()

        when (state) {
            State.KILLING -> killingState()
            State.LOOT_DELAYER -> lootDelayerState()
            State.LOOTING -> lootingState()
            State.RUNNING -> runningState()
            State.TO_BANK -> toBankState()
            State.BANKING -> bankingState()
            State.BUYING_FOOD -> buyingFoodState()
            State.TO_DRAGONS -> toDragonsState()
            State.TO_GE -> toGeState()
            State.SELL_GE -> sellGeState()
            State.REFRESHING -> running = false
        }
    }

    private fun killingState() {
        bot.properties.combatPulse.temporaryHandler = handler
        val attacked = scriptAPI.attackNpcInRadius(bot, "Green dragon", 20)
        if (attacked) state = State.LOOT_DELAYER
    }

    private fun lootDelayerState() {
        if (lootDelay++ >= 3) {
            lootDelay = 0
            state = State.LOOTING
        }
    }

    private fun lootingState() {
        val items = AIRepository.groundItems[bot]
        if (items.isNullOrEmpty()) {
            state = State.KILLING
            return
        }
        if (bot.inventory.isFull) {
            if (bot.inventory.containsItem(Item(food))) scriptAPI.forceEat(food)
            else state = State.TO_BANK
            return
        }
        items.forEach { scriptAPI.takeNearestGroundItem(it.id) }
        state = State.KILLING
    }

    private fun runningState() {
        val players = RegionManager.getLocalPlayers(bot.location)
        if (players.isEmpty()) {
            state = State.TO_DRAGONS
            avoidPlayer = false
            return
        }
        if (bot.skullManager.level < 21 && scriptAPI.teleportToGE()) {
            state = State.REFRESHING
            return
        }
        val targetLoc = WildernessZone.instance.borders.random().randomLoc
        bot.pulseManager.run(object : MovementPulse(bot, targetLoc, DestinationFlag.LOCATION) {
            override fun pulse(): Boolean = true
        })
    }

    private fun toBankState() {
        if (!wildernessLine.insideBorder(bot) && bot.location.y > 3521) {
            crossWildernessDitch()
            return
        }

        if (!bankZone.insideBorder(bot)) {
            val target = bankZone.randomLoc
            bot.pulseManager.run(object : MovementPulse(bot, target, DestinationFlag.LOCATION) {
                override fun pulse(): Boolean {
                    state = if (bankZone.insideBorder(bot)) State.BANKING else State.TO_BANK
                    return true
                }
            })
        } else state = State.BANKING
    }

    private fun crossWildernessDitch() {
        val ditch = scriptAPI.getNearestNode("Wilderness Ditch", true) ?: return
        bot.pulseManager.run(object : MovementPulse(bot, ditch, DestinationFlag.OBJECT) {
            override fun pulse(): Boolean {
                bot.faceLocation(ditch.location)
                InteractionListeners.run(ditch.id, IntType.SCENERY, "cross", bot, ditch.asScenery())
                state = State.TO_BANK
                return true
            }
        })
    }

    private fun bankingState() {
        val bank = scriptAPI.getNearestNode("Bank Booth", true) ?: return
        bot.pulseManager.run(object : MovementPulse(bot, bank, DestinationFlag.OBJECT) {
            override fun pulse(): Boolean {
                bot.faceLocation(bank.location)
                bot.inventory.toArray().filterNotNull().forEach {
                    if (it.id != food && it.id != 995) bot.bank.add(it)
                }
                bot.inventory.clear()
                scriptAPI.withdraw(food, 10)
                bot.fullRestore()
                state = State.TO_DRAGONS
                return true
            }
        })
    }

    private fun buyingFoodState() {
        bot.bank.add(Item(food, 50))
        bot.bank.refresh()
        scriptAPI.withdraw(food, 10)
        state = State.TO_DRAGONS
    }

    private fun toDragonsState() {
        val runTo = Location.create(3085, 3520, 0)
        if (bot.location != runTo) {
            bot.pulseManager.run(object : MovementPulse(bot, runTo, DestinationFlag.LOCATION) {
                override fun pulse(): Boolean {
                    state = State.TO_DRAGONS
                    return true
                }
            })
            return
        }

        if (myBorders?.insideBorder(bot) == true) {
            state = State.KILLING
            return
        }

        val target = myBorders?.randomLoc ?: return
        bot.pulseManager.run(object : MovementPulse(bot, target, DestinationFlag.LOCATION) {
            override fun pulse(): Boolean {
                state = State.KILLING
                return true
            }
        })
    }

    private fun toGeState() {
        scriptAPI.teleportToGE()
        state = State.SELL_GE
    }

    private fun sellGeState() {
        scriptAPI.sellAllOnGe()
        state = State.BUYING_FOOD
    }

    private fun attemptToBuryBone() {
        if (bot.inventory.containsAtLeastOneItem(Items.DRAGON_BONES_536)) {
            InteractionListeners.run(
                Items.DRAGON_BONES_536, IntType.ITEM, "bury",
                bot, bot.inventory.get(Item(Items.DRAGON_BONES_536))
            )
        }
    }

    private fun checkFoodStockAndEat() {
        if (bot.inventory.getAmount(food) < 3 && state == State.KILLING) state = State.TO_BANK
        scriptAPI.eat(food)
    }

    private fun sendTrashTalk() {
        if (trashTalkDelay-- <= 0) {
            scriptAPI.sendChat(trashTalkLines.random())
            trashTalkDelay = RandomFunction.random(10, 30)
        }
    }

    override fun newInstance(): Script {
        val script = GreenDragonKiller(style)
        val tier = CombatBotAssembler.Tier.MED
        script.bot = CombatBotAssembler().assembleMeleeDragonBot(tier, bot.startLocation)
        return script
    }

    internal class MeleeSwinger(val script: GreenDragonKiller) : MeleeSwingHandler() {
        override fun canSwing(entity: Entity, victim: Entity): InteractionType? {
            if (victim is Player || victim.name.contains("revenant", true)) {
                script.avoidPlayer = true
                script.bot.pulseManager.clear()
            }
            return super.canSwing(entity, victim)
        }
    }
}
