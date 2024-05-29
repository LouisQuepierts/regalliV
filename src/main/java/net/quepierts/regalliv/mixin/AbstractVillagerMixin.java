package net.quepierts.regalliv.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.quepierts.regalliv.Regalliv;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerMixin extends Entity {

    @Shadow @Nullable protected MerchantOffers offers;

    public AbstractVillagerMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
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
        if (hasCustomName() && getCustomName().getString().equals("Dinnerbone")) {
            LogUtils.getLogger().info("Dinnerbone");
            return Regalliv.flip(offer);
        }

        return offer;
    }

    /**
     * @author Louis_Quepierts
     * @reason
     */
    @Overwrite
    public void overrideOffers(@Nullable MerchantOffers offers) {
        this.offers = offers;
    }
}
