package content.region.misthalin.varrock.quest.surok.plugin;

import core.game.activity.ActivityPlugin;
import core.game.activity.CutscenePlugin;
import core.game.dialogue.FaceAnim;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.build.DynamicRegion;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.net.packet.OutgoingContext;
import core.net.packet.PacketRepository;
import core.net.packet.OutgoingContext.CameraType;
import core.net.packet.out.CameraViewPacket;
import shared.consts.Music;
import shared.consts.Quests;

import static core.api.ContentAPIKt.removeAttribute;
import static core.api.ContentAPIKt.setAttribute;

/**
 * The type What lies below cutscene.
 */
public class WhatLiesBelowCutscene extends CutscenePlugin {

    private NPC surok;

    private NPC king;

    private NPC zaff;

    /**
     * Instantiates a new What lies below cutscene.
     */
    public WhatLiesBelowCutscene() {
        super(Quests.WHAT_LIES_BELOW);
    }

    /**
     * Instantiates a new What lies below cutscene.
     *
     * @param player the player
     */
    public WhatLiesBelowCutscene(Player player) {
        this();
        this.player = player;
        this.setUid(getName().hashCode());
    }

    @Override
    public boolean start(final Player player, boolean login, java.lang.Object... args) {
        player.lock();
        setAttribute(player, "cutscene", this);
        Scenery table = RegionManager.getObject(base.transform(9, 37, 0));
        if (table != null) {
            SceneryBuilder.remove(table);
        }
        player.getMusicPlayer().unlock(Music.SUROKS_THEME_250);
        player.getMusicPlayer().unlock(Music.COMPLICATION_142);
        surok = NPC.create(5835, base.transform(8, 39, 0));
        surok.init();
        king = KingRoaldNPC.create(5838, base.transform(10, 34, 0));
        king.lock();
        ((KingRoaldNPC) king).setCutscene(this);
        king.init();
        king.face(player);
        surok.face(king);
        player.face(surok);
        player.getDialogueInterpreter().open(5834, surok, this);
        return super.start(player, login, args);
    }

    @Override
    public void open() {
        player.lock();
        sendCamera(0, -6, 0, 0, 500, 100);
        surok.animate(Animation.create(1084));
        surok.graphics(Graphics.create(108));
        surok.sendChat("Annach Narh Hin Dei!");
        sendCamera(0, 3, 0, 0, 450, 95, 5);
        king.animate(Animation.create(860), 7);
        king.sendChat("What's going on?", 6);
        king.sendChat("I...must...kill..." + player.getUsername() + "!!", 9);
        player.sendChat("Uh oh! King Roald looks evil!", 13);
        GameWorld.getPulser().submit(new Pulse(13) {
            @Override
            public boolean pulse() {
                reset();
                player.face(king);
                player.getDialogueInterpreter().sendDialogues(player, FaceAnim.ANNOYED, "Uh oh! King Roald looks evil!");
                return true;
            }
        });
        super.open();
    }

    @Override
    public void end() {
        super.end();
        removeAttribute(player, "cutscene");
        player.getDialogueInterpreter().close();
        if (player.getDialogueInterpreter().getDialogue() != null) {
            player.getDialogueInterpreter().getDialogue().end();
        }
    }

