package content.global.skill.agility.courses.pyramid;

import content.global.skill.agility.AgilityHandler;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.system.task.MovementHook;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import shared.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;

/**
 * The type Moving block npc.
 */
public final class MovingBlockNPC extends AbstractNPC {

    private static final Location[][] LOCATIONS = new Location[][]{{Location.create(3366, 2847, 3), Location.create(3367, 2847, 3), Location.create(3367, 2848, 3), Location.create(3366, 2848, 3), Location.create(3365, 2847, 3), Location.create(3365, 2848, 3), Location.create(3368, 2847, 3), Location.create(3368, 2848, 3)}, {Location.create(3374, 2847, 1), Location.create(3374, 2848, 1), Location.create(3375, 2848, 1), Location.create(3375, 2847, 1), Location.create(3374, 2849, 1), Location.create(3375, 2849, 1), Location.create(3374, 2846, 1), Location.create(3375, 2846, 1)}};

    private int nextMove;

    private boolean moving;

    /**
     * Instantiates a new Moving block npc.
     */
    public MovingBlockNPC() {
        super(-1, null);
    }

    /**
     * Instantiates a new Moving block npc.
     *
     * @param id       the id
     * @param location the location
     */
    public MovingBlockNPC(int id, Location location) {
        super(id, location, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (nextMove < GameWorld.getTicks() && nextMove != -1) {
            Direction dir = getId() == 3124 ? Direction.EAST : Direction.NORTH;
            Location loc = getLocation().transform(dir, 2);
            getWalkingQueue().reset();
            getWalkingQueue().addPath(loc.getX(), loc.getY());
            for (Player p : RegionManager.getLocalPlayers(getTileLocations()[0], 4)) {
                checkBlock(p);
                playAudio(p, Sounds.PYRAMID_BLOCK_1395, 30);
            }
            moving = true;
            GameWorld.getPulser().submit(new Pulse(1, this) {
                int counter;

                @Override
                public boolean pulse() {
                    switch (++counter) {
                        case 1:
                            for (Player p : RegionManager.getLocalPlayers(getTileLocations()[0], 2)) {
                                checkBlock(p);
                            }
                            break;
                        case 3:
                            getWalkingQueue().reset();
                            getWalkingQueue().addPath(getProperties().getSpawnLocation().getX(), getProperties().getSpawnLocation().getY());
                            nextMove = GameWorld.getTicks() + 13;
                            break;
                        case 5:
                            moving = false;
                            return true;
                    }
                    return false;
                }
            });
            nextMove = -1;
        }
    }

    @Override
    public void init() {
        super.init();
        final MovingBlockTrap trap = new MovingBlockTrap();
        for (Location l : getTileLocations()) {
            AgilityPyramidZone.Companion.getLOCATION_TRAPS().put(l, trap);
        }
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new MovingBlockNPC(id, location);
    }

    /**
     * Check block.
     *
     * @param player the player
     */
    public void checkBlock(final Player player) {
        if (player.getAttribute("block-move", -1) > GameWorld.getTicks()) {
            return;
        }
        Location[] locs = getTileLocations();
        for (int i = 0; i < 4; i++) {
            if (locs[i].equals(player.getLocation())) {
                GameWorld.getPulser().submit(new Pulse(1, player) {
                    @Override
                    public boolean pulse() {
                        boolean close = getProperties().getSpawnLocation().getDistance(player.getLocation()) > (getId() == 3124 ? 2 : 2.3);
                        Location dest = null;
                        if (getId() == 3124) {
                            if (player.getLocation().getY() == 2847) {
                                dest = Location.create(3376, 2847, 1);
                            } else if (player.getLocation().getY() == 2848) {
                                dest = Location.create(3376, 2848, 1);
                            }
                        } else {
                            if (player.getLocation().getX() == 3366) {
                                dest = Location.create(3366, 2849, 3);
                            } else if (player.getLocation().getX() == 3367) {
                                dest = Location.create(3367, 2849, 3);
                            }
                        }
                        player.lock(4);
                        playAudio(player, Sounds.LAND_FLAT_2469, 50);
                        player.setAttribute("block-move", GameWorld.getTicks() + 4);
                        if (dest != null) {
                            AgilityHandler.failWalk(player, close ? 1 : 3, player.getLocation(), dest, AgilityPyramidCourse.transformLevel(dest), Animation.create(3066), 10, 8, null, getId() == 3124 ? Direction.WEST : Direction.SOUTH);
                        }
                        return true;
                    }
                });
                return;
            }
        }
    }

    /**
     * Get tile locations location [ ].
     *
     * @return the location [ ]
     */
    public Location[] getTileLocations() {
        return LOCATIONS[3125 - getId()];
    }

    @Override
    public int[] getIds() {
        return new int[]{3124, 3125};
    }

    /**
     * The type Moving block trap.
     */
    public final class MovingBlockTrap implements MovementHook {

        @Override
        public boolean handle(Entity e, Location l) {
            return !moving;
        }

    }
}
