package content.global.ame.grave

import core.game.node.entity.player.link.emote.Emotes
import org.rs.consts.*

class GraveListener {

    companion object {

        val INSTRUCTION = arrayOf("You need to:", "Pick up the coffins.", "Check the body inside.", "Find out where they need to be buried.", "Put all give coffins in the correct graves.", "Then talk to Leo to get a reward.", "You can store items in the mausoleum", "if you need more inventory space.")

        val RANDOM_NPC              = NPCs.LEO_3508
        val ANIMATION               = Animations.MULTI_BEND_OVER_827
        val COFFIN_INTERFACE        = Components.GRAVEDIGGER_COFFIN_141
        val GRAVESTONE_INTERFACE    = Components.GRAVEDIGGER_GRAVE_143
        val HOTEL                   = Scenery.TRIVAGO

        val GRAVESTONES = intArrayOf(
            Scenery.GRAVESTONE_12716,
            Scenery.GRAVESTONE_12717,
            Scenery.GRAVESTONE_12718,
            Scenery.GRAVESTONE_12719,
            Scenery.GRAVESTONE_12720,
        )

        val GRAVE = intArrayOf(
            Scenery.GRAVE_12721,
            Scenery.GRAVE_12722,
            Scenery.GRAVE_12723,
            Scenery.GRAVE_12724,
            Scenery.GRAVE_12725,
        )

        val EMPTY_SLOTS = intArrayOf(
            Scenery.GRAVE_12726,
            Scenery.GRAVE_12727,
            Scenery.GRAVE_12728,
            Scenery.GRAVE_12729,
            Scenery.GRAVE_12730
        )

        val MAUSOLEUM = Scenery.MAUSOLEUM_12731

        val COFFIN = intArrayOf(
            Items.COFFIN_7587,
            Items.COFFIN_7588,
            Items.COFFIN_7589,
            Items.COFFIN_7590,
            Items.COFFIN_7591,
        )

        val ZOMBIE_OUTFIT = intArrayOf(
            Items.ZOMBIE_SHIRT_7592,
            Items.ZOMBIE_TROUSERS_7593,
            Items.ZOMBIE_MASK_7594,
            Items.ZOMBIE_GLOVES_7595,
            Items.ZOMBIE_BOOTS_7596,
        )

        val EMOTE_0     = Emotes.ZOMBIE_DANCE
        val EMOTE_1     = Emotes.ZOMBIE_WALK

        val APRON       = Items.ITEM_7597
        val BONES       = Items.ITEM_7598
        val BOWL        = Items.ITEM_7599
        val CAKE        = Items.ITEM_7600
        val CHEF_HAT    = Items.ITEM_7601
        val DIBBER      = Items.ITEM_7602
        val AXE         = Items.ITEM_7603
        val KEBAB       = Items.ITEM_7604
        val KNIFE       = Items.ITEM_7605
        val VEIN        = Items.ITEM_7606
        val PICK        = Items.ITEM_7607
        val PLANT_POT   = Items.ITEM_7608
        val WATER_CAN   = Items.ITEM_7609
        val SECATEURS   = Items.ITEM_7610
        val SKULL       = Items.ITEM_7611
        val LOG         = Items.ITEM_7612
        val POT         = Items.ITEM_7613

        val LUMBER_GRAVESTONE   = Items.ITEM_7614
        val COOKS_GRAVESTONE    = Items.ITEM_7615
        val MINER_GRAVESTONE    = Items.ITEM_7616
        val FARMER_GRAVESTONE   = Items.ITEM_7617
        val POTTER_GRAVESTONE   = Items.ITEM_7618

        /*
         * Cook's coffin content:
         *  7604,7601,7598
         *  7600,7598,7611
         *  7598,7598,7598
         */

        val COOKS_COFFIN_CONTENT = arrayOf(
            intArrayOf(Items.ITEM_7604,Items.ITEM_7601,Items.ITEM_7598),
            intArrayOf(Items.ITEM_7600,Items.ITEM_7598,Items.ITEM_7611),
            intArrayOf(Items.ITEM_7598,Items.ITEM_7598,Items.ITEM_7598),
        )

        /*
         * Farmer's coffin content:
         *  7598,7598,7610
         *  7611,7609,7598
         *  7602,7598,7598
         */

        val FARMER_COFFIN_CONTENT = arrayOf(
            intArrayOf(Items.ITEM_7598,Items.ITEM_7598,Items.ITEM_7610),
            intArrayOf(Items.ITEM_7613,Items.ITEM_7609,Items.ITEM_7598),
            intArrayOf(Items.ITEM_7602,Items.ITEM_7598,Items.ITEM_7598),
        )

        /*
         * Potter's coffin content:
         *  7598,7599,7608
         *  7613,7598,7598
         *  7598,7611,7598
         */

        val POTTER_COFFIN_CONTENT = arrayOf(
            intArrayOf(Items.ITEM_7598,Items.ITEM_7599,Items.ITEM_7608),
            intArrayOf(Items.ITEM_7613,Items.ITEM_7598,Items.ITEM_7598),
            intArrayOf(Items.ITEM_7598,Items.ITEM_7611,Items.ITEM_7598),
        )

        /*
         * Lumberjack's coffin content:
         *  7611,7598,7598
         *  7598,7598,7603
         *  7598,7605,7612
         */

        val LUMBERJACK_COFFIN_CONTENT = arrayOf(
            intArrayOf(Items.ITEM_7611,Items.ITEM_7598,Items.ITEM_7598),
            intArrayOf(Items.ITEM_7598,Items.ITEM_7598,Items.ITEM_7603),
            intArrayOf(Items.ITEM_7598,Items.ITEM_7605,Items.ITEM_7612),
        )

        /*
         * Miner's coffin content:
         *  7598,7598,7598
         *  7598,7597,7598
         *  7607,7598,7611
         */

        val MINER_COFFIN_CONTENT = arrayOf(
            intArrayOf(Items.ITEM_7598,Items.ITEM_7598,Items.ITEM_7606),
            intArrayOf(Items.ITEM_7598,Items.ITEM_7597,Items.ITEM_7598),
            intArrayOf(Items.ITEM_7607,Items.ITEM_7598,Items.ITEM_7611),
        )

    }

}