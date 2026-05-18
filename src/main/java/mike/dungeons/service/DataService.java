package mike.dungeons.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mike.blueprint.loader.Component;
import mike.dungeons.Dungeons;
import mike.dungeons.dungeon.team.DungeonTeam;
import mike.dungeons.dungeon.team.TeamData;
import mike.dungeons.storage.KeyStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;

@Component
public class DataService {

    private final DungeonTeamService dungeonTeamService;
    private final KeyStorage keyStorage;

    private static final Gson JSON_WRITER = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static URI DATA_ENDPOINT;

    public DataService(DungeonTeamService dungeonTeamService, KeyStorage keyStorage) {
        this.dungeonTeamService = dungeonTeamService;
        this.keyStorage = keyStorage;
        DATA_ENDPOINT = URI.create(keyStorage.getEndpoint());
    }

    public void sendDungeonData(DungeonTeam team) {
        asyncCall(() -> {

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(DATA_ENDPOINT)
                    .header("Content-Type", "application/json")
                    .header("DUNGEON-KEY", keyStorage.getDataKey())
                    .POST(HttpRequest.BodyPublishers.ofString(JSON_WRITER.toJson(team)))
                    .build();

            try{
                HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
            }catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public void sendDungeonStartData(DungeonTeam team) {
        asyncCall(() -> {

            final TeamData teamData = new TeamData(
                    team.getName(),
                    team.getPlayers(true).stream().map(Player::getName).toList(),
                    team.getDungeon().getDungeonName(),
                    team.getEncounterData().getEncountersCleared(),
                    0,
                    0
            );

            HttpRequest startRequest = HttpRequest.newBuilder()
                    .uri(DATA_ENDPOINT)
                    .header("Content-Type", "application/json")
                    .header("DUNGEON-KEY", keyStorage.getDataKey())
                    .POST(HttpRequest.BodyPublishers.ofString(JSON_WRITER.toJson(teamData)))
                    .build();

            try{
                httpClient.send(startRequest, HttpResponse.BodyHandlers.ofString());
                Dungeons.getInst().getLogger().info("Sending dungeon team data to endpoint...");
            }catch (Exception e) {
                Dungeons.getInst().getLogger().info("Endpoint is not reachable for sending dungeon data...");
            }

        });
    }

    private static void asyncCall(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Dungeons.getInst(), runnable);
    }

}
