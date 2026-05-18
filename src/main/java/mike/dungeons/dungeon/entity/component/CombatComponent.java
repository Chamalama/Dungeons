package mike.dungeons.dungeon.entity.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mike.dungeons.dungeon.entity.DungeonEntity;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

@Getter
@Setter
@AllArgsConstructor
public class CombatComponent implements ApplicableComponent {

    private double damage;
    private double armor;
    private double critChance;
    private double critMultiplier;

    public boolean shouldCrit() {
        return Math.random() <= critChance;
    }

    @Override
    public void apply(DungeonEntity entity) {
        entity.getEntity().registerAttribute(Attribute.ATTACK_DAMAGE);
        entity.getEntity().getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(damage);
    }
}
