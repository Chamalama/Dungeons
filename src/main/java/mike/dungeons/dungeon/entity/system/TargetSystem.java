package mike.dungeons.dungeon.entity.system;

import mike.blueprint.loader.Component;
import mike.blueprint.util.AbstractTask;
import mike.blueprint.util.TargetUtil;
import mike.dungeons.Dungeons;
import mike.dungeons.dungeon.Dungeon;
import mike.dungeons.dungeon.entity.DungeonEntity;
import mike.dungeons.dungeon.entity.DungeonMobs;
import mike.dungeons.dungeon.entity.component.TargetComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

@Component
public class TargetSystem extends AbstractTask {

    public TargetSystem() {
        super(Dungeons.getInst(), 0, 5, false);
    }

    @Override
    public void run() {
        for(UUID id : DungeonMobs.getComponentEntities(TargetComponent.class)) {
            final DungeonEntity dungeonEntity = DungeonMobs.getEntity(id);
            final TargetComponent targetComponent = dungeonEntity.getComponent(TargetComponent.class);
            if(!targetComponent.shouldRetarget()) continue;
            final LivingEntity dungeonEnt = dungeonEntity.getEntity();
            final LivingEntity target = TargetUtil.findClosestEntity(dungeonEnt, targetComponent.getTargetRadius(), targetComponent.getExcludedTargets().toArray(new NamespacedKey[0]));
            targetComponent.target(target);
        }
    }
}
