package mike.dungeons.storage;

import lombok.Getter;
import mike.blueprint.config.Config;
import mike.blueprint.loader.Component;
import mike.blueprint.util.FastLocation;
import mike.dungeons.Dungeons;

@Getter
@Component
public class SpawnStorage extends Config {

    private FastLocation spawnLocation = new FastLocation("world", 0, 100, 0);

    public SpawnStorage() {
        super(Dungeons.getInst(), "storage", "spawn-location");
    }

}
