package carpetextra.mixins.rules.creativeOpenContainerNoCheck;

import carpetextra.CarpetExtraSettings;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void creativeOpenEnderChest(World world, BlockPos pos, BlockState state, PlayerEntity player, InteractionHand hand, Direction face, float dx, float dy, float dz, CallbackInfoReturnable<Boolean> cir) {
        if (!CarpetExtraSettings.creativeOpenContainerNoCheck || world.isClient) {
            return;
        }
        if (!player.isCreative()) {
            return;
        }

        EnderChestInventory enderChestInventory = player.getEnderChestInventory();
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (enderChestInventory == null || !(blockEntity instanceof EnderChestBlockEntity)) {
            cir.setReturnValue(true);
        } else {
            enderChestInventory.setCurrentBlockEntity((EnderChestBlockEntity)blockEntity);
            player.openChestMenu(enderChestInventory);
            player.incrementStat(Stats.ENDER_CHESTS_OPENED);
            cir.setReturnValue(true);
        }
    }
}
