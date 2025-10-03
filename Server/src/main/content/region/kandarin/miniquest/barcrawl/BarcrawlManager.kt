package content.region.kandarin.miniquest.barcrawl

import com.google.gson.JsonObject
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items

class BarcrawlManager :
    LoginListener,
    PersistPlayer {
    private val player: Player?

    val bars: BooleanArray = BooleanArray(10)

    private var started = false

    constructor(player: Player?) {
        this.player = player
    }

    constructor() {
        this.player = null
    }

    override fun login(player: Player) {
        val instance = BarcrawlManager(player)
        setAttribute(player, "barcrawl-inst", instance)
    }

    override fun parsePlayer(
        player: Player,
        data: JsonObject,
    ) {
        val bcData = data.getAsJsonObject("barCrawl") ?: return
        val barsVisited = bcData.getAsJsonArray("bars")
        val instance = getInstance(player)
        instance.started = bcData.get("started").asBoolean
        for (i in 0 until barsVisited.size()) {
            instance.bars[i] = barsVisited[i].asBoolean
        }
    }

    override fun savePlayer(
        player: Player,
        save: JsonObject,
    ) {
        val instance = getInstance(player)
        val barCrawl = JsonObject()
        barCrawl.addProperty("started", instance.started)
        val barsVisited = com.google.gson.JsonArray()
        instance.bars.forEach { visited ->
            barsVisited.add(visited)
        }
        barCrawl.add("bars", barsVisited)
        save.add("barCrawl", barCrawl)
    }

    fun read() {
        if (isFinished) {
            sendMessage(player!!, "You are too drunk to be able to read the barcrawl card.")
            return
        }
        openInterface(player!!, COMPONENT)
        drawCompletions()
    }

    private fun drawCompletions() {
        sendString(player!!, "<col=0000FF>The Official Alfred Grimhand Barcrawl!", Components.MESSAGESCROLL_220, 1)
        var complete: Boolean
        for (i in bars.indices) {
            complete = bars[i]
            sendString(
                player,
                (if (complete) "<col=00FF00>" else "<col=FF0000>") + NAMES[i] + " - " +
                    (if (complete) "Complete!" else "Not Completed..."),
                Components.MESSAGESCROLL_220,
                3 + i,
            )
        }
    }

    fun complete(index: Int) {
        bars[index] = true
    }

    val isFinished: Boolean
        get() {
            for (i in bars.indices) {
                if (!isCompleted(i)) {
                    return false
                }
            }
            return true
        }

    fun reset() {
        started = false
        for (i in bars.indices) {
            bars[i] = false
        }
    }

    fun isCompleted(index: Int): Boolean = bars[index]

    fun hasCard(): Boolean = inInventory(player!!, Items.BARCRAWL_CARD_455) || inBank(player, Items.BARCRAWL_CARD_455)

    fun isStarted(): Boolean = started

    fun setStarted(started: Boolean) {
        this.started = started
    }

    companion object {
        val BARCRAWL_CARD: Item = Item(Items.BARCRAWL_CARD_455)
        val NAMES: Array<String> =
            arrayOf(
                "BlueMoon Inn",
                "Blueberry's Bar",
                "Dead Man's Chest",
                "Dragon Inn",
                "Flying Horse Inn",
                "Foresters Arms",
                "Jolly Boar Inn",
                "Karamja Spirits bar",
                "Rising Sun Inn",
                "Rusty Anchor Inn",
            )
        val COMPONENT = Components.MESSAGESCROLL_220

        @JvmStatic
        fun getInstance(player: Player): BarcrawlManager = player.getAttribute("barcrawl-inst", BarcrawlManager())
    }
}
