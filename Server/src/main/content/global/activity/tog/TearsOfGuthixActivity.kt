package content.global.activity.tog

import content.data.GameAttributes
import core.api.*
import core.game.component.Component
import core.game.event.EventHook
import core.game.event.TickEvent
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.game.world.update.flag.context.Animation
import shared.consts.Components
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Scenery

/**
 * Tears of Guthix Activity
 * ```
 * ::setvarbit 454 // (varp 449 7-11) 0-10 where its just time bar length
 * ::setvarbit 455 // (varp 449 12-20) X where x is number of points he gets
 * ```
 * @author ovenbreado
 */
class TearsOfGuthixActivity :
    InteractionListener,
    EventHook<TickEvent>,
    MapArea {
    companion object {
        const val varbitTimeBar = 454
        const val varbitPoints = 455
        const val attributeTicksRemaining = "minigame:tearsofguthix-ticksremaining"
        const val attributeTearsCollected = "minigame:tearsofguthix-tearscollected"
        const val attributeIsCollecting = "minigame:tearsofguthix-iscollecting"

        // In order specified by RS
        private val rewardArray =
            arrayOf(
                Skills.COOKING,
                Skills.CRAFTING,
                Skills.FIREMAKING,
                Skills.FISHING,
                Skills.MAGIC,
                Skills.MINING,
                Skills.PRAYER,
                Skills.RANGE,
                Skills.RUNECRAFTING,
                Skills.SMITHING,
                Skills.WOODCUTTING,
                Skills.AGILITY,
                Skills.HERBLORE,
                Skills.FLETCHING,
                Skills.THIEVING,
                Skills.SLAYER,
                Skills.ATTACK,
                Skills.DEFENCE,
                Skills.STRENGTH,
                Skills.HITPOINTS,
                Skills.FARMING,
                Skills.CONSTRUCTION,
                Skills.HUNTER,
            )

        private val rewardText =
            arrayOf(
                "You have a brief urge to cook some food.",
                "Your fingers feel nimble and suited to delicate work.",
                "You have a brief urge to set light to something.",
                "You gain a deep understanding of the creatures of the sea.",
                "You feel the power of the runes surging through you. ",
                "You gain a deep understanding of the stones of the earth.",
                "You suddenly feel very close to the gods.",
                "Your aim improves.",
                "You gain a deep understanding of runes.",
                "You gain a deep understanding of all types of metal.",
                "You gain a deep understanding of the trees in the wood.",
                "You feel very nimble.",
                "You gain a deep understanding of all kinds of strange plants.",
                "You gain a deep understanding of wooden sticks.",
                "You feel your respect for others' property slipping away.",
                "You gain a deep understanding of many strange creatures.",
                "You feel a brief surge of aggression.",
                "You feel more able to defend yourself.",
                "Your muscles bulge.",
                "You feel very healthy.",
                "You gain a deep understanding of the cycles of nature.",
                "You feel homesick.",
                "You briefly experience the joy of the hunt.",
                "You feel at one with nature.",
            )

        private fun rewardTears(player: Player) {
            val lowestSkill =
                rewardArray.reduce { acc, curr ->

                    if (curr == Skills.CONSTRUCTION && !hasHouse(player)) {
                        acc
                    } else if (curr == Skills.HERBLORE && !isQuestComplete(player, Quests.DRUIDIC_RITUAL)) {
                        acc
                    } else if (curr == Skills.RUNECRAFTING && !isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                        acc
                    } else if (player.skills.getExperience(acc) <= player.skills.getExperience(curr)) {
                        acc
                    } else {
                        curr
                    }
                }

            var perTearXP = 60.0
            if (getStatLevel(player, lowestSkill) < 30) {
                perTearXP = (getStatLevel(player, lowestSkill) - 1) * 1.724137
                perTearXP += 10
            }

            sendMessage(player, rewardText[rewardArray.indexOf(lowestSkill)])

            val tearsCollected = getAttribute(player, attributeTearsCollected, 0)
            rewardXP(player, lowestSkill, perTearXP * tearsCollected)
        }

        fun startGame(player: Player) {
            lock(player, 15)

            player.interfaceManager.openSingleTab(Component(Components.TOG_WATER_BOWL_4))
            setAttribute(player, attributeTicksRemaining, getQuestPoints(player) + 15)
            setAttribute(player, attributeTearsCollected, 0)
            setVarbit(player, varbitTimeBar, 10)
            setVarbit(player, varbitPoints, 0)

            replaceSlot(player, EquipmentSlot.WEAPON.ordinal, Item(Items.STONE_BOWL_4704), null, Container.EQUIPMENT)
            player.appearance.setAnimations(Animation(357))
            refreshAppearance(player)

            queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        val distance = player.location.getDistance(Location(3251, 9516, 2)).toInt() + 1 // Per tick?
                        forceMove(player, player.location, Location(3251, 9516, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }

                    1 -> {
                        face(player, Location(3252, 9516, 2))
                        val junaScenery = getScenery(Location(3252, 9516, 2))
                        if (junaScenery != null) {
                            animateScenery(junaScenery, 2055)
                        }
                        return@queueScript delayScript(player, 2)
                    }

                    2 -> {
                        val distance = player.location.getDistance(Location(3253, 9516, 2)).toInt() + 1 // Per tick?
                        forceMove(player, player.location, Location(3253, 9516, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }

                    3 -> {
                        face(player, Location(3253, 9517, 2))
                        return@queueScript delayScript(player, 2)
                    }

                    4 -> {
                        val distance = player.location.getDistance(Location(3253, 9517, 2)).toInt() + 1 // Per tick?
                        forceMove(player, player.location, Location(3253, 9517, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }

                    5 -> {
                        face(player, Location(3257, 9517, 2))
                        return@queueScript delayScript(player, 2)
                    }

                    6 -> {
                        val distance = player.location.getDistance(Location(3257, 9517, 2)).toInt() + 1 // Per tick?
                        forceMove(player, player.location, Location(3257, 9517, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }

                    7 -> {
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
        }

        fun endGame(player: Player) {
            lock(player, 22)
            queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        sendMessage(player, "Your time in the cave is up.")
                        val distance = player.location.getDistance(Location(3253, 9517, 2)).toInt() + 1 // Per tick?
                        forceMove(player, player.location, Location(3253, 9517, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }

                    1 -> {
                        face(player, Location(3253, 9516, 2))
                        return@queueScript delayScript(player, 2)
                    }

                    2 -> {
                        val distance = player.location.getDistance(Location(3253, 9516, 2)).toInt() + 1 // Per tick?
                        forceMove(player, player.location, Location(3253, 9516, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }

                    3 -> {
                        face(player, Location(3251, 9516, 2))
                        val junaScenery = getScenery(Location(3252, 9516, 2))
                        if (junaScenery != null) {
                            animateScenery(junaScenery, 2055)
                        }
                        return@queueScript delayScript(player, 2)
                    }

                    4 -> {
                        val distance = player.location.getDistance(Location(3251, 9516, 2)).toInt() + 1 // Per tick?
                        forceMove(player, player.location, Location(3251, 9516, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }

                    5 -> {
                        sendMessage(player, "You drink the liquid...")
                        animate(player, 2045)
                        return@queueScript delayScript(player, 3)
                    }

                    6 -> {
                        rewardTears(player)
                        setAttribute(player, GameAttributes.QUEST_TOG_LAST_DATE, System.currentTimeMillis())
                        setAttribute(player, GameAttributes.QUEST_TOG_LAST_XP_AMOUNT, player.skills.totalXp)
                        setAttribute(player, GameAttributes.QUEST_TOG_LAST_QP, getQuestPoints(player))
                        removeAttribute(player, attributeTearsCollected)
                        if (player.interfaceManager.singleTab?.id == 4) {
                            player.interfaceManager.closeSingleTab()
                        }
                        player.interfaceManager.restoreTabs()
                        removeItem(player, Items.STONE_BOWL_4704, Container.EQUIPMENT)
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
        }
    }

    override fun defineListeners() {
        on(Scenery.WEEPING_WALL_6660, SCENERY, "collect-from") { player, node ->
            animate(player, 2043)
            val index = TearsOfGuthixListener.allWalls.indexOf(node.location)
            setAttribute(player, attributeIsCollecting, index)
            return@on true
        }
    }

    override fun process(
        entity: Entity,
        event: TickEvent,
    ) {
        if (entity is Player) {
            if (getAttribute(entity, attributeTicksRemaining, -1) > 0) {
                setAttribute(entity, attributeTicksRemaining, getAttribute(entity, attributeTicksRemaining, 0) - 1)
                setVarbit(
                    entity,
                    varbitTimeBar,
                    (getAttribute(entity, attributeTicksRemaining, 0) * 10 / getQuestPoints(entity)),
                    false,
                )
                if (getAttribute(entity, attributeIsCollecting, 0) != 0) {
                    val currentArrayIndex = getAttribute(entity, attributeIsCollecting, 0)
                    val currentTearState = TearsOfGuthixListener.globalWallState[currentArrayIndex]
                    if (currentTearState == 1) {
                        setAttribute(
                            entity,
                            attributeTearsCollected,
                            getAttribute(entity, attributeTearsCollected, 0) + 1,
                        )
                    } else if (currentTearState == 2 && getAttribute(entity, attributeTearsCollected, 0) > 0) {
                        setAttribute(
                            entity,
                            attributeTearsCollected,
                            getAttribute(entity, attributeTearsCollected, 0) - 1,
                        )
                    }
                    setVarbit(entity, varbitPoints, getAttribute(entity, attributeTearsCollected, 0))
                }
            } else if (getAttribute(entity, attributeTicksRemaining, -1) == 0) {
                removeAttribute(entity, attributeTicksRemaining)
                endGame(entity)
            }
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(3253, 9513, 3262, 9522, 2))

    override fun getRestrictions(): Array<ZoneRestriction> =
        arrayOf(
            ZoneRestriction.RANDOM_EVENTS,
            ZoneRestriction.CANNON,
            ZoneRestriction.FOLLOWERS,
            ZoneRestriction.TELEPORT,
        )

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            if (getAttribute(entity, attributeTicksRemaining, 0) <= 0) {
                removeItem(entity, Items.STONE_BOWL_4704, Container.EQUIPMENT)
                teleport(entity, Location(3251, 9516, 2))
            } else {
                entity.hook(Event.Tick, this)
            }
        }
    }

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        if (entity is Player) {
            entity.unhook(this)
            if (logout) {
                removeItem(entity, Items.STONE_BOWL_4704, Container.EQUIPMENT)
                removeAttribute(entity, attributeTearsCollected)
                removeAttribute(entity, attributeTicksRemaining)
                teleport(entity, Location(3251, 9516, 2))
            }
        }
    }

    override fun entityStep(
        entity: Entity,
        location: Location,
        lastLocation: Location,
    ) {
        if (entity is Player) {
            entity.hook(Event.Tick, this)
            setAttribute(entity, attributeIsCollecting, 0)
        }
    }
}
