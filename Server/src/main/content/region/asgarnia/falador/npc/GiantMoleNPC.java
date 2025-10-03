package content.region.asgarnia.falador.npc;

import content.data.LightSource;
import content.global.plugin.iface.WarningListener;
import content.global.plugin.iface.Warnings;
import core.api.utils.BossKillCounter;
import core.cache.def.impl.SceneryDefinition;
import core.game.global.action.DigAction;
import core.game.global.action.DigSpadeHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.zone.impl.DarkZone;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.net.packet.OutgoingContext;
import core.net.packet.PacketRepository;
import core.net.packet.out.Interface;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import shared.consts.Animations;
import shared.consts.NPCs;
import shared.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;

/**
 * The type Giant mole npc.
 */
@Initializable
public final class GiantMoleNPC extends AbstractNPC {
    private static final Location[] DIG_LOCATIONS = new Location[]{Location.create(1760, 5183, 0), Location.create(1736, 5223, 0), Location.create(1777, 5235, 0), Location.create(1739, 5150, 0), Location.create(1769, 5148, 0), Location.create(1750, 5195, 0), Location.create(1778, 5207, 0), Location.create(1772, 5199, 0), Location.create(1774, 5173, 0), Location.create(1760, 5162, 0), Location.create(1753, 5151, 0), Location.create(1739, 5152, 0)};
    private static final Animation DIG_ANIMATION = new Animation(3314, Priority.VERY_HIGH);
    private static final Graphics DIG_GRAPHIC = new Graphics(shared.consts.Graphics.HOLE_OPENS_IN_GROUND_572);
    private static final Animation DIG_UP_ANIMATION = new Animation(3315, Priority.VERY_HIGH);
    private static final Graphics DIG_UP_GRAPHIC = new Graphics(shared.consts.Graphics.HOLE_OPENS_IN_GROUND_SHORTER_573);
    private static final Graphics DUST_GRAPHIC = new Graphics(shared.consts.Graphics.BUNCHA_SMOKE_BROWN_COLORED_571);

    private boolean digging;

    /**
     * Instantiates a new Giant mole npc.
     */
    public GiantMoleNPC() {
        super(NPCs.GIANT_MOLE_3340, null);
    }

    /**
     * Instantiates a new Giant mole npc.
     *
     * @param id       the id
     * @param location the location
     */
    public GiantMoleNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public void init() {
        super.init();
        super.setWalks(true);
        super.walkRadius = 128;
    }

    private void dig() {
        digging = true;
        lock(5);
        getProperties().getCombatPulse().stop();
        getWalkingQueue().reset();
        getLocks().lockMovement(5);
        final Direction dir = Direction.get(RandomFunction.randomize(4));
        faceLocation(getCenterLocation().transform(dir.getStepX() << 2, dir.getStepY() << 2, 0));
        setDirection(dir);
        int index = RandomFunction.randomize(DIG_LOCATIONS.length);
        Location dest = DIG_LOCATIONS[index];
        if (getLocation().withinDistance(dest)) {
            dest = DIG_LOCATIONS[(index + 1) % DIG_LOCATIONS.length];
        }
        final Location destination = dest;
        GameWorld.getPulser().submit(new Pulse(1, this) {
            int count = 0;
            Location hole;

            @Override
            public boolean pulse() {
                if (count == 0) {
                    hole = visualizeDig(destination, true);
                } else if (count == 1) {
                    if (RandomFunction.RANDOM.nextBoolean()) {
                        splatterMud(hole);
                    }
                } else if (count == 3) {
                    getProperties().setTeleportLocation(destination);
                } else if (count == 4) {
                    visualizeDig(destination, false);
                    digging = false;
                    return true;
                }
                count++;
                return false;
            }
        });
    }

