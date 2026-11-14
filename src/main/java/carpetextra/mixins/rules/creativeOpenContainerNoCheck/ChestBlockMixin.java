package carpetextra.mixins.rules.creativeOpenContainerNoCheck;

import carpetextra.CarpetExtraSettings;
import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.menu.LockableMenuProvider;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin {
    @Shadow
    @Final
    public ChestBlock.Type type;

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/ChestBlock;getMenuProvider(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/inventory/menu/LockableMenuProvider;"
            ),
            cancellable = true
    )
    private void creativeOpenChest(World world, BlockPos pos, BlockState state, PlayerEntity player, InteractionHand hand, Direction face, float dx, float dy, float dz, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetExtraSettings.creativeOpenContainerNoCheck && player.isCreative()) {
            LockableMenuProvider lockableMenuProvider = forceGetCombinedMenuProvider(world, pos);
            if (lockableMenuProvider != null) {
                player.openChestMenu(lockableMenuProvider);
                if (this.type == ChestBlock.Type.BASIC) {
                    player.incrementStat(Stats.CHESTS_OPENED);
                } else if (this.type == ChestBlock.Type.TRAP) {
                    player.incrementStat(Stats.TRAPPED_CHESTS_TRIGGERED);
                }
            }
            cir.setReturnValue(true);
        }
    }

    @Unique
    private LockableMenuProvider forceGetCombinedMenuProvider(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof ChestBlockEntity)) {
            return null;
        } else {
            LockableMenuProvider lockableMenuProvider = (ChestBlockEntity) blockEntity;
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos blockPos = pos.offset(direction);
                Block block = world.getBlockState(blockPos).getBlock();
                if (block == ((ChestBlock) (Object) this)) {
                    BlockEntity blockEntity2 = world.getBlockEntity(blockPos);
                    if (blockEntity2 instanceof ChestBlockEntity) {
                        if (direction != Direction.WEST && direction != Direction.NORTH) {
                            lockableMenuProvider = new DoubleInventory("container.chestDouble", lockableMenuProvider, (ChestBlockEntity) blockEntity2);
                        } else {
                            lockableMenuProvider = new DoubleInventory("container.chestDouble", (ChestBlockEntity) blockEntity2, lockableMenuProvider);
                        }
                    }
                }
            }

            return lockableMenuProvider;
        }
    }

//    private static final ThreadLocal<Boolean> ignoreChestBlockedCheck = ThreadLocal.withInitial(() -> false);
//
//    @Inject(
//            method = "use",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/block/ChestBlock;getMenuProvider(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/inventory/menu/LockableMenuProvider;"
//            )
//    )
//    private void getPlayer(World world, BlockPos pos, BlockState state, PlayerEntity player, InteractionHand hand, Direction face, float dx, float dy, float dz, CallbackInfoReturnable<Boolean> cir) {
//        ignoreChestBlockedCheck.set(CarpetExtraSettings.creativeOpenContainerNoCheck && player.isCreative());
//    }
//
//    @Inject(
//            method = "use",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/entity/living/player/PlayerEntity;openInventoryMenu(Lnet/minecraft/inventory/Inventory;)V",
//                    shift = At.Shift.AFTER
//            )
//    )
//    private void afterUse(CallbackInfoReturnable<Boolean> cir) {
//        ignoreChestBlockedCheck.set(false);
//    }
//
//    @Inject(method = "isLocked", at = @At("HEAD"), cancellable = true)
//    private void forceOpen(World world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
//        if (ignoreChestBlockedCheck.get()) {
//            cir.setReturnValue(false);
//        }
//    }
}

