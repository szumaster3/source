package core.game.node.entity.combat.equipment.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import shared.consts.Items;
import shared.consts.Sounds;

import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * Handles the Shatter special attack.
 *
 * @author Emperor
 */
@Initializable
public final class ShatterSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 25;
    private static final Animation ANIMATION = new Animation(1060, Priority.HIGH);
    private static final Graphics GRAPHICS = new Graphics(shared.consts.Graphics.DRAGON_MACE_SPECIAL_251, 96);

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.MELEE.getSwingHandler().register(Items.DRAGON_MACE_1434, this);
        CombatStyle.MELEE.getSwingHandler().register(Items.DRAGON_MACE_13479, this);
        return this;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        state.setStyle(CombatStyle.MELEE);
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.25, 1.0)) {
            hit = RandomFunction.random(calculateHit(entity, victim, 1.5) + 1);
        }
        state.setEstimatedHit(hit);
        return 1;
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        playGlobalAudio(entity.getLocation(), Sounds.SHATTER_2541);
        entity.visualize(ANIMATION, GRAPHICS);
    }
}
