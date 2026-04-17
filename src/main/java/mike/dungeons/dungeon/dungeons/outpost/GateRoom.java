package mike.dungeons.dungeon.dungeons.outpost;

import mike.blueprint.util.ExpiringTask;
import mike.blueprint.util.FastLocation;
import mike.blueprint.util.TitleUtil;
import mike.dungeons.Dungeons;
import mike.dungeons.dungeon.DungeonRegistry;
import mike.dungeons.dungeon.DungeonRoom;
import mike.dungeons.dungeon.EntityLocation;
import mike.dungeons.dungeon.component.GenericEventComponent;
import mike.dungeons.dungeon.component.KillComponent;
import mike.dungeons.dungeon.component.SpawnComponent;
import mike.dungeons.dungeon.dungeons.outpost.component.GateCaptureComponent;
import mike.dungeons.dungeon.entity.DungeonMobs;
import mike.dungeons.dungeon.team.DungeonTeam;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GateRoom extends DungeonRoom {

    private static final transient String OUTPOST_WORLD = DungeonRegistry.dungeonWorldName(Outpost.class);

    public GateRoom() {
        super(DungeonRegistry.getDungeon(Outpost.class), "GATE_ROOM");
        this.encounterTime = 180L;
        this.addComponent(new SpawnComponent(DungeonMobs.GATE_GUARD::spawnEntity, List.of(
                EntityLocation.of(new FastLocation(OUTPOST_WORLD, -607, 214, 121), 4, 30000),
                EntityLocation.of(new FastLocation(OUTPOST_WORLD, -631, 214, 34), 4, 30000),
                EntityLocation.of(new FastLocation(OUTPOST_WORLD, -637, 217, 83), 4, 30000)
        )));
        this.addComponent(new GenericEventComponent(new GateCaptureComponent()));
    }

    @Override
    public void startEncounter(DungeonTeam dungeonTeam) {
        dungeonTeam.getEncounterData().setEncounterStarted(true);
        final List<Player> team = dungeonTeam.getPlayers(true);
        final AtomicReference<Float> pitch = new AtomicReference<>(0.9F);
        new ExpiringTask(Dungeons.getInst()).runCount(3).setAsync(true).setAction(() -> {
            for(final Player player : team) {
                player.playSound(player, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.8F, pitch.getAndSet(pitch.get() - 0.25f));
                if(pitch.get() <= 0.15f) {
                    player.playSound(player, Sound.ITEM_TRIDENT_THUNDER, 0.7F, 0.4F);
                }
            }
        }).run(3, 7);
        for(final Player player : team) {
            TitleUtil.sendTitle(player, "<gold><b>Encounter Started", "<gray>Open the gate", 1, 2, 1);
        }
    }
}
