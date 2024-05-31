package net.quepierts.regalliv;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Regalliv.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Common {
    @SubscribeEvent
    public static void onCreativeTabBuildContents(VillagerTradesEvent event) {
        if (event.getType() != VillagerProfession.LIBRARIAN)
            return;

        List<VillagerTrades.ItemListing> list = event.getTrades().get(5);
        List<VillagerTrades.ItemListing> newList = new ArrayList<>(list.size() + 1);
        newList.addAll(list);
        newList.add(new VillagerTrades.ItemListing() {
            @Override
            public @NotNull MerchantOffer getOffer(Entity entity, RandomSource source) {
                return new MerchantOffer(
                        new ItemStack(Items.EMERALD, 12),
                        new ItemStack(Regalliv.ERASER.get(), 1),
                        12, 30, 0.05f
                );
            }
        });

        event.getTrades().put(5, newList);
    }
}
