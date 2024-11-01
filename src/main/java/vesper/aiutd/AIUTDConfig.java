package vesper.aiutd;

import dev.isxander.yacl3.api.NameableEnum;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class AIUTDConfig {
    private static final ConfigClassHandler<AIUTDConfig> GSON = ConfigClassHandler.createBuilder(AIUTDConfig.class)
            .id(Identifier.of("aiutd", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("aiutd.json5"))
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public boolean menuAlert = true;

    @SerialEntry
    public boolean chatAlert = true;

    @SerialEntry
    public Platforms platform = Platforms.MODRINTH;

    public enum Platforms implements NameableEnum {
        CUSTOM("generator.custom"),
        MODRINTH("aiutd.modrinth");

        private final String displayName;

        Platforms(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public Text getDisplayName() {
            return Text.translatable(displayName);
        }
    }

    @SerialEntry
    public String modpackId = "your-modpack";

    @SerialEntry
    public String localVersion = "0.0.0";

    @SerialEntry
    public String versionAPI = "https://api.modrinth.com/v2/project/<modpack-id>/version";

    @SerialEntry
    public String changelogLink = "https://modrinth.com/modpack/<modpack-id>/version/";

    public static ConfigClassHandler<AIUTDConfig> handler() {
        return GSON;
    }

    public static void load() {
        GSON.load();
    }

    public static AIUTDConfig instance() {
        return GSON.instance();
    }

    public static Screen createScreen(@Nullable Screen parent) {
        return AIUTDGui.createConfigScreen(parent);
    }
}
