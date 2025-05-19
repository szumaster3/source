package content.global.activity.ttrail.cryptic;

import content.global.activity.ttrail.ClueLevel;
import content.global.activity.ttrail.ClueScrollPlugin;
import core.game.global.action.DigAction;
import core.game.global.action.DigSpadeHandler;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.map.zone.ZoneBorders;
import org.rs.consts.Components;
import org.rs.consts.Items;

/**
 * A base class for cryptic clue scrolls requiring object interaction or digging at a specific location.
 */
public abstract class CrypticClueScroll extends ClueScrollPlugin {

    /**
     * The location associated with the clue.
     */
    private final Location location;

    /**
     * The id of the searchable object, or 0 if not used.
     */
    private final int object;

    /**
     * The text of the cryptic clue shown to the player.
     */
    private final String clueText;

    /**
     * The id of the npc.
     */
    private final int npcId;

    /**
     * The required item.
     */
    private final int requiredItemId;


    /**
     * Constructs a new cryptic clue scroll.
     *
     * @param name         the internal name of the clue
     * @param clueId       the clue item id
     * @param level        the clue difficulty level
     * @param clueText     the cryptic clue text shown to the player
     * @param location     the associated dig or interaction location
     * @param npcId        the NPC id related to this clue (used externally)
     * @param objectId     the object id that can be searched, or 0 if unused
     * @param borders      the optional zone borders associated with this clue
     */
    public CrypticClueScroll(String name, int clueId, ClueLevel level, String clueText, Location location, int npcId, int objectId, ZoneBorders[] borders) {
        super(name, clueId, level, Components.TRAIL_MAP09_345, borders);
        this.location = location;
        this.npcId = npcId;
        this.object = objectId;
        this.requiredItemId = -1;
        this.clueText = clueText;
    }

    /**
     * Called when a player interacts with an object.
     *
     * @param e      the entity performing the action
     * @param target the target node (object)
     * @param option the selected interaction option
     * @return true if the clue was completed through this interaction
     */
    @Override
    public boolean interact(Entity e, Node target, Option option) {
        if (e instanceof Player) {
            Player p = e.asPlayer();
            if (target.getId() == object && option.getName().equals("Search")) {
                if (!p.getInventory().contains(clueId, 1) || !target.getLocation().equals(location)) {
                    p.sendMessage("Nothing interesting happens.");
                    return false;
                }
                reward(p);
                return true;
            }

            if (npcId > 0 && target instanceof NPC) {
                NPC npc = (NPC) target;
                if (npc.getId() == npcId) {
                    if (!p.getInventory().contains(clueId, 1) || !target.getLocation().equals(location)) {
                        p.sendMessage("Nothing interesting happens.");
                        return false;
                    }
                    reward(p);
                    npc.face(p);
                    return true;
                }
            }
            /*
            if (object > 0 && target.getId() == object && option.getName().equals("Search")) {
                if (!target.getLocation().equals(location)) {
                    p.sendMessage("Nothing interesting happens.");
                    return false;
                }

                if (requiredItemId > 0 && !p.getInventory().contains(requiredItemId, 1)) {
                    p.sendMessage("The chest is locked.");
                    return false;
                }

                reward(p);
                return true;
            }
            */
        }
        return super.interact(e, target, option);
    }

    /**
     * Registers the dig location for this clue.
     */
    @Override
    public void configure() {
        DigSpadeHandler.register(getLocation(), new CrypticDigAction());
        super.configure();
    }

    /**
     * Displays the clue text to the player.
     *
     * @param player the player reading the clue scroll
     */
    @Override
    public void read(Player player) {
        super.read(player);
        for (int i = 1; i <= 8; i++) {
            player.getPacketDispatch().sendString("", interfaceId, i);
        }
        player.getPacketDispatch().sendString(clueText, interfaceId, 1);
    }

    /**
     * Called when the player digs at the correct location.
     *
     * @param player the player digging
     */
    public void dig(Player player) {
        reward(player);
        player.getDialogueInterpreter().sendItemMessage(Items.CASKET_405, "You've found a casket!");
    }

    /**
     * Handles digging action for this clue.
     */
    public final class CrypticDigAction implements DigAction {
        @Override
        public void run(Player player) {
            if (!hasRequiredItems(player)) {
                player.sendMessage("Nothing interesting happens.");
                return;
            }
            dig(player);
        }
    }

    /**
     * Checks if the player has the clue item.
     *
     * @param player the player to check
     * @return true if they have the clue item
     */
    public boolean hasRequiredItems(Player player) {
        return player.getInventory().contains(clueId, 1);
    }

    /**
     * @return the clue's target location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return the id of the object to search, or 0 if none
     */
    public int getObject() {
        return object;
    }

    /**
     * @return the clue text shown to the player
     */
    public String getClueText() {
        return clueText;
    }

    /**
     * @return the id of the related NPC
     */
    public int getNpcId() {
        return npcId;
    }

    /**
     * @return the id of the required item, or -1 if no item is required
     */
    public int getRequiredItemId() {
        return requiredItemId;
    }

}
