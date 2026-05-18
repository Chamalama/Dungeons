package mike.dungeons.dungeon.dungeons.outpost.component;

import mike.dungeons.dungeon.component.EventComponent;
import mike.dungeons.dungeon.component.GenericEventComponent;
import mike.dungeons.dungeon.team.DungeonTeam;

public class GateCaptureComponent implements EventComponent {

    public GateCaptureComponent() {

    }

    @Override
    public void tick(DungeonTeam team, GenericEventComponent genericEventComponent) {

    }

    @Override
    public void startEvent(DungeonTeam dungeonTeam) {

    }

    @Override
    public void stopEvent(DungeonTeam dungeonTeam) {

    }

    @Override
    public long tickTime() {
        return 20;
    }

    @Override
    public boolean startOnEnter() {
        return false;
    }

}
