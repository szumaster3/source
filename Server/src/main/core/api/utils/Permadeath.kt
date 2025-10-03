package core.api.utils

import core.api.isUsingSecondaryBankAccount
import core.api.teleport
import core.api.toggleBankAccount
import core.game.node.entity.player.Player
import core.game.node.entity.player.VarpManager
import core.game.node.entity.player.info.login.PlayerSaver
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.player.link.SavedData
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.quest.QuestRepository
import core.game.node.entity.skill.Skills
import core.game.world.map.Location

fun Permadeath(target: Player) {
    teleport(target, Location.create(3094, 3107, 0))
    target.equipment.clear()
    target.inventory.clear()

    if (isUsingSecondaryBankAccount(target)) {
        toggleBankAccount(target)
    }

    target.bank.clear()
    target.bankSecondary.clear()
    target.skills = Skills(target)
    target.clearAttributes()
    target.savedData = SavedData(target)
    target.questRepository = QuestRepository(target)
    target.varpManager = VarpManager(target)
    target.varpMap.clear()
    target.saveVarp.clear()

    if (target.familiarManager.hasFamiliar()) {
        target.familiarManager.dismiss()
    }

    val petKeys =
        target.familiarManager.petDetails.keys
            .toList()
    for (key in petKeys) {
        target.familiarManager.removeDetails(key)
    }

    for (type in DiaryType.values()) {
        val diary = target.achievementDiaryManager.getDiary(type)
        if (diary != null) {
            for (level in 0 until diary.levelStarted.size) {
                for (task in 0 until diary.taskCompleted[level].size) {
                    diary.resetTask(target, level, task)
                }
            }
        }
    }

    target.musicPlayer.clearUnlocked()

    target.ironmanManager.mode = IronmanMode.NONE

    PlayerSaver(target).save()

    target.clear()
}
