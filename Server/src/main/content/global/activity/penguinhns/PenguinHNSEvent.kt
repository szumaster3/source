package content.global.activity.penguinhns

import com.google.gson.JsonObject
import core.ServerStore
import core.api.StartupListener
import core.api.log
import core.plugin.ClassScanner
import core.tools.Log

/**
 * Handles startup initialization for the Penguin Hunter activity.
 * @author Ceikry
 */
class PenguinHNSEvent : StartupListener {
    /**
     * The [PenguinManager] instance managing penguin activity state.
     */
    val manager = PenguinManager()

    /**
     * Called on server startup to initialize the Penguin Hunter activity.
     *
     * - Rebuilds penguin variables from stored data.
     * - Registers dialogue plugins related to Larry (activity NPC).
     * - Logs the successful initialization.
     */
    override fun startup() {
        manager.rebuildVars()
        ClassScanner.definePlugins(LarryDialogue())
        log(this::class.java, Log.FINE, "Penguin HNS initialized.")
    }

    companion object {
        /**
         * Retrieves the persistent JSON storage file for the Penguin Hunter activity.
         *
         * @return The JSON object representing stored activity data.
         */
        @JvmStatic
        fun getStoreFile(): JsonObject = ServerStore.getArchive("weekly-penguinhns")
    }
}