    @Override
    public boolean interact(Entity entity, Node target, Option option) {
        if (entity instanceof Player) {
            switch (target.getId()) {
                case 5835:
                    if (player.getAttribute("can-arrest", false)) {
                        Location loc = base.transform(9, 34, 0);
                        PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, CameraType.POSITION, loc.getX(), loc.getY(), 450, 1, 100));
                        PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, CameraType.ROTATION, loc.getX(), loc.getY(), 450, 1, 100));
                        zaff.face(surok);
                        surok.face(zaff);
                        player.lock();
                        surok.animate(Animation.create(1084));
                        surok.sendChat("Mirra din namus!!", 1);
                        player.sendMessage("Surok looks like he's trying to teleport away!");
                        zaff.sendChat("Stop!!", 3);
                        surok.sendChat("Nooooooooooooo!", 6);
                        GameWorld.getPulser().submit(new Pulse(3, player) {

                            @Override
                            public boolean pulse() {
                                surok.animate(Animation.create(6098));
                                zaff.animate(Animation.create(1819));
                                Projectile.magic(zaff, surok, 109, 60, 36, 36, 10).send();
                                return true;
                            }

                        });
                        GameWorld.getPulser().submit(new Pulse(9, player) {

                            @Override
                            public boolean pulse() {
                                player.getDialogueInterpreter().open(5834, surok, this, true);
                                return true;
                            }

                        });
                        return true;
                    }
                    return true;
                case 11014:
                    if (option.name.equalsIgnoreCase("operate") || option.name.equalsIgnoreCase("summon")) {
                        if (king.getAttribute("message", false)) {
                            summonZaff();
                            return true;
                        }
                    }
                    break;
                case 15536:
                    return true;
            }
        }
        return super.interact(entity, target, option);
    }

    @Override
    public boolean teleport(Entity e, int type, Node node) {
        if (type != -1) {
            return false;
        }
        return super.teleport(e, type, node);
    }

    /**
     * Summon zaff.
     */
    public void summonZaff() {
        if (!king.getAttribute("message", false)) {
            player.sendMessage("Zaff isn't ready to be summoned.");
            return;
        }
        player.lock();
        king.lock();
        king.getProperties().getCombatPulse().stop();
        player.getProperties().getCombatPulse().stop();
        player.getInterfaceManager().removeTabs(getRemovedTabs());
        zaff = NPC.create(5836, player.getLocation());
        Location loc = RegionManager.getSpawnLocation(player, zaff);
        if (loc != null) {
            zaff.setLocation(loc);
        }
        zaff.init();
        zaff.graphics(Graphics.create(110));
        if (zaff.getLocation().equals(king.getLocation()) || zaff.getLocation().equals(surok.getLocation())) {
            zaff.moveStep();
        }
        zaff.face(king);
        king.face(zaff);
        zaff.animate(Animation.create(5633));
        zaff.sendChat("Sin danna nim borha!!", 2);
        king.animate(Animation.create(6099), 5);
        zaff.graphics(Graphics.create(108), 3);
        king.graphics(Graphics.create(110), 8);
        king.sendChat("Wh...!", 7);
        GameWorld.getPulser().submit(new Pulse(9, player) {

            @Override
            public boolean pulse() {
                king.clear();
                player.unlock();
                setAttribute(player, "can-arrest", true);
                player.getDialogueInterpreter().sendDialogues(zaff, null, "The king's mind has been restored to him and he has", "been teleported away to safety. Now, to deal with", "Surok!");
                return true;
            }

        });
    }

    /**
     * Send camera.
     *
     * @param x1     the x 1
     * @param y1     the y 1
     * @param x2     the x 2
     * @param y2     the y 2
     * @param height the height
     * @param speed  the speed
     * @param ticks  the ticks
     */
    public void sendCamera(final int x1, final int y1, final int x2, final int y2, final int height, final int speed, int ticks) {
        GameWorld.getPulser().submit(new Pulse(ticks, player) {

            @Override
            public boolean pulse() {
                sendCamera(x1, y1, x2, y2, height, speed);
                return true;
            }

        });
    }

    /**
     * Send camera.
     *
     * @param x1     the x 1
     * @param y1     the y 1
     * @param x2     the x 2
     * @param y2     the y 2
     * @param height the height
     * @param speed  the speed
     */
    public void sendCamera(int x1, int y1, int x2, int y2, int height, int speed) {
        PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, CameraType.POSITION, player.getLocation().getX() + x1, player.getLocation().getY() + y1, height, 1, speed));
        PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, CameraType.ROTATION, player.getLocation().getX() + x2, player.getLocation().getY() + y2, height, 1, speed));
    }

    /**
     * Reset.
     *
     * @param ticks the ticks
     */
    public void reset(int ticks) {
        GameWorld.getPulser().submit(new Pulse(1, player) {

            @Override
            public boolean pulse() {
                reset();
                return true;
            }

        });
    }

    /**
     * Reset.
     */
    public void reset() {
        PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, CameraType.RESET, 0, 0, 0, 0, 0));
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new WhatLiesBelowCutscene(p);
    }

    @Override
    public Location getStartLocation() {
        return base.transform(10, 38, 0);
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(12854);
        setRegionBase();
        registerRegion(region.getId());
    }

    /**
     * Gets surok.
     *
     * @return the surok
     */
    public NPC getSurok() {
        return surok;
    }

    /**
     * Sets surok.
     *
     * @param surok the surok
     */
    public void setSurok(NPC surok) {
        this.surok = surok;
    }

    /**
     * Gets king.
     *
     * @return the king
     */
    public NPC getKing() {
        return king;
    }

    /**
     * Sets king.
     *
     * @param king the king
     */
    public void setKing(NPC king) {
        this.king = king;
    }

    /**
     * Gets zaff.
     *
     * @return the zaff
     */
    public NPC getZaff() {
        return zaff;
    }

    /**
     * Sets zaff.
     *
     * @param zaff the zaff
     */
    public void setZaff(NPC zaff) {
        this.zaff = zaff;
    }

}
