package net.quepierts.regalliv.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.quepierts.regalliv.util.FlipUtil;
import net.quepierts.regalliv.util.PiglinUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin {
    @Inject(
            method = "stopHoldingOffHandItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void stopHoldingOffHandItemFlipped(Piglin piglin, boolean p_34869_, CallbackInfo cir) {
        if (piglin.isAdult() && FlipUtil.isDinnerbone(piglin) && PiglinUtil.isAdmirableItemIgnoredCost(piglin.getItemInHand(InteractionHand.OFF_HAND))) {
            piglin.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            throwItems(piglin, Collections.singletonList(new ItemStack(Items.GOLD_INGOT)));
            cir.cancel();
        }
    }

    @Inject(
            method = "wantsToPickup",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void wantsToPickupFlipped(Piglin piglin, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (FlipUtil.isDinnerbone(piglin)) {
            if (PiglinUtil.isAdmirableItemIgnoredCost(itemStack)) {
                cir.setReturnValue(isNotHoldingLovedItemInOffHand(piglin));
            } else if (isFood(itemStack)) {
                cir.setReturnValue(!hasEatenRecently(piglin) && piglin.getInventory().canAddItem(itemStack));
            }
            cir.cancel();
        }
    }

    @Inject(
            method = "pickUpItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;stopWalking(Lnet/minecraft/world/entity/monster/piglin/Piglin;)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private static void pickUpItemFlipped(Piglin piglin, ItemEntity entity, CallbackInfo ci) {
        if (FlipUtil.isDinnerbone(piglin)) {
            ItemStack itemstack = entity.getItem();
            if (!isAdmiringItem(piglin) && PiglinUtil.isAdmirableItem(itemstack)) {
                int cost = PiglinUtil.getRandomCost(itemstack, piglin.getRandom());

                piglin.take(entity, cost);
                itemstack = entity.getItem();
                ItemStack itemstack1 = itemstack.split(cost);

                if (itemstack.isEmpty()) {
                    entity.discard();
                } else {
                    entity.setItem(itemstack);
                }

                itemstack = itemstack1;

                piglin.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
                holdInOffhand(piglin, itemstack);
                admireGoldItem(piglin);
            }
            ci.cancel();
        }
    }

    /**
     * @author Louis_Quepierts
     * @reason Flipped Interact Method
     */
    @Overwrite
    public static InteractionResult mobInteract(Piglin piglin, Player player, InteractionHand p_34849_) {
        ItemStack itemstack = player.getItemInHand(p_34849_);

        boolean dinnerbone = FlipUtil.isDinnerbone(piglin);
        boolean canAdmire = !isAdmiringDisabled(piglin) && !isAdmiringItem(piglin) && piglin.isAdult()
                && dinnerbone
                ? PiglinUtil.isAdmirableItem(itemstack)
                : itemstack.isPiglinCurrency();

        if (canAdmire) {
            ItemStack itemstack1 = itemstack.split(dinnerbone ? PiglinUtil.getRandomCost(itemstack, piglin.getRandom()) : 1);
            holdInOffhand(piglin, itemstack1);
            admireGoldItem(piglin);
            stopWalking(piglin);
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.PASS;
        }
    }

    /**
     * @author Louis_Quepierts
     * @reason Flipped Admire Method
     */
    @Overwrite
    protected static boolean canAdmire(Piglin piglin, ItemStack itemStack) {
        boolean flag = !isAdmiringDisabled(piglin) && !isAdmiringItem(piglin) && piglin.isAdult();
        boolean dinnerbone = FlipUtil.isDinnerbone(piglin);
        return flag && dinnerbone ? PiglinUtil.isAdmirableItem(itemStack) : itemStack.isPiglinCurrency();
    }

    /**
     * @author Louis_Quepierts
     * @reason Flipped Items
     */
    @Overwrite
    private static boolean isNotHoldingLovedItemInOffHand(Piglin piglin) {
        return piglin.getOffhandItem().isEmpty()
                || (FlipUtil.isDinnerbone(piglin)
                        ? !PiglinUtil.isAdmirableItem(piglin.getOffhandItem())
                        : !isLovedItem(piglin.getOffhandItem())
                );
    }

    @Shadow
    private static boolean isAdmiringItem(Piglin p34910) {
        return false;
    }

    @Shadow
    private static boolean isAdmiringDisabled(Piglin p34910) {
        return false;
    }

    @Shadow
    private static void throwItems(Piglin p_34861_, List<ItemStack> p_34862_) {}

    @Shadow
    private static void holdInOffhand(Piglin p_34933_, ItemStack p_34934_) {}

    @Shadow
    private static void admireGoldItem(LivingEntity p_34939_) {}

    @Shadow
    private static void stopWalking(Piglin p_35007_) {}

    @Shadow
    protected static boolean isLovedItem(ItemStack p_149966_) {
        return false;
    }

    @Shadow
    private static boolean hasEatenRecently(Piglin p_35019_) {
        return false;
    }

    @Shadow
    private static boolean isFood(ItemStack p_149970_) {
        return false;
    }
}
