package carpetextra;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.log.framework.HudController;
import carpetextra.command.XpCounterCommand;
import carpetextra.logging.ExtraLoggerRegistry;
import carpetextra.utils.CarpetExtensionTranslations;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.handler.CommandRegistry;

import java.util.Map;

public class CarpetExtraServer implements CarpetExtension {
    public static MinecraftServer minecraftServer = null;

    @Override
    public String version() {
        return "carpet-extra";
    }

    public static void init(){
        // load mixin extra
        MixinExtrasBootstrap.init();
        // register extension
        loadExtension();
    }

    public static void loadExtension() {
        // add to carpet's extension list
        CarpetServer.manageExtension(new CarpetExtraServer());
    }

    @Override
    public void onGameStarted() {
        // let carpet handle the settings
        CarpetServer.settingsManager.parseSettingsClass(CarpetExtraSettings.class);
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        minecraftServer = server;
    }

    @Override
    public void registerCommands(CommandRegistry registry) {
        // register commands here
        registry.register(new XpCounterCommand());
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return CarpetExtensionTranslations.getTranslationFromResourcePath(lang);
    }

    @Override
    public void registerLoggers() {
        ExtraLoggerRegistry.initLoggers();
        HudController.register(ExtraLoggerRegistry::updateExtraHud);
    }
}
