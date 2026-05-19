package mike.dungeons.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import mike.blueprint.loader.Component;
import mike.blueprint.util.FastLocation;
import mike.blueprint.util.Region;
import mike.blueprint.util.Text;
import mike.dungeons.Dungeons;
import mike.dungeons.dungeon.*;
import mike.dungeons.service.DungeonService;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Component
public class DungeonEditListener implements Listener {

    private final DungeonService dungeonService;

    public DungeonEditListener(DungeonService dungeonService) {
        this.dungeonService = dungeonService;
    }

    @EventHandler
    public void onEditPositions(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack inHand = player.getInventory().getItemInMainHand();
        if(inHand.isEmpty() || event.getHand() != EquipmentSlot.HAND) return;
        final Block block = event.getClickedBlock();
        if(block == null) return;
        final DungeonRoom room = dungeonService.getEditing(player);
        if(room == null) return;
        final Location blockLocation = block.getLocation();
        if(event.getAction().isRightClick()) {
            if (DungeonEditItems.isSpawnPointItem(inHand)) {
                room.setEncounterSpawnPoint(new FastLocation(blockLocation));
                room.update();
                player.sendMessage(Text.translate("<yellow><b>(!)</b> Set the spawn point to " + blockLocation.getBlockX() + "X, " + blockLocation.getBlockY() + "Y, " + blockLocation.getBlockZ() + "Z."));
                player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.0F, 1.5F);
                return;
            }
            if (DungeonEditItems.isTriggerPointItem(inHand)) {
                room.setStartPoint(new TriggerPoint(new FastLocation(blockLocation), 16));
                room.update();
                player.sendMessage(Text.translate("<yellow><b>(!)</b> Set the trigger point to " + blockLocation.getBlockX() + "X, " + blockLocation.getBlockY() + "Y, " + blockLocation.getBlockZ() + "Z."));
                player.sendMessage(Text.translate("<gray> Initial trigger radius is 16"));
                player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.0F, 0.8F);
                return;
            }
        }
        if(DungeonEditItems.isRegionItem(inHand)) {
            boolean rightClick = event.getAction().isRightClick();
            final Location[] currentPositions = dungeonService.getRegionPositions().get(player.getUniqueId());
            if(rightClick) {
                currentPositions[0] = blockLocation;
            }else{
                currentPositions[1] = blockLocation;
            }
            if(currentPositions[0] != null && currentPositions[1] != null) {
                room.setRoomRegion(new DungeonRegion(room.getOwningDungeon().getWorldName(), new Region(currentPositions[0], currentPositions[1])));
                player.sendMessage(Text.translate("<yellow><b>(!)</b> Set the encounter region!"));
                player.playSound(player, Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 1.0F, 0.5F);
                room.update();
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        final Item item = event.getItemDrop();
        final ItemStack stack = item.getItemStack();
        if(DungeonEditItems.isTriggerPointItem(stack) || DungeonEditItems.isSpawnPointItem(stack) || DungeonEditItems.isRegionItem(stack)) {
            player.sendMessage(Text.translate("<red><b>(!)</b> You cannot drop this item!"));
            player.sendMessage(Text.translate("<gray> Use /dungeon stopedit to clear the items..."));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final ItemStack cursor = event.getCursor();
        if(DungeonEditItems.isTriggerPointItem(cursor) || DungeonEditItems.isSpawnPointItem(cursor) || DungeonEditItems.isRegionItem(cursor)) {
            final Inventory inventory = event.getClickedInventory();
            if(inventory != player.getInventory()) {
                event.setCancelled(true);
                player.sendMessage(Text.translate("<red><b>(!)</b> You cannot move this item out of your inventory!"));
                player.sendMessage(Text.translate("<gray> Use /dungeon stopedit to clear the items..."));
            }
        }
    }

}