    private void splatterMud(Location hole) {
        for (Player p : RegionManager.getLocalPlayers(getCenterLocation(), (size() >> 1) + 2)) {
            PacketRepository.send(Interface.class, new OutgoingContext.InterfaceContext(p, 548, 77, 226, true));
            LightSource s = LightSource.getActiveLightSource(p);
            if (s == null || s.getOpen()) {
                if (s != null) {
                    p.getPacketDispatch().sendMessage("Your " + s.getName() + " seems to have been extinguished by the mud.");
                    int slot = p.getInventory().getSlot(s.getProduct());
                    if (slot > -1) {
                        p.getInventory().replace(new Item(s.getRaw().getId()), slot);
                    }
                }
                DarkZone.Companion.checkDarkArea(p);
            }
        }
        for (int i = 0; i < 1 + RandomFunction.randomize(3); i++) {
            Projectile.create(hole, hole.transform(-4 + RandomFunction.randomize(9), -4 + RandomFunction.randomize(9), 0), 570, 0, 5, 45, 70, 5, 11).send();
        }
    }

    private Location visualizeDig(Location destination, boolean underground) {
        Location offset = getCenterLocation();
        if (underground) {
            switch (getDirection()) {
                case NORTH:
                    offset = getLocation().transform(1, size() - 1, 0);
                    break;
                case EAST:
                    offset = getLocation().transform(size() - 1, 1, 0);
                    break;
                case WEST:
                    offset = getLocation().transform(0, 1, 0);
                    break;
                default:
                    offset = getLocation().transform(1, 0, 0);
                    break;
            }
        }
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                Graphics.send(DUST_GRAPHIC, offset.transform(x, y, 0));
            }
        }
        if (underground) {
            animate(DIG_ANIMATION);
            Graphics.send(DIG_GRAPHIC, offset);
        } else {
            animate(DIG_UP_ANIMATION);
            Graphics.send(DIG_UP_GRAPHIC, offset);
        }
        return offset;
    }

    @Override
    public void onImpact(final Entity entity, BattleState state) {
        if (!getLocks().isInteractionLocked()) {
            if (RandomFunction.randomize(100) < 24 && inCombat() && getSkills().getLifepoints() < 100 && getSkills().getLifepoints() > 5) {
                dig();
                return;
            }
        }
        super.onImpact(entity, state);
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
        if (digging) {
            return false;
        }
        return super.isAttackable(entity, style, message);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        DigAction action = new DigAction() {
            @Override
            public void run(Player player) {
                if (!LightSource.hasActiveLightSource(player)) {
                    player.getPacketDispatch().sendMessage("It's going to be dark down there, I should bring a light source.");
                    return;
                }
                if (!WarningListener.isDisabled(player, Warnings.FALADOR_MOLE_LAIR)) {
                    WarningListener.openWarning(player, Warnings.FALADOR_MOLE_LAIR);
                } else {
                    player.getProperties().setTeleportLocation(Location.create(1752, 5237, 0));
                    playAudio(player, Sounds.ROOF_COLLAPSE_1384);
                    player.getPacketDispatch().sendMessage("You seem to have dropped down into a network of mole tunnels.");

                    if (!player.getAchievementDiaryManager().getDiary(DiaryType.FALADOR).isComplete(0, 5)) {
                        player.getAchievementDiaryManager().getDiary(DiaryType.FALADOR).updateTask(player, 0, 5, true);
                    }
                }
            }
        };
        DigSpadeHandler.register(Location.create(3005, 3376, 0), action);
        DigSpadeHandler.register(Location.create(2999, 3375, 0), action);
        DigSpadeHandler.register(Location.create(2996, 3377, 0), action);
        DigSpadeHandler.register(Location.create(2989, 3378, 0), action);
        DigSpadeHandler.register(Location.create(2984, 3387, 0), action);
        DigSpadeHandler.register(Location.create(2987, 3387, 0), action);

        SceneryDefinition.forId(12230).getHandlers().put("option:climb", new OptionHandler() {
            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                return this;
            }

            @Override
            public boolean handle(final Player player, Node node, String option) {
                player.animate(Animation.create(Animations.USE_LADDER_828));
                player.lock(2);
                GameWorld.getPulser().submit(new Pulse(1, player) {
                    @Override
                    public boolean pulse() {
                        player.getProperties().setTeleportLocation(Location.create(2985, 3316, 0));
                        return true;
                    }
                });
                return true;
            }
        });
        return super.newInstance(arg);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            Player player = killer.asPlayer();
            BossKillCounter.addToKillCount(player, this.getId());
        }
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new GiantMoleNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.GIANT_MOLE_3340};
    }

}
