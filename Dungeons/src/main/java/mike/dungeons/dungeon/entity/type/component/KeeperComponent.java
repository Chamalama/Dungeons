package mike.dungeons.dungeon.entity.type.component;

import mike.dungeons.dungeon.entity.DungeonEntity;
import mike.dungeons.dungeon.entity.component.AIState;
import mike.dungeons.dungeon.entity.component.GenericAIComponent;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Transformation;

public class KeeperComponent implements AIState {

    private BlockDisplay keeperShield;
    private Interaction shieldInteraction;

    public KeeperComponent() {

    }

    @Override
    public void tick(DungeonEntity entity, GenericAIComponent component) {
        final Transformation transformation = keeperShield.getTransformation();
        keeperShield.setInterpolationDelay(5);
        keeperShield.setInterpolationDuration(5);
        transformation.getRightRotation().rotateY(2);
        keeperShield.setTransformation(transformation);
    }

    @Override
    public void start(DungeonEntity entity) {
        final LivingEntity le = entity.getCached();
        this.keeperShield = (BlockDisplay) le.getWorld().spawnEntity(le.getLocation(), EntityType.BLOCK_DISPLAY);
        this.shieldInteraction = (Interaction) le.getWorld().spawnEntity(le.getLocation(), EntityType.INTERACTION);
        this.keeperShield.setBlock(Material.WHITE_STAINED_GLASS.createBlockData());
        le.addPassenger(keeperShield);
        le.addPassenger(shieldInteraction);
        final Transformation transformation = keeperShield.getTransformation();
        transformation.getTranslation().sub(0, 2, 0);
        transformation.getScale().add(2, 2, 2);
    }

    @Override
    public void stop(DungeonEntity entity) {

    }

    @Override
    public void reset(DungeonEntity entity) {

    }
}
