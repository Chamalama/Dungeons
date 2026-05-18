package mike.dungeons.dungeon.entity.component;

import com.destroystokyo.paper.SkinParts;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import lombok.Getter;
import mike.dungeons.dungeon.entity.DungeonEntity;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Mannequin;
import org.bukkit.profile.PlayerTextures;

@Getter
public class SkinComponent implements ApplicableComponent {

    private String skin;
    private String cape;

    private static final String NAMESPACE = "legends";

    public SkinComponent(String skin, String cape) {
        this.skin = skin;
        this.cape = cape;
    }

    @Override
    public void apply(DungeonEntity entity) {
        Mannequin mannequin = (Mannequin) entity.getEntity();
        final Key skinKey = Key.key(NAMESPACE, "npc/" + skin);
        final Key capeKey = Key.key(NAMESPACE, "npc/" + cape);
        final ResolvableProfile.SkinPatch skinPatch = ResolvableProfile.SkinPatch.skinPatch()
                .model(PlayerTextures.SkinModel.CLASSIC)
                .body(skinKey)
                .cape(capeKey).build();
        final ResolvableProfile resolvableProfile = ResolvableProfile.resolvableProfile().skinPatch(skinPatch).build();
        mannequin.setSkinParts(SkinParts.allParts());
        mannequin.setProfile(resolvableProfile);
    }

}
