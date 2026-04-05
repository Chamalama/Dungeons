package mike.dungeons.dungeon.entity.type;

import mike.dungeons.dungeon.DungeonKeys;
import mike.dungeons.dungeon.entity.DungeonEntity;
import mike.dungeons.dungeon.entity.component.*;
import mike.dungeons.dungeon.entity.type.component.KeeperComponent;
import org.bukkit.entity.EntityType;

import java.util.Set;

public class GateKeeper extends DungeonEntity {

    public GateKeeper() {
        super("Gate Keeper", EntityType.COPPER_GOLEM);
        this.addComponent(new HealthComponent(150))
                .addComponent(new ScaleComponent(2.25))
                .addComponent(new TargetComponent(16, 1000, Set.of(DungeonKeys.DUNGEON_MOB)))
                .addComponent(new MovementComponent(1.5, 6.0, true))
                .addComponent(new TagComponent(DungeonKeys.DUNGEON_MOB))
                .addComponent(new GenericAIComponent(new KeeperComponent()));
    }

}
