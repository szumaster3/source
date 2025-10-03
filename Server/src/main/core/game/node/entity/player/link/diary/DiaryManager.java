package core.game.node.entity.player.link.diary;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import content.global.skill.smithing.Bar;
import core.game.component.Component;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import shared.consts.Components;

/**
 * The Diary manager.
 */
public class DiaryManager {

    private final Diary[] diarys = new Diary[]{
        new Diary(DiaryType.KARAMJA),
        new Diary(DiaryType.VARROCK),
        new Diary(DiaryType.LUMBRIDGE),
        new Diary(DiaryType.FALADOR),
        new Diary(DiaryType.FREMENNIK),
        new Diary(DiaryType.SEERS_VILLAGE)
    };

    private final Player player;

    /**
     * Instantiates a new Diary manager.
     *
     * @param player the player
     */
    public DiaryManager(Player player) {
        this.player = player;
    }

    /**
     * Parse.
     *
     * @param data the data
     */
    public void parse(JsonArray data) {
        for (JsonElement element : data) {
            if (!element.isJsonObject()) continue;

            JsonObject diary = element.getAsJsonObject();

            if (diary.entrySet().isEmpty()) continue;
            String rawName = diary.entrySet().iterator().next().getKey();

            String name = rawName.replace("_", "' ");

            for (int i = 0; i < diarys.length; i++) {
                if (diarys[i].getType().getName().equalsIgnoreCase(name)) {
                    String jsonKey = name.replace("' ", "_");

                    JsonElement toParseElement = diary.get(jsonKey);
                    if (toParseElement != null && toParseElement.isJsonObject()) {
                        JsonObject toParse = toParseElement.getAsJsonObject();
                        diarys[i].parse(toParse);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Open tab.
     */
    public void openTab() {
        player.getInterfaceManager().openTab(2, new Component(Components.AREA_TASK_259));
        for (Diary diary : diarys) {
            diary.drawStatus(player);
        }
    }

    /**
     * Update task.
     *
     * @param player   the player
     * @param type     the type
     * @param level    the level
     * @param index    the index
     * @param complete the complete
     */
    public void updateTask(Player player, DiaryType type, int level, int index, boolean complete) {
        getDiary(type).updateTask(player, level, index, complete);
    }

    /**
     * Finish task.
     *
     * @param player the player
     * @param type   the type
     * @param level  the level
     * @param index  the index
     */
    public void finishTask(Player player, DiaryType type, int level, int index) {
        if (!player.isArtificial()) {
            getDiary(type).finishTask(player, level, index);
        }
    }

    /**
     * Has completed task boolean.
     *
     * @param type  the type
     * @param level the level
     * @param index the index
     * @return the boolean
     */
    public boolean hasCompletedTask(DiaryType type, int level, int index) {
        return getDiary(type).isComplete(level, index);
    }

    /**
     * Sets started.
     *
     * @param type  the type
     * @param level the level
     */
    public void setStarted(DiaryType type, int level) {
        getDiary(type).setLevelStarted(level);
    }

    /**
     * Sets completed.
     *
     * @param type  the type
     * @param level the level
     * @param index the index
     */
    public void setCompleted(DiaryType type, int level, int index) {
        getDiary(type).setCompleted(level, index);
    }

    /**
     * Gets diary.
     *
     * @param type the type
     * @return the diary
     */
    public Diary getDiary(DiaryType type) {
        if (type == null) {
            return null;
        }
        for (Diary diary : diarys) {
            if (diary.getType() == type) {
                return diary;
            }
        }
        return null;
    }

    /**
     * Gets karamja glove.
     *
     * @return the karamja glove
     */
    public int getKaramjaGlove() {
        if (!hasGlove()) {
            return -1;
        }
        for (int i = 0; i < 3; i++) {
            if (player.getEquipment().containsItem(DiaryType.KARAMJA.getRewards()[i][0])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets armour.
     *
     * @return the armour
     */
    public int getArmour() {
        if (!hasArmour()) {
            return -1;
        }
        for (int i = 0; i < 3; i++) {
            if (player.getEquipment().containsItem(DiaryType.VARROCK.getRewards()[i][0])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets headband.
     *
     * @return the headband
     */
    public int getHeadband() {
        if (!hasHeadband()) {
            return -1;
        }
        for (int i = 0; i < 3; i++) {
            if (player.getEquipment().containsItem(DiaryType.SEERS_VILLAGE.getRewards()[i][0])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check mining reward boolean.
     *
     * @param reward the reward
     * @return the boolean
     */
    public boolean checkMiningReward(int reward) {
        int level = player.getAchievementDiaryManager().getArmour();
        if (level == -1) {
            return false;
        }
        if (reward == 453) {
            return true;
        }
        return level == 0 && reward <= 442 || level == 1 && reward <= 447 || level == 2 && reward <= 449;
    }

    /**
     * Check smith reward boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public boolean checkSmithReward(Bar type) {
        int level = player.getAchievementDiaryManager().getArmour();
        if (level == -1) {
            return false;
        }
        return level == 0 && type.ordinal() <= Bar.STEEL.ordinal() || level == 1 && type.ordinal() <= Bar.MITHRIL.ordinal() || level == 2 && type.ordinal() <= Bar.ADAMANT.ordinal();
    }

    /**
     * Has glove boolean.
     *
     * @return the boolean
     */
    public boolean hasGlove() {
        Item glove = player.getEquipment().get(EquipmentContainer.SLOT_HANDS);
        return glove != null && (glove.getId() == DiaryType.KARAMJA.getRewards()[0][0].getId() || glove.getId() == DiaryType.KARAMJA.getRewards()[1][0].getId() || glove.getId() == DiaryType.KARAMJA.getRewards()[2][0].getId());
    }

    /**
     * Has armour boolean.
     *
     * @return the boolean
     */
    public boolean hasArmour() {
        Item plate = player.getEquipment().get(EquipmentContainer.SLOT_CHEST);
        return plate != null && (plate.getId() == DiaryType.VARROCK.getRewards()[0][0].getId() || plate.getId() == DiaryType.VARROCK.getRewards()[1][0].getId() || plate.getId() == DiaryType.VARROCK.getRewards()[2][0].getId());
    }

    /**
     * Has Seer's headband.
     *
     * @return the boolean
     */
    public boolean hasHeadband() {
        Item hat = player.getEquipment().get(EquipmentContainer.SLOT_HAT);
        return hat != null && (hat.getId() == DiaryType.SEERS_VILLAGE.getRewards()[0][0].getId() || hat.getId() == DiaryType.SEERS_VILLAGE.getRewards()[1][0].getId() || hat.getId() == DiaryType.SEERS_VILLAGE.getRewards()[2][0].getId());
    }

    /**
     * Is complete boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public boolean isComplete(DiaryType type) {
        return diarys[type.ordinal()].isComplete();
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get diarys diary.
     *
     * @return the diary
     */
    public Diary[] getDiarys() {
        return diarys;
    }

    /**
     * Reset rewards.
     */
    public void resetRewards() {
        for (Diary diary : diarys) {
            for (Item[] axis : diary.getType().getRewards()) {
                for (Item item : axis) {
                    if (player.getInventory().containsItem(item)) {
                        player.getInventory().remove(item);
                    }
                    if (player.getBank().containsItem(item)) {
                        player.getBank().remove(item);
                    }
                    if (player.getEquipment().containsItem(item)) {
                        player.getEquipment().remove(item);
                    }
                }
            }
        }
    }
}
