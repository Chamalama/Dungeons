package mike.dungeons.dungeon.entity.type;

import mike.dungeons.dungeon.entity.DungeonEntity;
import mike.dungeons.dungeon.DungeonKeys;
import mike.dungeons.dungeon.entity.component.*;
import mike.dungeons.dungeon.entity.type.component.RoamComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class GateGuard extends DungeonEntity {

    public GateGuard() {
        super("Gate Guard", EntityType.SKELETON);
        this.addComponent(new HealthComponent(100))
                .addComponent(new SkinComponent("knight", "knight_cape"))
                .addComponent(new EquipmentComponent()
                        .setHelmet(ItemStack.of(Material.RED_STAINED_GLASS))
                        .setChestplate(ItemStack.of(Material.IRON_CHESTPLATE))
                        .setLeggings(ItemStack.of(Material.IRON_LEGGINGS))
                        .setBoots(ItemStack.of(Material.IRON_BOOTS))
                        .setHand(ItemStack.of(Material.IRON_SWORD)))
                .addComponent(new TargetComponent(19, 100, Set.of(DungeonKeys.DUNGEON_MOB)))
                .addComponent(new TagComponent(DungeonKeys.DUNGEON_MOB))
                .addComponent(new MovementComponent(0.8, 3.5, true))
                .addComponent(new CombatComponent(6.0, 1.0, 0.05, 1.2))
                .addComponent(new ScaleComponent(1.05))
                .addComponent(new AttackComponent(1500, 7.5, Sound.ENTITY_IRON_GOLEM_ATTACK));

    }

}
