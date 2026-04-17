package mike.dungeons.dungeon.dungeons.outpost.component;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;
import lombok.Setter;
import mike.blueprint.util.ExpiringTask;
import mike.blueprint.util.FastLocation;
import mike.blueprint.util.ParticleUtil;
import mike.dungeons.Dungeons;
import mike.dungeons.dungeon.DungeonBuff;
import mike.dungeons.dungeon.DungeonUtil;
import mike.dungeons.dungeon.component.EventComponent;
import mike.dungeons.dungeon.component.GenericEventComponent;
import mike.dungeons.dungeon.system.BuffSystem;
import mike.dungeons.dungeon.team.DungeonTeam;
import mike.dungeons.dungeon.team.EncounterData;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class GateCaptureComponent implements EventComponent {

    private static final String BUFF_ID = "CHARGED";
    private final List<CapturePoint> capturePoints = new ArrayList<>();

    public GateCaptureComponent() {
        this.capturePoints.add(new CapturePoint(new int[]{-576, 210, 123}, Particle.FLAME));
        this.capturePoints.add(new CapturePoint(new int[]{-616, 210, 5}, Particle.SOUL_FIRE_FLAME));
        this.capturePoints.add(new CapturePoint(new int[]{-665, 214, 81}, Particle.SOUL));
    }

    @Override
    public void tick(DungeonTeam team, GenericEventComponent genericEventComponent) {
        final long currTime = System.currentTimeMillis();
        final CaptureState captureState = team.getEncounterData().getState(CaptureState.class);
        if(captureState == null) {
            return;
        }
        if(currTime - captureState.getLastTickTime() < tickTime()) return;
        final World world = Bukkit.getWorld(team.getWorldName());
        if(world == null) return;
        final List<Player> teamPlayers = team.getPlayers(true);
        for(CapturePoint capturePoint : captureState.getCapturePoints()) {
            for(Player player : teamPlayers) {
                if(capturePoint.isInRange(player) && capturePoint.canIncrement()) {
                    capturePoint.increment();
                    if(capturePoint.getProgress() % 7 == 0) {
                        player.playSound(player, Sound.ITEM_TRIDENT_THUNDER, 1.0F, 1.2f + (capturePoint.getProgress() / 50));
                    }
                    break;
                }
            }
            if(capturePoint.getProgress() >= 25 && !capturePoint.isCaptured()) {
                captureState.incrementCaptured();
                if(captureState.getPointsCaptured() == 3) {
                    final Player toBuff = teamPlayers.get(new Random().nextInt(teamPlayers.size()));
                    toBuff.playSound(toBuff, Sound.ITEM_TRIDENT_RETURN, 1.0F, 0.7F);
                    BuffSystem.addBuff(toBuff, new DungeonBuff(30, BUFF_ID, "<aqua>Conduit Charge"));
                }
                capturePoint.setCaptured(true);
                for(Player player : teamPlayers) {
                    DungeonUtil.sendDungeonMessage(player, "<aqua>Conduit charged!");
                    player.playSound(player, Sound.ITEM_TRIDENT_RETURN, 1.0F, 1.5F);
                }
            }
            if(capturePoint.getProgress() > 0) {
                final Location location = capturePoint.asLocation(world);
                ParticleUtil.circle(location.clone().add(0, 3, 0).toCenterLocation(), capturePoint.getCaptureParticle(), 4, 0.75, (int)capturePoint.getProgress()/2);
            }
        }
        if(captureState.getPointsCaptured() >= 3 && !captureState.isBuffSpawned()) {
            AtomicDouble originalPitch = new AtomicDouble(2.0);
            for(Player player : teamPlayers) {
                DungeonUtil.sendDungeonMessage(player, "All conduits charged!");
                new ExpiringTask(Dungeons.getInst()).setAsync(true).runCount(5).setAction(() -> {
                    float val = (float) originalPitch.get();
                    player.playSound(player, Sound.ENTITY_SKELETON_CONVERTED_TO_STRAY, 1.0F, val);
                    originalPitch.getAndAdd(-0.25);
                }).run(5, 4);

            }
            captureState.setBuffSpawned(true);
        }
        captureState.setLastTickTime(currTime);
    }

    @Override
    public void startEvent(DungeonTeam dungeonTeam) {
        final World world = Bukkit.getWorld(dungeonTeam.getWorldName());
        final EncounterData encounterData = dungeonTeam.getEncounterData();
        final CaptureState state = new CaptureState();
        for(CapturePoint capturePoint : capturePoints) {
            capturePoint = capturePoint.clone();
            encounterData.getRoamPoints().add(new FastLocation(capturePoint.asLocation(world)));
            capturePoint.setTeamWorld(dungeonTeam.getWorldName());
            state.getCapturePoints().add(capturePoint);
        }
        dungeonTeam.getEncounterData().setState(CaptureState.class, state);
        if(world == null) return;
        for (final CapturePoint capturePoint : capturePoints) {
            int[] point = capturePoint.getCoordinates();
            final int x = point[0];
            final int y = point[1];
            final int z = point[2];
            final Location location = new Location(world, x, y, z).toCenterLocation();
            world.strikeLightningEffect(location);
        }
    }

    @Override
    public void stopEvent(DungeonTeam dungeonTeam) {
        final EncounterData data = dungeonTeam.getEncounterData();
        for(Player player : dungeonTeam.getPlayers(true)) {
            BuffSystem.removeBuff(player);
        }
        data.getRoamPoints().clear();
        data.clearState();
    }

    @Override
    public long tickTime() {
        return 1000L;
    }

    @Override
    public boolean startOnEnter() {
        return true;
    }

    @Getter
    @Setter
    static class CapturePoint implements Cloneable {

        private final int[] coordinates;
        private Particle captureParticle;
        private float progress;
        private long lastIncrementTime;
        private boolean captured;
        private String teamWorld;

        public CapturePoint(int[] coordinates, Particle captureParticle) {
            this.coordinates = coordinates;
            this.progress = 0;
            this.captured = false;
            this.captureParticle = captureParticle;
            this.teamWorld = "";
        }

        public Location asLocation(World world) {
            return new Location(world, coordinates[0], coordinates[1], coordinates[2]);
        }

        public boolean isInRange(Entity entity) {
            final Location entityLoc = entity.getLocation();
            final World entityWorld = entityLoc.getWorld();
            if(!entityWorld.getName().equalsIgnoreCase(teamWorld)) return false;
            final int entityX = entityLoc.getBlockX();
            final int entityY = entityLoc.getBlockY();
            final int entityZ = entityLoc.getBlockZ();
            final int pointX = coordinates[0];
            final int pointY = coordinates[1];
            final int pointZ = coordinates[2];
            return Math.abs(pointX - entityX) < 8
                    && Math.abs(pointY - entityY) < 8
                    && Math.abs(pointZ - entityZ) < 8;
        }

        private boolean canIncrement() {
            return System.currentTimeMillis() - lastIncrementTime >= 750L && !captured;
        }

        public void increment() {
            this.progress++;
            this.lastIncrementTime = System.currentTimeMillis();
        }

        @Override
        public CapturePoint clone() {
            try {
                return (CapturePoint) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    @Getter
    @Setter
    static class CaptureState {

        private final List<CapturePoint> capturePoints = new ArrayList<>();
        private int pointsCaptured = 0;
        private boolean buffSpawned = false;
        private long lastTickTime = 0L;

        public void incrementCaptured() {
            ++pointsCaptured;
        }

    }

}
