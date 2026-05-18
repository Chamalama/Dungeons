package mike.dungeons.dungeon.entity.component;

import lombok.Getter;
import lombok.Setter;
import mike.dungeons.dungeon.entity.DungeonEntity;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

@Getter
@Setter
public class ScaleComponent implements ApplicableComponent {

    private double scale;

    public ScaleComponent(double scale) {
        this.scale = scale;
    }

    @Override
    public void apply(DungeonEntity entity) {
        entity.getEntity().getAttribute(Attribute.SCALE).setBaseValue(scale);
    }

}
