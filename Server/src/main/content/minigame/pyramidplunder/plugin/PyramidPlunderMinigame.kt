package content.minigame.pyramidplunder.plugin

import content.minigame.pyramidplunder.npc.PyramidPlunderMummyNPC
import content.minigame.pyramidplunder.npc.PyramidPlunderSwarmNPC
import core.api.*
import core.api.applyPoison
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class PyramidPlunderMinigame :
    InteractionListener,
    TickListener,
    LogoutListener,
    MapArea {
    override fun tick() {
        val playersToExpel = PlunderUtils.decrementTimeRemaining()
        playersToExpel.forEach { player -> PlunderUtils.expel(player, false) }
        PlunderUtils.checkEntranceSwitch()
    }

    override fun logout(player: Player) {
        if (PlunderUtils.hasPlayer(player)) {
            player.location = Location.create(3288, 2802, 0)
            PlunderUtils.unregisterPlayer(player)
        }
    }

    override fun defineListeners() {
        val checkAnim = 3572
        val checkUrnAnim = 4340
        val urnBitAnim = 4341
        val urnSuccessAnim = 4342
        val pushLidStartAnim = 4343
        val pushLidLoopAnim = 4344
        val pushLidFinishAnim = 4345
        val charmAnim = 1877

        val spearObjAnim = 459
        val snakeUrnAnim = 4335

        val sealedUrn = intArrayOf(Scenery.URN_16501, Scenery.URN_16502, Scenery.URN_16503)
        val openUrn = intArrayOf(Scenery.URN_16505, Scenery.URN_16506, Scenery.URN_16507)
        val snakeUrn = intArrayOf(Scenery.URN_16509, Scenery.URN_16510, Scenery.URN_16511)
        val charmedSnake = intArrayOf(Scenery.URN_16513, Scenery.URN_16514, Scenery.URN_16515)
        val entranceId = intArrayOf(16484, 16487, 16490, 16493)
        val sarcophagusId = Scenery.SARCOPHAGUS_16495

        on(Scenery.SPEARTRAP_16517, IntType.SCENERY, "pass") { player, node ->
            val anim = Animation(checkAnim)
            val duration = animationDuration(anim)

            if (getStatLevel(player, Skills.THIEVING) < PlunderUtils.getRoomLevel(player)) {
                sendDialogue(player, "You need a Thieving level of ${PlunderUtils.getRoomLevel(player)} to do that.")
                return@on true
            }

            val spearDir = PlunderUtils.getRoom(player)!!.spearDirection

            val pastSpears =
                (spearDir == Direction.NORTH && player.location.y > node.location.y) ||
                        (spearDir == Direction.SOUTH && player.location.y < node.location.y) ||
                        (spearDir == Direction.EAST && player.location.x > node.location.x) ||
                        (spearDir == Direction.WEST && player.location.x < node.location.x)

            if (pastSpears) {
                sendDialogue(player, "I have no reason to do that.")
                return@on true
            }

            animate(player, anim)
            sendMessage(player, "You carefully try to temporarily deactivate the trap mechanism.")
            submitIndividualPulse(
                player,
                object : Pulse(duration) {
                    override fun pulse(): Boolean {
                        if (RandomFunction.roll(5)) {
                            val dest = PlunderUtils.getSpearDestination(player)
                            player.walkingQueue.reset()
                            player.walkingQueue.addPath(dest.x, dest.y)
                            rewardXP(player, Skills.THIEVING, 10.0)
                            sendMessage(player, "You deactivate the trap!")
                            return true
                        }
                        if (RandomFunction.roll(20)) {
                            animateScenery(node.asScenery(), spearObjAnim)
                            impact(player, RandomFunction.random(1, 5))
                            sendChat(player, "Ouch!")
                            sendMessage(player, "You fail to disable the trap.")
                            return true
                        }
                        animate(player, anim)
                        return false
                    }
                },
            )
            return@on true
        }

        on(intArrayOf(*sealedUrn, *snakeUrn), IntType.SCENERY, "search") { player, node ->
            animate(player, checkUrnAnim)
            player.faceLocation(node.location)
            lock(player, 1)
            queueScript(player, 1, QueueStrength.SOFT) {
                if (!PlunderUtils.rollUrnSuccess(player)) {
                    animate(player, urnBitAnim)
                    sendMessage(player, "You've been poisoned by the snake bite.")
                    impact(player, RandomFunction.random(1, 5))
                    sendChat(player, "Ow!")
                    applyPoison(player, player, 2)
                } else {
                    animate(player, urnSuccessAnim)
                    sendMessage(player, "You successfully loot the urn!")
                    rewardXP(player, Skills.THIEVING, PlunderUtils.getUrnXp(player, false))
                    addItemOrDrop(player, PlunderUtils.rollArtifact(player, 1))
                    setVarbit(player, node.asScenery().definition.varbitID, 1)
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(sealedUrn, IntType.SCENERY, "check for snakes") { player, node ->
            val urn = node.asScenery()
            animate(player, checkUrnAnim)
            player.faceLocation(node.location)
            lock(player, 1)
            queueScript(player, 1, QueueStrength.SOFT) {
                animate(player, urnBitAnim)
                setVarbit(player, urn.definition.varbitID, 2)
                animateScenery(urn, snakeUrnAnim)
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(snakeUrn, IntType.SCENERY, "charm snake") { player, node ->
            if (!inInventory(player, Items.SNAKE_CHARM_4605)) {
                sendMessage(player, "You need a snake charm to charm a snake.")
                return@on true
            }
            lock(player, 2)
            animate(player, charmAnim)
            queueScript(player, 1, QueueStrength.SOFT) {
                setVarbit(player, node.asScenery().definition.varbitID, 3)
                sendMessage(player, "You charm the snake with your music.")
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(charmedSnake, IntType.SCENERY, "search") { player, node ->
            animate(player, checkUrnAnim)
            player.faceLocation(node.location)
            lock(player, 1)
            queueScript(player, 1, QueueStrength.SOFT) {
                if (!PlunderUtils.rollUrnSuccess(player, true)) {
                    animate(player, urnBitAnim)
                    sendMessage(player, "You've been poisoned by something moving inside the urn.")
                    impact(player, RandomFunction.random(1, 5))
                    sendChat(player, "Ow!")
                    applyPoison(player, player, 2)
                } else {
                    animate(player, urnSuccessAnim)
                    sendMessage(player, "You successfully loot the urn.")
                    addItemOrDrop(player, PlunderUtils.rollArtifact(player, 1))
                    rewardXP(player, Skills.THIEVING, PlunderUtils.getUrnXp(player, false) * 0.66)
                    setVarbit(player, node.asScenery().definition.varbitID, 1)
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(openUrn, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You've already looted this urn.")
            return@on true
        }

        on(sarcophagusId, IntType.SCENERY, "open") { player, node ->
            if (PlunderUtils.getRoom(player) == null) {
                PlunderUtils.expel(player, false)
                return@on true
            }
            sendMessage(player, "You attempt to push open the massive lid.")
            val strength = getDynLevel(player, Skills.STRENGTH)
            animate(player, pushLidStartAnim)
            submitIndividualPulse(
                player,
                object : Pulse(2) {
                    override fun pulse(): Boolean {
                        animate(player, pushLidLoopAnim)
                        if (RandomFunction.random(125) > strength) return false
                        animate(player, pushLidFinishAnim)
                        if (PlunderUtils.getRoom(player) == null) {
                            PlunderUtils.expel(player, false)
                            return true
                        }
                        runTask(player, 3) {
                            setVarbit(player, node.asScenery().definition.varbitID, 1)
                            rewardXP(player, Skills.STRENGTH, PlunderUtils.getSarcophagusXp(player))
                            if (RandomFunction.roll(25)) {
                                val mummy =
                                    PyramidPlunderMummyNPC(
                                        player.location,
                                        player,
                                    )
                                mummy.isRespawn = false
                                mummy.init()
                                mummy.attack(player)
                            } else if (!PlunderUtils.rollSceptre(player)) {
                                addItemOrDrop(player, PlunderUtils.rollArtifact(player, 2))
                            }
                        }
                        return true
                    }
                },
            )
            return@on true
        }

        on(Scenery.GRAND_GOLD_CHEST_16473, IntType.SCENERY, "search") { player, node ->
            if (PlunderUtils.getRoom(player) == null) {
                PlunderUtils.expel(player, false)
                return@on true
            }
            animate(player, Animations.HUMAN_OPEN_CHEST_536)
            runTask(player) {
                if (RandomFunction.roll(25)) {
                    val swarm =
                        PyramidPlunderSwarmNPC(
                            player.location,
                            player,
                        )
                    swarm.isRespawn = false
                    swarm.init()
                    swarm.attack(player)
                    impact(player, RandomFunction.random(1, 5))
                } else {
                    rewardXP(player, Skills.THIEVING, PlunderUtils.getChestXp(player))
                    if (!PlunderUtils.rollSceptre(player)) addItemOrDrop(player, PlunderUtils.rollArtifact(player, 3))
                }
                setVarbit(player, node.asScenery().definition.varbitID, 1)
            }
            return@on true
        }

        on(Scenery.TOMB_DOOR_16475, IntType.SCENERY, "pick-lock") { player, node ->
            val anim = Animation(checkAnim)
            val duration = animationDuration(anim)

            if (PlunderUtils.getRoom(player)!!.room == 8) {
                sendMessage(player, "This is the final room. I should probably just leave instead.")
                return@on true
            }

            animate(player, anim)
            lock(player, duration)
            val rate =
                if (inInventory(player, Items.LOCKPICK_1523)) {
                    sendMessage(player, "You use your lockpick and attempt to open the door.")
                    2
                } else {
                    sendMessage(player, "You attempt to open the door. Lockpicks would make it easier...")
                    3
                }
            runTask(player, duration) {
                if (RandomFunction.roll(rate)) {
                    val varbitId = node.asScenery().definition.varbitID
                    val door = PlunderUtils.getDoor(player)

                    if (door == -1) {
                        PlunderUtils.expel(player, false)
                    } else {
                        rewardXP(player, Skills.THIEVING, PlunderUtils.getDoorXp(player, rate == 3))
                    }

                    if (door == varbitId) {
                        PlunderUtils.loadNextRoom(player)
                        PlunderUtils.resetObjectVarbits(player)
                    } else {
                        when (getVarbit(player, varbitId)) {
                            0 ->
                                sendMessage(player, "The door leads to a dead end.").also {
                                    setVarbit(player, varbitId, 1)
                                }

                            else -> sendMessage(player, "You've already opened this door and it leads to a dead end.")
                        }
                    }
                } else {
                    sendMessage(player, "Your attempt fails.")
                }
            }
            return@on true
        }

        on(Scenery.TOMB_DOOR_16458, IntType.SCENERY, "leave tomb") { player, _ ->
            val outsidePyramidLocation = Location.create(3288, 2801, 0)
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 -> sendDialogue(player, "Do you really want to leave the Tomb?").also { stage++ }
                            1 -> {
                                setTitle(player, 2)
                                sendDialogueOptions(player, "Leave the Tomb?", "Yes, I'm out of here.", "Ah, I think I'll stay a little longer.").also { stage++ }
                            }
                            2 -> when (buttonID) {
                                    1 -> {
                                        end()
                                        teleport(player, outsidePyramidLocation)
                                        PlunderUtils.unregisterPlayer(player)
                                        PlunderUtils.resetOverlay(player)
                                        PlunderUtils.resetObjectVarbits(player)
                                    }
                                    2 -> end()
                                }
                        }
                    }
                },
            )
            return@on true
        }

        on(entranceId, IntType.SCENERY, "search") { player, door ->
            if (!getAttribute(player, "tarik-spoken-to", false)) {
                sendDialogue(
                    player,
                    "I should probably try to find out more about this place before I try to break in.",
                )
                return@on true
            }
            val anim = Animation(checkAnim)
            val duration = animationDuration(anim)

            animate(player, anim)
            sendMessage(player, "You use your thieving skills to search the stone panel.")
            player.pulseManager.run(
                object : Pulse(duration) {
                    override fun pulse(): Boolean {
                        if (RandomFunction.roll(3)) {
                            setAttribute(player, "pyramid-entrance", player.location.transform(0, 0, 0))
                            sendMessage(player, "You find a door! You open it.")
                            if (PlunderUtils.checkEntrance(door)) {
                                teleport(player, GUARDIAN_ROOM)
                                rewardXP(player, Skills.THIEVING, 20.0)
                            } else {
                                teleport(player, EMPTY_ROOM)
                            }

                            return true
                        }
                        animate(player, anim)
                        return false
                    }
                },
            )
            return@on true
        }

        on(Scenery.TOMB_DOOR_16459, IntType.SCENERY, "leave tomb") { player, _ ->
            teleport(player, getAttribute(player, "pyramid-entrance", Location.create(3288, 2801, 0)))
            return@on true
        }

        on(NPCs.GUARDIAN_MUMMY_4476, IntType.NPC, "start-minigame") { player, _ ->
            if (!getAttribute(player, "pp:mummy-spoken-to", false)) {
                openDialogue(player, NPCs.GUARDIAN_MUMMY_4476)
                return@on true
            }

            join(player)
            return@on true
        }
    }

    companion object {
        @JvmStatic
        val GUARDIAN_ROOM = Location.create(1968, 4420, 2)

        @JvmStatic
        val EMPTY_ROOM = Location.create(1934, 4450, 2)

        @JvmStatic
        fun join(player: Player) {
            if (PlunderUtils.hasPlayer(player)) {
                sendMessage(player, "[PLUNDER] You should never see this message. Please report this.")
                return
            }
            PlunderUtils.registerPlayer(player)
            PlunderUtils.loadNextRoom(player)
            PlunderUtils.openOverlay(player)
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(getRegionBorders(7749))

    override fun getRestrictions(): Array<ZoneRestriction> =
        arrayOf(
            ZoneRestriction.TELEPORT,
            ZoneRestriction.RANDOM_EVENTS,
            ZoneRestriction.CANNON,
            ZoneRestriction.FIRES,
        )
}
