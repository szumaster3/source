package core.worker

import com.google.protobuf.Message
import core.ServerConstants
import core.api.sendMessage
import core.auth.UserAccountInfo
import core.game.system.communication.ClanEntry
import core.game.system.communication.ClanRank
import core.game.system.communication.ClanRepository
import core.game.system.communication.CommunicationInfo
import core.game.world.GameWorld
import core.game.world.repository.Repository
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.CommunicationMessage
import core.net.packet.out.ContactPackets
import core.tools.SystemLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import proto.management.*
import proto.management.SendClanInfo.ClanMember
import proto.management.SendContactInfo.Contact
import java.util.*
import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque

/**
 * Handles management events such as player status updates, contact requests,
 * private messages, clan management, and logging.
 */
object ManagementEvents {
    private var isRunning: Boolean = true
    private val eventQueue: BlockingDeque<Message> = LinkedBlockingDeque()
    private val waitingOnClanInfo = HashMap<String, Deque<Message>>()
    private val hasRequestedClanInfo = HashMap<String, Boolean>()

    /**
     * Coroutine that processes the event queue continuously.
     */
    val job = GlobalScope.launch {
        while (isRunning) {
            val event = withContext(Dispatchers.IO) { eventQueue.take() }
            try {
                handleEvent(event)
                handleLoggingFor(event)
            } catch (ignored: Exception) {
            }
        }
    }

    /**
     * Logs event-specific messages for debugging.
     */
    private fun handleLoggingFor(event: Message) {
        when (event) {
            is PlayerStatusUpdate -> SystemLogger.logMS("${event.username} -(WLD)> ${event.world}")
            is RequestContactInfo -> SystemLogger.logMS("${event.username} -> RQ CONTACT INFO")
            is SendContactInfo -> SystemLogger.logMS("${event.username} <- SND CONTACT INFO")
            is PrivateMessage -> SystemLogger.logMS("[PM] ${event.sender}->${event.receiver}: ${event.message}")
            is ClanMessage -> SystemLogger.logMS("[CM:${event.clanName}] ${event.sender}: ${event.message}")
            is JoinClanRequest -> SystemLogger.logMS("${event.username} +CL ${event.clanName}")
            is LeaveClanRequest -> SystemLogger.logMS("${event.username} -CL ${event.clanName}")
            is RequestClanInfo -> SystemLogger.logMS("REQUEST CLAN INFO: ${event.clanOwner}")
            is SendClanInfo -> SystemLogger.logMS("RECEIVE CLAN INFO: ${event.clanOwner}->${event.clanName}")
            is ClanJoinNotification -> SystemLogger.logMS("${event.username} JOINED CLAN ${event.clanName}")
            is ClanLeaveNotification -> SystemLogger.logMS("${event.username} LEFT CLAN ${event.clanName}")
        }
    }

    /**
     * Publishes an event by adding it to the event queue.
     */
    @JvmStatic
    fun publish(event: Message) {
        eventQueue.offer(event)
    }

