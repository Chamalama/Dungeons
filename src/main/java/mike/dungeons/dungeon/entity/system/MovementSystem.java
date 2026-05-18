package mike.dungeons.dungeon.entity.system;

import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.math.Position;
import mike.blueprint.loader.Component;
import mike.blueprint.pathfinder.Node;
import mike.blueprint.pathfinder.PathfinderContext;
import mike.blueprint.util.AbstractTask;
import mike.dungeons.Dungeons;
import mike.dungeons.dungeon.entity.DungeonEntity;
import mike.dungeons.dungeon.entity.DungeonMobs;
import mike.dungeons.dungeon.entity.component.MovementComponent;
import mike.dungeons.dungeon.entity.component.TargetComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mannequin;

import java.util.UUID;

@Component
public class MovementSystem extends AbstractTask {

    public MovementSystem() {
        super(Dungeons.getInst(), 0, 1, true);
    }

    @Override
    public void run() {
        for(UUID id : DungeonMobs.getComponentEntities(MovementComponent.class)) {
            final DungeonEntity dungeonEntity = DungeonMobs.getEntity(id);
            if(dungeonEntity.getEntity() == null) continue;
            if(!dungeonEntity.hasComponent(TargetComponent.class)) continue;
            final MovementComponent movementComponent = dungeonEntity.getComponent(MovementComponent.class);
            final TargetComponent targetComponent = dungeonEntity.getComponent(TargetComponent.class);
            final Entity targetEntity = targetComponent.getTarget();
            final Location targetLocation = targetComponent.getLocationTarget();
            final LivingEntity le = dungeonEntity.getEntity();
            boolean usePathfinder = movementComponent.isUsePathfinder();
            if(targetEntity == null && targetLocation != null) {
                if(usePathfinder) {
                    if(le instanceof Mannequin) {
                        movementComponent.moveAlongPath(le, targetComponent.getTravelPath(le.getLocation()));
                    } else {
                        Bukkit.getScheduler().runTask(Dungeons.getInst(), () -> movementComponent.moveWithPathfinder(le, targetLocation));
                    }
                } else {
                    movementComponent.move(le, targetLocation);
                }
                le.lookAt(Position.fine(targetLocation), LookAnchor.EYES);
            } else {
                if (targetEntity != null) {
                    if(usePathfinder) {
                        if(le instanceof Mannequin) {
                            movementComponent.moveAlongPath(le, targetComponent.getTravelPath(le.getLocation()));
                        } else {
                            Bukkit.getScheduler().runTask(Dungeons.getInst(), () -> movementComponent.moveWithPathfinder(le, targetEntity.getLocation()));
                        }
                    } else {
                        movementComponent.move(le, targetEntity.getLocation());
                    }
                    le.lookAt(Position.fine(targetEntity.getLocation().clone().add(0, 1.5, 0)), LookAnchor.EYES);
                }
            }
        }
    }
}
