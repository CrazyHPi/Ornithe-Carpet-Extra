package carpetextra;

import carpet.api.settings.Rule;
import carpet.api.settings.Validators;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static carpet.api.settings.RuleCategory.*;

public class CarpetExtraSettings {
    public static final Logger LOGGER = LogManager.getLogger("carpet-extra");
    // extension category
    public static final String EXTRA = "extra";

    // ==== RULES ==== //
    @Rule(desc = "allow water to be placed in nether", categories = {CREATIVE, EXTRA})
    public static boolean allowNetherWater = false;

    @Rule(desc = "Disable creative mode item drop cooldown", categories = {CREATIVE, EXTRA})
    public static boolean antiSpamDisabled = false;

    @Rule(desc = "Creative player can open chest and shulker box when they are blocked", categories = EXTRA)
    public static boolean creativeOpenContainerNoCheck = false;

    @Rule(desc = "No more bat spawning", categories = {CREATIVE, EXTRA})
    public static boolean disableBatSpawning = false;

    @Rule(desc = "Hopper counter's hopper has no cooldown", categories = {CREATIVE, EXTRA})
    public static boolean hopperCountersUnlimitedSpeed = false;

    @Rule(desc = "Hopper with wool block on top outputs item infinitely without having its item decreased", categories = {CREATIVE, EXTRA})
    public static boolean hopperNoItemCost = false;

    @Rule(
            desc = "liquid no longer destory blocks, e.g redstone",
            extra = {
                    "liquid might weird when it hits block that should be destroyed",
                    "it will just stop there with no more flow action"
            },
            categories = {CREATIVE, EXTRA}
    )
    public static boolean liquidNoBlockBreaking = false;

    @Rule(
            desc = "Count xp when sucked by player or armor stand",
            extra = {
                    "xp sucked by armor stand will be removed after counted",
                    "player: count xp sucked by player, xp reduced by mending is included",
                    "armorstand: count xp sucked by armor stand, and xp will get destroyed",
                    "Use /xpcounter <player name|armorstand> to query counter",
                    "Use /xpcounter <player|armorstand> reset to reset counter"
            },
            categories = {CREATIVE, EXTRA},
            options = {"off", "armorstand", "player"},
            validators = Validators.StrictValidator.class
    )
    public static String xpCounter = "off";

    // ==== COMMANDS ==== //

    @Rule(desc = "better /time command", categories = {COMMAND})
    public static boolean betterTimeCommand = true;
}
