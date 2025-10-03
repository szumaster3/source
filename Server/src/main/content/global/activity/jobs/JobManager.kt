package content.global.activity.jobs

import com.google.gson.JsonObject
import content.global.activity.jobs.impl.BoneBuryingJobs
import content.global.activity.jobs.impl.CombatJobs
import content.global.activity.jobs.impl.ProductionJobs
import core.ServerStore
import core.ServerStore.Companion.getInt
import core.api.*
import core.game.event.BoneBuryEvent
import core.game.event.EventHook
import core.game.event.JobAssignmentEvent
import core.game.event.NPCKillEvent
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.StringUtils
import shared.consts.Items
import java.lang.Integer.min

/**
 * Manages the player job data & progression.
 *
 * @source https://runescape.wiki/w/Jobs?oldid=831782
 */
class JobManager(
    val player: Player? = null,
) : LoginListener,
    PersistPlayer {
    var job: Job? = null
    var jobAmount: Int = 0
    var jobOriginalAmount: Int = 0

    private fun hasLevelRequirement(job: ProductionJobs): Boolean {
        if (player == null) return false
        if (player.isArtificial) return false

        return getStatLevel(player, job.skill) >= job.lvlReq
    }

    fun hasJob(): Boolean = this.job != null

    fun hasReachedJobLimit(): Boolean = player?.let { getStoreFile().getInt(it.username.lowercase()) >= 3 } ?: false

    fun generate(npc: NPC) {
        if (player == null) return
        if (player.isArtificial) return

        val instance = getInstance(player)

        val potentialJobs: List<Job> =
            (
                ProductionJobs.values().filter { hasLevelRequirement(it) } +
                    BoneBuryingJobs.values() +
                    CombatJobs.values()
            ).filter { job -> job.employer.npcId == npc.id }

        val job = potentialJobs.randomOrNull() ?: return

        instance.job = job
        this.jobAmount = job.getAmount()
        this.jobOriginalAmount = instance.jobAmount

        player.dispatch(JobAssignmentEvent(job.type, npc))

        val key = player.username.lowercase()
        val current = if (getStoreFile().has(key)) getStoreFile().get(key).asInt else 0
        getStoreFile().addProperty(key, current + 1)
    }

    fun getAssignmentMessage(): String? {
        if (player == null) return null
        if (player.isArtificial) return null

        val instance = getInstance(player)
        val job = instance.job ?: return null

        return when (job.type) {
            JobType.PRODUCTION -> {
                job as ProductionJobs
                val itemName = Item(job.itemId).name.lowercase()
                val itemNamePluralized = if (this.jobAmount > 1) StringUtils.plusS(itemName) else itemName
                "Okay, your job is to bring me ${this.jobAmount} $itemNamePluralized."
            }

            JobType.BONE_BURYING -> {
                job as BoneBuryingJobs
                val boneName = Item(job.boneIds.first()).name.lowercase()
                val boneNamePluralized = if (this.jobAmount > 1) StringUtils.plusS(boneName) else boneName
                "Okay, your job is to bury ${this.jobAmount} $boneNamePluralized in the ${job.buryArea.title}."
            }

            JobType.COMBAT -> {
                job as CombatJobs
                val monsterName = NPC(job.monsterIds.first()).name.lowercase()
                val monsterNamePluralized = if (this.jobAmount > 1) StringUtils.plusS(monsterName) else monsterName
                "Okay, your job is to kill ${this.jobAmount} $monsterNamePluralized."
            }
        }
    }

    fun turnInItems() {
        if (player == null) return
        if (player.isArtificial) return

        val instance = getInstance(player)
        val job = instance.job ?: return
        if (job.type != JobType.PRODUCTION) return

        val itemId = (job as ProductionJobs).itemId
        val amountHeld = amountInInventory(player, itemId)
        val amountToTurnIn = min(amountHeld, instance.jobAmount)
        removeItem(player, Item(itemId, amountToTurnIn))
        instance.jobAmount -= amountToTurnIn
    }

    fun rewardPlayer() {
        if (player == null) return
        if (player.isArtificial) return

        val instance = getInstance(player)

        addItemOrDrop(player, Items.COINS_995, 250 * instance.jobOriginalAmount)

        instance.job = null
        instance.jobAmount = -1
        instance.jobOriginalAmount = -1
    }

    override fun login(player: Player) {
        if (player.isArtificial) return

        val jobManager = JobManager(player)
        setAttribute(player, "job-manager", jobManager)

        player.hook(
            Event.BoneBuried,
            object : EventHook<BoneBuryEvent> {
                override fun process(
                    entity: Entity,
                    event: BoneBuryEvent,
                ) {
                    if (entity !is Player) return
                    if (entity.isArtificial) return

                    val instance = getInstance(entity)

                    if (!instance.hasJob()) return
                    if (instance.job?.type != JobType.BONE_BURYING) return
                    if (instance.jobAmount == 0) return

                    val job = (instance.job as BoneBuryingJobs)
                    if (inBorders(entity, job.buryArea.zoneBorder) && event.boneId in job.boneIds) {
                        instance.jobAmount--
                    }
                }
            },
        )
        player.hook(
            Event.NPCKilled,
            object : EventHook<NPCKillEvent> {
                override fun process(
                    entity: Entity,
                    event: NPCKillEvent,
                ) {
                    if (entity !is Player) return
                    if (entity.isArtificial) return

                    val instance = getInstance(entity)

                    if (!instance.hasJob()) return
                    if (instance.job?.type != JobType.COMBAT) return
                    if (instance.jobAmount == 0) return

                    val job = (instance.job as CombatJobs)
                    if (event.npc.id in job.monsterIds) {
                        instance.jobAmount--
                    }
                }
            },
        )
    }

    override fun savePlayer(
        player: Player,
        save: JsonObject,
    ) {
        val instance = getInstance(player)
        val jobId: Int =
            instance.job?.let {
                when (it.type) {
                    JobType.PRODUCTION -> ProductionJobs.values().indexOf(it)
                    JobType.COMBAT -> CombatJobs.values().indexOf(it)
                    JobType.BONE_BURYING -> BoneBuryingJobs.values().indexOf(it)
                }
            } ?: -1

        setAttribute(player, "/save:jobs:id", jobId)
        setAttribute(player, "/save:jobs:type", instance.job?.type?.ordinal ?: -1)
        setAttribute(player, "/save:jobs:amount", instance.jobAmount)
        setAttribute(player, "/save:jobs:original_amount", instance.jobOriginalAmount)
    }

    override fun parsePlayer(
        player: Player,
        data: JsonObject,
    ) {
        val instance = getInstance(player)
        val jobId = getAttribute(player, "jobs:id", -1)
        val jobType = getAttribute(player, "jobs:type", -1)

        instance.job =
            when (JobType.values().getOrNull(jobType)) {
                JobType.PRODUCTION -> ProductionJobs.values().getOrNull(jobId)
                JobType.COMBAT -> CombatJobs.values().getOrNull(jobId)
                JobType.BONE_BURYING -> BoneBuryingJobs.values().getOrNull(jobId)
                else -> null
            }
        instance.jobAmount = getAttribute(player, "jobs:amount", -1)
        instance.jobOriginalAmount = getAttribute(player, "jobs:original_amount", -1)
    }

    companion object {
        @JvmStatic
        fun getInstance(player: Player): JobManager = player.getAttribute("job-manager", JobManager(player))

        fun getStoreFile(): JsonObject = ServerStore.getArchive("daily-jobs-tracking")
    }
}
