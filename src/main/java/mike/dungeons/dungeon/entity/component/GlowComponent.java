package mike.dungeons.dungeon.entity.component;

import mike.dungeons.dungeon.entity.DungeonEntity;

public class GlowComponent implements ApplicableComponent {

    public GlowComponent() {

    }

    @Override
    public void apply(DungeonEntity entity) {
        entity.getEntity().setGlowing(true);
    }
}