    /**
     * Handles processing of incoming events.
     */
    private fun handleEvent(event: Message) {
        when (event) {

            /**
             * Handles player status updates by notifying friends or all players.
             */
            is PlayerStatusUpdate -> {
                val notifiablePlayers = if (event.notifyFriendsOnly) {
                    GameWorld.accountStorage.getOnlineFriends(event.username)
                } else {
                    Repository.playerNames.keys.toList()
                }.filter {
                    Repository.getPlayerByName(
                            it,
                        )?.communication?.contacts?.containsKey(event.username) == true
                }

                for (playerName in notifiablePlayers) {
                    val p = Repository.getPlayerByName(playerName) ?: continue
                    p.communication.contacts[event.username]?.worldId = event.world
                    PacketRepository.send(
                        ContactPackets::class.java, OutgoingContext.Contact(p, event.username, event.world)
                    )
                }
            }

            /**
             * Handles requests for contact information and sends back the appropriate response.
             */
            is RequestContactInfo -> {
                val response = SendContactInfo.newBuilder()
                response.username = event.username
                val info = GameWorld.accountStorage.getAccountInfo(event.username)
                val contacts = CommunicationInfo.parseContacts(info.contacts)

                for ((username, contact) in contacts) {
                    val online = Repository.getPlayerByName(username) != null
                    val cbuild = Contact.newBuilder()
                    cbuild.username = username
                    cbuild.world = if (online) GameWorld.settings!!.worldId else 0
                    cbuild.rank = contact.rank.ordinal
                    response.addContacts(cbuild)
                }

                val blocked = info.blocked.split(",")
                for (user in blocked) response.addBlocked(user)

                publish(response.build())
            }

            /**
             * Processes received contact info and updates the player's contacts.
             */
            is SendContactInfo -> {
                val p = Repository.getPlayerByName(event.username) ?: return

                PacketRepository.send(
                    ContactPackets::class.java,
                    OutgoingContext.Contact(p, OutgoingContext.Contact.UPDATE_STATE_TYPE),
                )

                p.communication.contacts.clear()
                p.communication.blocked.clear()

                for (contact in event.contactsList) {
                    val c = core.game.system.communication.Contact(contact.username)
                    p.communication.contacts[contact.username] = c
                    c.worldId = contact.world
                    c.rank = ClanRank.values()[contact.rank]
                    PacketRepository.send(
                        ContactPackets::class.java,
                        OutgoingContext.Contact(p, contact.username, contact.world),
                    )
                }

                for (blocked in event.blockedList) {
                    p.communication.blocked.add(blocked)
                }

                PacketRepository.send(
                    ContactPackets::class.java,
                    OutgoingContext.Contact(p, OutgoingContext.Contact.IGNORE_LIST_TYPE),
                )
            }

            is FriendUpdate -> {
                val remove = event.type == FriendUpdate.Type.REMOVE
                val f = Repository.getPlayerByName(event.friend)
                val p = Repository.getPlayerByName(event.username)
                val world = if (f != null) GameWorld.settings!!.worldId else 0
            }

            /**
             * Processes private messages between players.
             */
            is PrivateMessage -> {
                val sender = Repository.getPlayerByName(event.sender)
                val receiver = Repository.getPlayerByName(event.receiver)

                if (sender != null) {
                    PacketRepository.send(
                        CommunicationMessage::class.java,
                        OutgoingContext.MessageContext(
                            sender,
                            event.receiver,
                            event.rank,
                            OutgoingContext.MessageContext.SEND_MESSAGE,
                            event.message
                        ),
                    )
                }

                if (receiver != null) {
                    PacketRepository.send(
                        CommunicationMessage::class.java,
                        OutgoingContext.MessageContext(
                            receiver,
                            event.sender,
                            event.rank,
                            OutgoingContext.MessageContext.RECEIVE_MESSAGE,
                            event.message,
                        ),
                    )
                }
            }

            /**
             * Processes a request to join a clan, checking if the clan exists and allowing entry.
             */
            is JoinClanRequest -> {
                val p = Repository.getPlayerByName(event.username) ?: return

                if (shouldWaitForClanInfo(event.clanName)) {
                    queueUntilClanInfo(event.clanName, event)
                    return
                }

                val clan = ClanRepository.get(event.clanName)

                if (clan == null) {
                    sendMessage(p, "The channel you tried to join does not exist.:clan:")
                } else {
                    clan.enter(p)
                    p.communication.clan = clan
                }
            }

            /**
             * Handles notifications of players joining a clan.
             */
            is ClanJoinNotification -> {
                if (event.world == GameWorld.settings!!.worldId) return

                if (shouldWaitForClanInfo(event.clanName)) {
                    queueUntilClanInfo(event.clanName, event)
                    return
                }

                val clan = ClanRepository.get(event.clanName)
                val entry = ClanEntry(event.username, event.world)
                clan.players.add(entry)
                clan.update()
            }

            /**
             * Processes a request to leave a clan.
             */
            is LeaveClanRequest -> {
                val p = Repository.getPlayerByName(event.username) ?: return

                if (shouldWaitForClanInfo(event.clanName)) {
                    queueUntilClanInfo(event.clanName, event)
                    return
                }

                val clan = ClanRepository.get(event.clanName)

                if (clan == null) {
                    sendMessage(p, "Error leaving clan. Please relog.")
                } else {
                    clan.leave(p, true)
                    p.details.communication.clan = null
                }
            }

            /**
             * Handles notifications of players leaving a clan.
             */
            is ClanLeaveNotification -> {
                if (shouldWaitForClanInfo(event.clanName)) {
                    queueUntilClanInfo(event.clanName, event)
                    return
                }

                val clan = ClanRepository.get(event.clanName)
                val entry = clan.players.firstOrNull { it.name.equals(event.username) } ?: return
                clan.players.remove(entry)
                clan.update()
            }

            /**
             * Processes requests for clan information.
             */
            is RequestClanInfo -> {
                val clan = ClanRepository.get(event.clanOwner)
                val response = SendClanInfo.newBuilder()

                if (clan == null) {
                    response.hasInfo = false
                    response.clanOwner = event.clanOwner
                } else {
                    response.hasInfo = true
                    response.clanName = clan.name
                    response.clanOwner = event.clanOwner

                    for (member in clan.players) {
                        val cmBuilder = ClanMember.newBuilder()
                        cmBuilder.username = member.name
                        cmBuilder.world = member.worldId
                        cmBuilder.rank = (clan.ranks[member.name] ?: ClanRank.ANYONE).ordinal
                        response.addMembers(cmBuilder)
                    }
                }

                publish(response.build())
            }

            /**
             * Handles received clan information and updates the repository accordingly.
             */
            is SendClanInfo -> {
                if (event.hasInfo) {
                    initializeClanFrom(event)
                } else {
                    var info = GameWorld.accountStorage.getAccountInfo(event.clanOwner)
                    if (info.clanName.isNotEmpty()) {
                        initializeClanWith(info)
                    } else {
                        SystemLogger.logMS("Creating default server clan")
                        if (GameWorld.settings!!.enable_default_clan && event.clanOwner == ServerConstants.SERVER_NAME.lowercase()) {
                            if (info == UserAccountInfo.createDefault()) {
                                info.username = ServerConstants.SERVER_NAME.lowercase()
                                info.password = ServerConstants.MS_SECRET_KEY
                                info.rights = 2
                                GameWorld.authenticator.createAccountWith(info)
                                info = GameWorld.accountStorage.getAccountInfo(event.clanOwner)
                            }

                            info.clanName = "Global"
                            info.clanReqs = "-1,-1,7,7" // Any join, any message, owner kick, owner loot
                            GameWorld.accountStorage.update(info)
                            initializeClanWith(info)
                        }
                    }
                }

                val queuedEvents = waitingOnClanInfo[event.clanOwner] ?: return
                while (queuedEvents.peek() != null) {
                    publish(queuedEvents.pop())
                }
            }

            /**
             * Processes messages sent in a clan chat.
             */
            is ClanMessage -> {
                if (shouldWaitForClanInfo(event.clanName)) {
                    queueUntilClanInfo(event.clanName, event)
                    return
                }

                val clan = ClanRepository.get(event.clanName)

                for (member in clan.players.filter { it.player != null }) {
                    PacketRepository.send(
                        CommunicationMessage::class.java,
                        OutgoingContext.MessageContext(
                            member.player,
                            event.sender,
                            event.rank,
                            OutgoingContext.MessageContext.CLAN_MESSAGE,
                            event.message,
                        ),
                    )
                }
            }
        }
    }

