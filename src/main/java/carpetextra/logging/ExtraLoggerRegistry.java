package carpetextra.logging;

import carpet.api.log.HudLogger;
import carpet.api.log.Logger;
import carpet.log.framework.LoggerRegistry;
import carpetextra.logging.logHelpers.XpCounter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

public class ExtraLoggerRegistry {
    public static boolean __xpCounter;

    public static void initLoggers() {
        LoggerRegistry.registerLogger("xpCounter", getHudLogger("xpCounter", "armorStand", new String[]{"armorStand", "playerName"}));
    }

    public static void updateExtraHud(MinecraftServer server) {
        if (__xpCounter) {
            LoggerRegistry.getLogger("xpCounter").log((option) -> sendXpCounter(option, server));
        }
    }

    private static Text[] sendXpCounter(String option, MinecraftServer server) {
        XpCounter counter = XpCounter.getCounter(option);
        if (counter != null) {
            return counter.format(server, false, true).toArray(new Text[0]);
        }
        return null;
    }

    public static HudLogger getHudLogger(String name, String defaultOption, String[] options) {
        try {
            return new HudLogger(ExtraLoggerRegistry.class.getField("__" + name), name, defaultOption, options, false);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to create logger " + name);
        }
    }

    public static Logger getChatLogger(String name, String defaultOption, String[] options) {
        try {
            return new Logger(ExtraLoggerRegistry.class.getField("__" + name), name, defaultOption, options, false);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to create logger " + name);
        }

    }
}
