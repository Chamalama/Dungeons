package mike.dungeons.dungeon.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TeamData {

    private String teamName;
    private List<String> teamPlayers;
    private String dungeonName;
    private int currentEncounter, currentBossHealth, currentBossPhase;

}
