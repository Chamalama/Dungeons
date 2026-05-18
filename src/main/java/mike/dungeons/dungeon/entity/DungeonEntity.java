package mike.dungeons.dungeon.entity;

import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.destroystokyo.paper.entity.ai.MobGoals;
import com.destroystokyo.paper.entity.ai.VanillaGoal;
import lombok.Getter;
import mike.dungeons.Dungeons;
import mike.dungeons.dungeon.entity.component.*;
import mike.dungeons.dungeon.team.DungeonTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
public abstract class DungeonEntity {

    private final String entityName;
    private final EntityType entityType;
    private final Map<Class<?>, Object> components = new HashMap<>();
    private LivingEntity cached;

    private UUID id;

    public DungeonEntity(String entityName, EntityType entityType) {
        this.entityName = entityName;
        this.entityType = entityType;
        this.id = null;
    }

    public <T> DungeonEntity addComponent(T component) {
        components.put(component.getClass(), component);
        return this;
    }

    public <T> T getComponent(Class<T> clazz) {
        return clazz.cast(components.get(clazz));
    }

    public <T> boolean hasComponent(Class<T> clazz) {
        return components.containsKey(clazz);
    }

    public void spawn(Location location, DungeonTeam dungeonTeam, Consumer<LivingEntity> callback) {
        final LivingEntity dungeonEntity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
        id = dungeonEntity.getUniqueId();
        if(dungeonEntity instanceof Mob mob) {
            Bukkit.getMobGoals().removeAllGoals(mob);
        }
        if(hasComponent(GenericAIComponent.class)) {
            getComponent(GenericAIComponent.class).getCurrentState().start(this);
        }
        for(Class<?> component : components.keySet()) {
            DungeonMobs.registerComponent(component, this);
            final Object o = getComponent(component);
            if(o instanceof ApplicableComponent applicableComponent) {
                applicableComponent.apply(this);
            }
        }
        dungeonTeam.getEncounterData().addEntity(id);
        DungeonMobs.registerToTeam(this, dungeonTeam);
        DungeonMobs.register(this);
        callback.accept(dungeonEntity);
    }

    public LivingEntity getEntity() {
        if(cached != null && cached.isValid() && !cached.isDead()) {
            return cached;
        }
        Bukkit.getScheduler().runTask(Dungeons.getInst(), () -> cached = (LivingEntity) Bukkit.getEntity(id));
        return cached;
    }

}
