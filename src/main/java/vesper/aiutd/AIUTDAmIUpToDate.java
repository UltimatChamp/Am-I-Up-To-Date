package vesper.aiutd;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class AIUTDAmIUpToDate implements ClientModInitializer {
    public static final String MOD_ID = "aiutd";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Grab version from Modrinth API
    public static String getLatestVersion() {
        StringBuilder result = new StringBuilder();
        try {
            URL url;
            if (AIUTDConfig.instance().platform == AIUTDConfig.Platforms.MODRINTH) {
                url = URI.create("https://api.modrinth.com/v2/project/" + AIUTDConfig.instance().modpackId + "/version").toURL();
            } else {
                url = URI.create(AIUTDConfig.instance().versionAPI).toURL();
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            JsonArray jsonArray = JsonParser.parseString(result.toString()).getAsJsonArray();
            if (!jsonArray.isEmpty()) {
                JsonElement getVersionElement = jsonArray.get(0);
                return getVersionElement.getAsJsonObject().get("version_number").getAsString();
            }
        } catch (Exception e) {
            LOGGER.error("e: ", e);
        }
        return null;
    }

    public static boolean updateAvailable() {
        // Version grabbed
        String modpackVersion = getLatestVersion();
        //Local version
        String localVersion = AIUTDConfig.instance().localVersion;

        // Compare local version to the version grabbed
        return !localVersion.equals(modpackVersion);
    }

    @Override
    public void onInitializeClient() {
        AIUTDConfig.load();
        LOGGER.info("Am I Up To Date says Trans Rights are Human Rights!");

        if (AIUTDConfig.instance().chatAlert && updateAvailable()) {
            ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> client.execute(() -> {
                assert MinecraftClient.getInstance().player != null;
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("aiutd.text.chat"), false);
                MinecraftClient.getInstance().player.sendMessage(Text.of(AIUTDConfig.instance().localVersion + " --> " + getLatestVersion()), false);
            })));
        }
    }
}
