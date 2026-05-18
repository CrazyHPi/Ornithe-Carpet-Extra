package carpetextra.mixins.rules.betterTimeCommand;

import carpetextra.CarpetExtraSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.TimeCommand;
import net.minecraft.server.command.source.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TimeCommand.class)
public abstract class TimeCommandMixin {
    @Shadow
    protected abstract void setTimeOfDay(MinecraftServer server, int time);

    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    private void betterSet(MinecraftServer server, CommandSource source, String[] args, CallbackInfo ci) {
        if (!CarpetExtraSettings.betterTimeCommand) {
            return;
        }
        if (args.length <= 1) {
            return;
        }
        if ("set".equals(args[0])) {
            if ("noon".equals(args[1])) {
                this.setTimeOfDay(server, 6000);
                AbstractCommand.sendSuccess(source, ((TimeCommand) (Object) this), "commands.time.set", 6000);
                ci.cancel();
            }
            if ("midnight".equals(args[1])) {
                this.setTimeOfDay(server, 18000);
                AbstractCommand.sendSuccess(source, ((TimeCommand) (Object) this), "commands.time.set", 18000);
                ci.cancel();
            }
        }
    }

    @WrapOperation(
            method = "getSuggestions",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/server/command/TimeCommand.suggestMatching([Ljava/lang/String;[Ljava/lang/String;)Ljava/util/List;",
                    ordinal = 1
            )
    )
    private List<String> addCommandSuggestion(String[] args, String[] suggestions, Operation<List<String>> original) {
        if (CarpetExtraSettings.betterTimeCommand) {
            return original.call(args, new String[]{"day", "noon", "night", "midnight"});
        }
        return original.call(args, suggestions);
    }
}
