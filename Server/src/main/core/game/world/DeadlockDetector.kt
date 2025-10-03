package core.game.world

import core.api.log
import core.tools.Log
import java.lang.management.ManagementFactory

/**
 * Detects and logs deadlocked threads when run.
 */
class DeadlockDetector : Runnable {
    override fun run() {
        val mbean = ManagementFactory.getThreadMXBean()
        val deadLockedThreads = mbean.findDeadlockedThreads()

        fun `(┛◉Д◉)┛彡┻━┻`() {
            val infos = mbean.getThreadInfo(deadLockedThreads)
            infos.forEach { threadInfo ->
                if (threadInfo != null) {
                    for (thread in Thread.getAllStackTraces().keys) {
                        if (thread.id == threadInfo.threadId) {
                            log(this::class.java, Log.ERR, threadInfo.toString().trim())
                            for (ste in thread.stackTrace) {
                                log(this::class.java, Log.ERR, "\t${ste.toString().trim()}")
                            }
                        }
                    }
                }
            }
        }

        if (deadLockedThreads != null) {
            `(┛◉Д◉)┛彡┻━┻`()
        }
    }
}
