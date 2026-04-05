package mike.dungeons.dungeon.entity.component;

import lombok.Getter;
import lombok.Setter;
import mike.blueprint.util.MovementUtil;
import mike.dungeons.dungeon.entity.DungeonEntity;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

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
