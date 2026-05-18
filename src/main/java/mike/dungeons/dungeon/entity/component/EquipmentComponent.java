package mike.dungeons.dungeon.entity.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mike.dungeons.dungeon.entity.DungeonEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

@Getter
public class EquipmentComponent implements ApplicableComponent {

    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack hand;
    private ItemStack offhand;

    public EquipmentComponent setHelmet(ItemStack helmet) {
        this.helmet = helmet;
        return this;
    }

    public EquipmentComponent setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
        return this;
    }

    public EquipmentComponent setLeggings(ItemStack leggings) {
        this.leggings = leggings;
        return this;
    }

    public EquipmentComponent setBoots(ItemStack boots) {
        this.boots = boots;
        return this;
    }

    public EquipmentComponent setHand(ItemStack hand) {
        this.hand = hand;
        return this;
    }

    public EquipmentComponent setOffhand(ItemStack offhand) {
        this.offhand = offhand;
        return this;
    }

    @Override
    public void apply(DungeonEntity dungeonEntity) {
        LivingEntity entity = dungeonEntity.getEntity();
        if(entity.getEquipment() == null) return;
        final EntityEquipment entityEquipment = entity.getEquipment();
        entityEquipment.setHelmet(helmet, true);
        entityEquipment.setChestplate(chestplate, true);
        entityEquipment.setLeggings(leggings, true);
        entityEquipment.setBoots(boots, true);
        entityEquipment.setItemInMainHand(hand, true);
        entityEquipment.setItemInOffHand(offhand, true);
    }

}
