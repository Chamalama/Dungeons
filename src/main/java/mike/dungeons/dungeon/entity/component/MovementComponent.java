package mike.dungeons.dungeon.entity.component;

import lombok.Getter;
import lombok.Setter;
import mike.blueprint.pathfinder.Node;
import mike.blueprint.pathfinder.PathfinderContext;
import mike.blueprint.pathfinder.PathfinderState;
import mike.blueprint.util.MovementUtil;
import mike.dungeons.dungeon.entity.DungeonEntity;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.util.Vector;

import java.util.Queue;

@Getter
@Setter
public class MovementComponent {

    private double speed, stopDistance;
    private boolean usePathfinder;
    private long lastMoveTime;

    public MovementComponent(double speed, double stopDistance, boolean usePathfinder) {
        this.speed = speed;
        this.stopDistance = stopDistance;
        this.usePathfinder = usePathfinder;
        this.lastMoveTime = 0L;
    }

    public void move(LivingEntity dungeonEntity, Location location) {
        if(dungeonEntity == null || dungeonEntity.isDead()) return;
        MovementUtil.moveTo(dungeonEntity, location, stopDistance, speed);
    }

    public void moveAlongPath(LivingEntity entity, PathfinderState state) {
        final Queue<Node> nodes = state.getPath();
        final Node currNode = nodes.peek();
        if(currNode == null) return;
        final Location entityLocation = entity.getLocation();
        final Location nodeLocation = PathfinderContext.convertNodeToBukkit(currNode, entity.getWorld()).toCenterLocation();
        final Vector entityVelocity = nodeLocation.toVector().subtract(entityLocation.toVector());

        double yMulti;

        if(currNode.getY() > entity.getLocation().getBlockY()) {
            yMulti = 0.6055;
        }else{
            yMulti = -1.0;
        }

        entity.setVelocity(entityVelocity.normalize().multiply(speed).setY(yMulti));

        if(currNode.getDistance(entityLocation) <= 0.1) {
            nodes.poll();
        }
    }


    public void moveWithPathfinder(LivingEntity dungeonEntity, Location location) {
        if(dungeonEntity == null || dungeonEntity.isDead()) return;
        final long currTime = System.currentTimeMillis();
        if(currTime - lastMoveTime < 500) return;
        if(dungeonEntity instanceof Mob mob) {
            mob.getPathfinder().moveTo(location, speed);
            lastMoveTime = currTime;
        }
    }

}
