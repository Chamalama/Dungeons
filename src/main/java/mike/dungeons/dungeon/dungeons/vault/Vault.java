package mike.dungeons.dungeon.dungeons.vault;

import mike.blueprint.util.FastLocation;
import mike.dungeons.dungeon.Difficulty;
import mike.dungeons.dungeon.Dungeon;

public class Vault extends Dungeon {

    public Vault() {
        super("Vault", "dungeon_vault", Difficulty.NIGHTMARE);
        this.spawnLocation = new FastLocation("dungeon_vault", -69, 100, 56, -136, 0);
    }

}
