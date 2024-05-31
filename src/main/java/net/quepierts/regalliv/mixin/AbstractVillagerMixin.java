package net.quepierts.regalliv.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.quepierts.regalliv.util.FlipUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerMixin extends LivingEntity {

    @Shadow @Nullable protected MerchantOffers offers;

    protected AbstractVillagerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @ModifyVariable(
            method = "addOffersFromItemListings",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/trading/MerchantOffers;add(Ljava/lang/Object;)Z",
                    shift = At.Shift.BEFORE,
                    remap = false
            ),
            ordinal = 0
    )
    private MerchantOffer replaceOffer(MerchantOffer offer) {
        if (FlipUtil.isDinnerbone(this)) {
            return FlipUtil.flip(offer);
        }

        return offer;
    }

    /**
     * @author Louis_Quepierts
     * @reason Allow to override offers
     */
    @Overwrite
    public void overrideOffers(@Nullable MerchantOffers offers) {
        this.offers = offers;
    }
}
