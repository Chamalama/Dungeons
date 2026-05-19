package mike.dungeons.dungeon;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import mike.blueprint.util.Text;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class DungeonEditItems {

    public static ItemStack getSpawnPositionItem() {
        final ItemStack stack = new ItemStack(Material.STICK);
        stack.setData(DataComponentTypes.CUSTOM_NAME, Text.translate("<yellow>Spawn Point Tool"));
        stack.setData(DataComponentTypes.LORE, ItemLore.lore(Text.translate(List.of("<gray>Right-Click to set the spawn point of", "<gray>this dungeon"))));
        stack.editPersistentDataContainer(persistentDataContainer -> {
            persistentDataContainer.set(DungeonKeys.SPAWN_POINT_ITEM, PersistentDataType.BYTE, (byte)1);
        });
        return stack;
    }

    public static ItemStack getTriggerPointItem() {
        final ItemStack stack = new ItemStack(Material.SLIME_BALL);
        stack.setData(DataComponentTypes.CUSTOM_NAME, Text.translate("<green>Trigger Point Tool"));
        stack.setData(DataComponentTypes.LORE, ItemLore.lore(Text.translate(List.of("<gray>Right-Click to set the encounter trigger point"))));
        stack.editPersistentDataContainer(persistentDataContainer -> {
            persistentDataContainer.set(DungeonKeys.TRIGGER_POINT_ITEM, PersistentDataType.BYTE, (byte)1);
        });
        return stack;
    }

    public static ItemStack getRegionItem() {
        final ItemStack stack = new ItemStack(Material.DIAMOND_AXE);
        stack.setData(DataComponentTypes.CUSTOM_NAME, Text.translate("<green>Region Tool"));
        stack.setData(DataComponentTypes.LORE, ItemLore.lore(Text.translate(List.of("<gray>Right-Click to set the first region position", "<gray>Left-Click to set the second region position"))));
        stack.editPersistentDataContainer(persistentDataContainer -> {
            persistentDataContainer.set(DungeonKeys.REGION_ITEM, PersistentDataType.BYTE, (byte)1);
        });
        return stack;
    }

    public static boolean isSpawnPointItem(ItemStack stack) {
        if(!stack.hasItemMeta()) return false;
        return stack.getItemMeta().getPersistentDataContainer().has(DungeonKeys.SPAWN_POINT_ITEM);
    }

    public static boolean isTriggerPointItem(ItemStack stack) {
        if(!stack.hasItemMeta()) return false;
        return stack.getItemMeta().getPersistentDataContainer().has(DungeonKeys.TRIGGER_POINT_ITEM);
    }

    public static boolean isRegionItem(ItemStack stack) {
        if(!stack.hasItemMeta()) return false;
        return stack.getItemMeta().getPersistentDataContainer().has(DungeonKeys.REGION_ITEM);
    }

}
