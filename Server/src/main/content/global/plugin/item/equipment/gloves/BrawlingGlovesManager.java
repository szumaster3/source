package content.global.plugin.item.equipment.gloves;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import core.api.LoginListener;
import core.api.PersistPlayer;
import core.cache.def.impl.ItemDefinition;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static core.api.ContentAPIKt.setAttribute;

/**
 * The type Brawling gloves manager.
 */
public class BrawlingGlovesManager implements LoginListener, PersistPlayer {

    /**
     * The Player.
     */
    final Player player;

    /**
     * The Glove charges.
     */
    public HashMap<Integer, Integer> GloveCharges = new HashMap<Integer, Integer>();

    /**
     * Instantiates a new Brawling gloves manager.
     *
     * @param player the player
     */
    public BrawlingGlovesManager(Player player) {
        this.player = player;
    }

    /**
     * Instantiates a new Brawling gloves manager.
     */
    public BrawlingGlovesManager() {
        this.player = null;
    }

    @Override
    public void login(@NotNull Player player) {
        BrawlingGlovesManager instance = new BrawlingGlovesManager(player);
        setAttribute(player, "bg-manager", instance);
    }

    @Override
    public void parsePlayer(@NotNull Player player, @NotNull JsonObject data) {
        BrawlingGlovesManager instance = getInstance(player);
        if (data.has("brawlingGloves")) {
            JsonArray bgData = data.getAsJsonArray("brawlingGloves");
            for (JsonElement bg : bgData) {
                JsonObject glove = bg.getAsJsonObject();
                int gloveId = Integer.parseInt(glove.get("gloveId").getAsString());
                int charges = Integer.parseInt(glove.get("charges").getAsString());
                instance.registerGlove(BrawlingGloves.forIndicator(gloveId).id, charges);
            }
        }
    }

    @Override
    public void savePlayer(@NotNull Player player, @NotNull JsonObject save) {
        BrawlingGlovesManager instance = getInstance(player);
        if (instance.GloveCharges.size() > 0) {
            JsonArray brawlingGloves = new JsonArray();
            for (Map.Entry<Integer, Integer> glove : instance.GloveCharges.entrySet()) {
                JsonObject bGlove = new JsonObject();
                bGlove.addProperty("gloveId", Integer.toString(BrawlingGloves.forId(glove.getKey()).indicator));
                bGlove.addProperty("charges", Integer.toString(glove.getValue()));
                brawlingGloves.add(bGlove);
            }
            save.add("brawlingGloves", brawlingGloves);
        }
    }

    /**
     * Register glove.
     *
     * @param id the id
     */
    public void registerGlove(int id) {
        try {
            registerGlove(id, Objects.requireNonNull(BrawlingGloves.forId(id)).charges);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Register glove.
     *
     * @param id      the id
     * @param charges the charges
     */
    public void registerGlove(int id, int charges) {
        GloveCharges.putIfAbsent(id, charges);
    }

    /**
     * Update charges.
     *
     * @param glove   the glove
     * @param charges the charges
     */
    public void updateCharges(int glove, int charges) {
        if (GloveCharges.get(glove) != null) {
            if (GloveCharges.get(glove) - charges <= 0) {
                GloveCharges.remove(glove);
                player.getEquipment().remove(new Item(glove));
                player.getPacketDispatch().sendMessage("<col=ff0000>You use the last charge of your " + ItemDefinition.forId(glove).getName() + " and they vanish.</col>");
                return;
            }
            int currentCharges = GloveCharges.get(glove);
            GloveCharges.replace(glove, currentCharges - charges);
            player.debug("Charges remaining: " + (currentCharges - 1));
            if ((currentCharges - 1) % 50 == 0) {
                player.getPacketDispatch().sendMessage("<col=1fbd0d>Charges remaining: " + GloveCharges.get(glove));
            }
        }
    }

    /**
     * Gets experience bonus.
     *
     * @return the experience bonus
     */
    public double getExperienceBonus() {
        double bonus;
        int level = player.getSkullManager().getLevel();
        if (level > 0) {
            bonus = 3.0;
        } else {
            bonus = 0.5;
        }
        return bonus;
    }

    /**
     * Gets instance.
     *
     * @param player the player
     * @return the instance
     */
    public static BrawlingGlovesManager getInstance(Player player) {
        return player.getAttribute("bg-manager", new BrawlingGlovesManager());
    }
}
