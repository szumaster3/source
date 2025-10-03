package content.global.skill.magic.spells.lunar;

import content.global.skill.magic.spells.LunarSpells;
import core.game.component.CloseEvent;
import core.game.component.Component;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.spell.MagicSpell;
import core.game.node.entity.combat.spell.Runes;
import core.game.node.entity.combat.spell.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import shared.consts.Animations;
import shared.consts.Components;
import shared.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;

/**
 * The Stat spy spell.
 */
@Initializable
public final class StatSpySpell extends MagicSpell {

    private final static Animation ANIMATION = new Animation(Animations.LUNAR_STATSPY_6293);
    private static final Graphics GRAPHICS = new Graphics(734, 120);
    private static final Graphics EYE = new Graphics(shared.consts.Graphics.STAT_SPY_GFX_1059);
    private static final Component COMPONENT = new Component(Components.DREAM_PLAYER_STATS_523);
    private static final int[][] SKILLS = {{Skills.ATTACK, 1, 2}, {Skills.HITPOINTS, 5, 6}, {Skills.MINING, 9, 10}, {Skills.STRENGTH, 13, 14}, {Skills.AGILITY, 17, 18}, {Skills.SMITHING, 21, 22}, {Skills.DEFENCE, 25, 26}, {Skills.HERBLORE, 29, 30}, {Skills.FISHING, 33, 34}, {Skills.RANGE, 37, 38}, {Skills.THIEVING, 41, 42}, {Skills.COOKING, 45, 46}, {Skills.PRAYER, 49, 50}, {Skills.CRAFTING, 53, 54}, {Skills.FIREMAKING, 57, 58}, {Skills.MAGIC, 61, 62}, {Skills.FLETCHING, 65, 66}, {Skills.WOODCUTTING, 69, 70}, {Skills.RUNECRAFTING, 73, 74}, {Skills.SLAYER, 77, 78}, {Skills.FARMING, 81, 82}, {Skills.CONSTRUCTION, 85, 86}, {Skills.HUNTER, 89, 90}, {Skills.SUMMONING, 93, 94}};

    /**
     * Instantiates a new Stat spy spell.
     */
    public StatSpySpell() {
        super(SpellBook.LUNAR, 75, 76, ANIMATION, null, null, new Item[]{new Item(Runes.COSMIC_RUNE.getId(), 2), new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.BODY_RUNE.getId(), 5)});
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.LUNAR.register(LunarSpells.STAT_SPY, this);
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        final Player player = ((Player) entity);
        if (!(target instanceof Player)) {
            player.getPacketDispatch().sendMessage("You can only cast this spell on players.");
            return false;
        }
        if (super.meetsRequirements(player, true, true)) {
            final Player o = ((Player) target);
            player.animate(ANIMATION);
            player.face(o);
            playAudio(player, Sounds.LUNAR_STAT_SPY_3620);
            COMPONENT.setCloseEvent(new CloseEvent() {
                @Override
                public boolean close(Player player, Component c) {
                    player.getInterfaceManager().restoreTabs();
                    return true;
                }
            });
            player.graphics(EYE);
            o.graphics(GRAPHICS);
            playAudio(player, Sounds.LUNAR_STAT_SPY_IMPACT_3621);
            for (int[] element : SKILLS) {
                player.getPacketDispatch().sendString("" + o.getSkills().getLevel(element[0]), 523, element[1]);
                player.getPacketDispatch().sendString("" + o.getSkills().getStaticLevel(element[0]), 523, element[2]);
            }
            player.getPacketDispatch().sendString((o.getUsername()), 523, 99);
            player.getInterfaceManager().openSingleTab(COMPONENT);
            return true;
        }
        return true;
    }

}
