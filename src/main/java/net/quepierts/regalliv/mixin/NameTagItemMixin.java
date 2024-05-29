package net.quepierts.regalliv.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.quepierts.regalliv.Regalliv;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NameTagItem.class)
public class NameTagItemMixin {

    @Inject(
            method = "interactLivingEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;setCustomName(Lnet/minecraft/network/chat/Component;)V",
                    shift = At.Shift.BEFORE
            )
    )
    public void flipVillager(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (entity instanceof AbstractVillager villager) {
            if (!stack.getHoverName().getString().equals("Dinnerbone"))
                return;

            if (!entity.hasCustomName() || !entity.getCustomName().getString().equals("Dinnerbone")) {
                Regalliv.flip(villager);
            }
        }
    }
}
