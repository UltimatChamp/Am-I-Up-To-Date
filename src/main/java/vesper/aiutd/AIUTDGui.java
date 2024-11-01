package vesper.aiutd;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.gui.controllers.cycling.EnumController;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class AIUTDGui {
    public static Screen createConfigScreen(Screen parent) {
        return YetAnotherConfigLib.create(AIUTDConfig.handler(), (defaults, config, builder) -> builder
                .title(Text.translatable("aiutd.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("stat.generalButton"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("aiutd.menuAlert"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("aiutd.menuAlert.desc"))
                                        .build())
                                .binding(
                                        defaults.menuAlert,
                                        () -> config.menuAlert,
                                        (value) -> config.menuAlert = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("aiutd.chatAlert"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("aiutd.chatAlert.desc"))
                                        .build())
                                .binding(
                                        defaults.chatAlert,
                                        () -> config.chatAlert,
                                        (value) -> config.chatAlert = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<AIUTDConfig.Platforms>createBuilder()
                                .name(Text.translatable("aiutd.platform"))
                                .binding(
                                        defaults.platform,
                                        () -> config.platform,
                                        (value) -> config.platform = value
                                )
                                .customController(opt -> new EnumController<>(opt, AIUTDConfig.Platforms.class))
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.translatable("aiutd.modpackId"))
                                .binding(
                                        defaults.modpackId,
                                        () -> config.modpackId,
                                        (value) -> config.modpackId = value
                                )
                                .available(AIUTDConfig.instance().platform != AIUTDConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.translatable("aiutd.localVersion"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("aiutd.localVersion.desc"))
                                        .build())
                                .binding(
                                        defaults.localVersion,
                                        () -> config.localVersion,
                                        (value) -> config.localVersion = value
                                )
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.translatable("aiutd.versionAPI"))
                                .binding(
                                        defaults.versionAPI,
                                        () -> config.versionAPI,
                                        (value) -> config.versionAPI = value
                                )
                                .available(AIUTDConfig.instance().platform == AIUTDConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.translatable("aiutd.changelogLink"))
                                .binding(
                                        defaults.changelogLink,
                                        () -> config.changelogLink,
                                        (value) -> config.changelogLink = value
                                )
                                .available(AIUTDConfig.instance().platform == AIUTDConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .build())
                .save(AIUTDConfig.handler()::save)
        ).generateScreen(parent);
    }
}
