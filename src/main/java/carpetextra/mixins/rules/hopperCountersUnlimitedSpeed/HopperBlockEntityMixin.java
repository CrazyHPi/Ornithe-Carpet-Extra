package carpetextra.mixins.rules.hopperCountersUnlimitedSpeed;

import carpet.CarpetSettings;
import carpet.utils.Messenger;
import carpet.utils.WoolTool;
import carpetextra.CarpetExtraSettings;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootInventoryBlockEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootInventoryBlockEntity {
    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    protected abstract boolean pushItem();

    @Shadow
    protected abstract boolean isFull();

    @Shadow
    protected abstract void setCooldown(int cooldown);

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Unique
    private static final int OPERATION_LIMIT = Short.MAX_VALUE;

    @Inject(
            method = "transferItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/HopperBlockEntity;setCooldown(I)V"
            )
    )
    private void noCooldown(CallbackInfoReturnable<Boolean> cir) {
        if (CarpetSettings.hopperCounters && CarpetExtraSettings.hopperCountersUnlimitedSpeed) {
            DyeColor color = WoolTool.getWoolColorAtPosition(
                    getWorld(),
                    new BlockPos(getX(), getY(), getZ()).offset(HopperBlock.getFacing(this.getBlockMetadata()))
            );
            if (color == null) {
                return;
            }
            for (int i = OPERATION_LIMIT - 1; i >= 0; i--) {
                boolean flag1 = false;

                if (!this.isEmpty()) {
                    flag1 = this.pushItem();
                }

                if (!this.isFull()) {
                    flag1 = HopperBlockEntity.pullItem((HopperBlockEntity) (Object) this) || flag1;
                }

                if (!flag1) {
                    break;
                }
                if (i == 0) {
                    Messenger.print_server_message(this.getWorld().getServer(), String.format("Hopper in %s exceeded hopperCountersUnlimitedSpeed operation limit %d", new BlockPos(getX(), getY(), getZ()), OPERATION_LIMIT));
                }
            }
            this.setCooldown(0);
        }
    }
}
