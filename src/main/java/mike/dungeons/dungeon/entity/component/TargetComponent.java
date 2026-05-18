package mike.dungeons.dungeon.entity.component;

import lombok.Getter;
import mike.blueprint.pathfinder.PathfinderContext;
import mike.blueprint.pathfinder.PathfinderState;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Set;

@Getter
public class TargetComponent {

    private final double targetRadius;
    private final long retargetTime;
    private final Set<NamespacedKey> excludedTargets;
    private Entity target;
    private Location locationTarget;
    private PathfinderState path;
    private long lastTargetTime;

    public TargetComponent(double targetRadius, long retargetTime, Set<NamespacedKey> excludedTargets) {
        this.targetRadius = targetRadius;
        this.retargetTime = retargetTime;
        this.excludedTargets = excludedTargets;
        this.target = null;
        this.locationTarget = null;
        this.lastTargetTime = 0L;
        this.path = null;
    }

    public boolean shouldRetarget() {
        if(target == null) return true;
        return System.currentTimeMillis() - lastTargetTime >= retargetTime;
    }

    public PathfinderState getTarget(Location current) {
        if(target != null && target.isValid()) return new PathfinderContext(new PathfinderState()).buildPath(current, target.getLocation());
        return new PathfinderContext(new PathfinderState()).buildPath(current, locationTarget);
    }

    public PathfinderState getTravelPath(Location current) {
        if(path != null && !shouldRetarget()) return path;
        if(path != null && !path.getPath().isEmpty()) return path;
        final PathfinderState result = getTarget(current);
        if(result != null) {
            path = result;
        }
        return path;
    }

    public void target(LivingEntity entity) {
        this.target = entity;
        this.lastTargetTime = System.currentTimeMillis();
        this.locationTarget = null;
    }

    public void target(Location location) {
        if(target != null) return;
        this.locationTarget = location;
    }

}
