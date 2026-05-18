package mike.dungeons.storage;

import lombok.Getter;
import mike.blueprint.config.Config;
import mike.blueprint.loader.Component;
import mike.dungeons.Dungeons;

@Getter
@Component
public class KeyStorage extends Config {

    private String endpoint = "";
    private String dataKey = "";

    public KeyStorage() {
        super(Dungeons.getInst(), "storage", "data-keys");
    }

}
