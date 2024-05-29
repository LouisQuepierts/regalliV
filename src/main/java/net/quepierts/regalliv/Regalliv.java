package net.quepierts.regalliv;

import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Regalliv.MODID)
@Mod.EventBusSubscriber(modid = Regalliv.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Regalliv {
    public static final String MODID = "regalliv";

    static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    static final RegistryObject<EraserItem> ERASER = ITEMS.register("eraser", () -> new EraserItem(new Item.Properties()));

    public Regalliv() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(modEventBus);
    }

    public static MerchantOffer flip(MerchantOffer offer) {
        return new MerchantOffer(offer.getResult(), ItemStack.EMPTY, offer.getBaseCostA(), offer.getUses(), offer.getMaxUses(), offer.getXp(), offer.getPriceMultiplier(), offer.getDemand());
    }

    public static void flip(AbstractVillager villager) {
        MerchantOffers offers = villager.getOffers();
        MerchantOffers flipped = new MerchantOffers();

        for (MerchantOffer offer : offers) {
            flipped.add(Regalliv.flip(offer));
        }

        villager.overrideOffers(flipped);
    }

    @SubscribeEvent
    public static void onCreativeTabBuildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ERASER.get());
        }
    }
}