    private fun initializeClanFrom(event: SendClanInfo) {
        val clan = ClanRepository.getClans().getOrPut(event.clanOwner) { ClanRepository(event.clanOwner) }
        clan.name = event.clanName
        clan.joinRequirement = ClanRank.values()[event.joinRequirement]
        clan.kickRequirement = ClanRank.values()[event.kickRequirement]
        clan.messageRequirement = ClanRank.values()[event.messageRequirement]
        clan.lootRequirement = ClanRank.values()[event.lootRequirement]

        for (member in event.membersList) {
            val entry = ClanEntry(member.username, member.world)
            clan.ranks[member.username] = ClanRank.values()[member.rank]
            if (member.world == GameWorld.settings!!.worldId) {
                val p = Repository.getPlayerByName(member.username)
                entry.player = p
                p?.communication?.clan = clan
            }
            clan.players.add(entry)
        }

        clan.update()
    }

    private fun initializeClanWith(info: UserAccountInfo) {
        val reqs = CommunicationInfo.parseClanRequirements(info.clanReqs)
        val c = ClanRepository(info.username)
        val contacts = CommunicationInfo.parseContacts(info.contacts)
        c.name = info.clanName
        c.joinRequirement = reqs[0]
        c.messageRequirement = reqs[1]
        c.kickRequirement = reqs[2]
        c.lootRequirement = reqs[3]
        for ((username, contact) in contacts) {
            c.ranks[username] = contact.rank
        }
        ClanRepository.getClans()[info.username] = c
    }

    /**
     * Queues events until clan information is available.
     */
    private fun queueUntilClanInfo(
        clanName: String,
        message: Message,
    ) {
        val queue = waitingOnClanInfo.getOrPut(clanName) { LinkedList() }
        queue.offer(message)

        if (hasRequestedClanInfo[clanName] == null) {
            val request = RequestClanInfo.newBuilder()
            request.clanOwner = clanName
            request.world = GameWorld.settings!!.worldId
            publish(request.build())
            hasRequestedClanInfo[clanName] = true
        }
    }

    /**
     * Determines whether to wait for clan information before processing an event.
     */
    private fun shouldWaitForClanInfo(clanName: String): Boolean =
        ClanRepository.get(clanName) == null && hasRequestedClanInfo[clanName] == null
}
