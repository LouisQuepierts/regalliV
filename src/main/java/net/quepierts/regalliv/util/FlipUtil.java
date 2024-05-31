package net.quepierts.regalliv.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public class FlipUtil {
    public static MerchantOffer flip(MerchantOffer offer) {
        return new MerchantOffer(offer.getResult(), offer.getCostB(), offer.getBaseCostA(), offer.getUses(), offer.getMaxUses(), offer.getXp(), offer.getPriceMultiplier(), offer.getDemand());
    }

    public static void flip(AbstractVillager villager) {
        MerchantOffers offers = villager.getOffers();
        MerchantOffers flipped = new MerchantOffers();

        for (MerchantOffer offer : offers) {
            flipped.add(flip(offer));
        }

        villager.overrideOffers(flipped);
    }

    public static boolean isDinnerbone(LivingEntity entity) {
        return entity.getName().getString().equals("Dinnerbone");
    }
}
