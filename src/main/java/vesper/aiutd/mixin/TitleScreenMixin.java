package vesper.aiutd.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import vesper.aiutd.AIUTDAmIUpToDate;
import vesper.aiutd.AIUTDConfig;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void addUpdateNotice(int y, int spacingY, CallbackInfo ci) {
        super.init();

        // Message should only display if there is an update
        if (AIUTDConfig.instance().menuAlert && AIUTDAmIUpToDate.updateAvailable()) {
            this.addDrawableChild(ButtonWidget.builder(Text.translatable("aiutd.text.updateButton"), button -> {
                try {
                    String changelogLink = AIUTDConfig.instance().changelogLink + AIUTDConfig.instance().localVersion;
                    if (AIUTDConfig.instance().platform == AIUTDConfig.Platforms.MODRINTH) {
                        changelogLink = "https://modrinth.com/modpack/" + AIUTDConfig.instance().modpackId + "/version/" + AIUTDConfig.instance().localVersion;
                    }

                    // URL to fetch from
                    URI uri = URI.create(changelogLink);

                    // Check if the Desktop class is supported and if the browser can be opened
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(uri);  // Open the browser with the URL
                    } else {
                        // Alternative link opening logic
                        String os = System.getProperty("os.name").toLowerCase();
                        try {
                            if (os.contains("win")) {
                                Runtime.getRuntime().exec(new String[]{"rundll32", "uri.dll,FileProtocolHandler", AIUTDConfig.instance().changelogLink});
                            } else if (os.contains("mac")) {
                                Runtime.getRuntime().exec(new String[]{"open", changelogLink});
                            } else if (os.contains("nix") || os.contains("nux")) {
                                Runtime.getRuntime().exec(new String[]{"xdg-open", changelogLink});
                            } else {
                                System.out.println("Unsupported OS for opening a browser.");
                            }
                        } catch (IOException e) {
                            AIUTDAmIUpToDate.LOGGER.info(String.valueOf(e));
                        }
                    }
                } catch (Exception e) {
                    AIUTDAmIUpToDate.LOGGER.info(String.valueOf(e));
                }
            }).dimensions(this.width / 2 - 100 + 205, y, 90, 20).build());
        }
    }
}
