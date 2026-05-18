package mike.dungeons.dungeon.entity;

import lombok.Getter;
import mike.dungeons.dungeon.entity.component.GenericAIComponent;
import mike.dungeons.dungeon.entity.type.GateGuard;
import mike.dungeons.dungeon.entity.type.GateKeeper;
import mike.dungeons.dungeon.team.DungeonTeam;
import org.bukkit.entity.Entity;

import java.util.*;
import java.util.function.Supplier;

@Getter
public enum DungeonMobs {

    GATE_GUARD(() -> new GateGuard()),
    GATE_KEEPER(() -> new GateKeeper());

    private final Supplier<DungeonEntity> entitySupplier;

    @Getter
    public static final Map<UUID, DungeonEntity> activeEntities = new HashMap<>();
    public static final Map<Class<?>, Set<UUID>> componentEntities = new HashMap<>();
    public static final Map<DungeonEntity, DungeonTeam> entityToTeam = new HashMap<>();

    DungeonMobs(Supplier<DungeonEntity> entitySupplier) {
        this.entitySupplier = entitySupplier;
    }

    public static void registerComponent(Class<?> component, DungeonEntity entity) {
        componentEntities.computeIfAbsent(component, k -> new HashSet<>()).add(entity.getId());
    }

    public static void register(DungeonEntity entity) {
        activeEntities.put(entity.getId(), entity);
    }

    public static void registerToTeam(DungeonEntity entity, DungeonTeam team) {
        entityToTeam.put(entity, team);
    }

    public static void unregister(Entity le) {
        final UUID id = le.getUniqueId();
        final DungeonEntity entity = activeEntities.get(id);
        activeEntities.remove(id);
        entityToTeam.remove(entity);
        if (entity != null) {
            if(entity.hasComponent(GenericAIComponent.class)) {
                entity.getComponent(GenericAIComponent.class).getCurrentState().stop(entity);
            }
            componentEntities.values().forEach(dungeonEntities -> dungeonEntities.remove(id));
        }
    }

    public static DungeonTeam getEntitiesTeam(DungeonEntity entity) {
        return entityToTeam.get(entity);
    }

    public static Set<UUID> getComponentEntities(Class<?> component) {
        return componentEntities.getOrDefault(component, Collections.emptySet());
    }

    public static Collection<DungeonEntity> getEntities() {
        return activeEntities.values();
    }

    public static DungeonEntity getEntity(UUID uuid) {
        return activeEntities.get(uuid);
    }

    public DungeonEntity spawnEntity() {
        return this.getEntitySupplier().get();
    }

}
