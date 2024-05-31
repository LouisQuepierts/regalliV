package net.quepierts.regalliv.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class PiglinUtil {
    private static final ImmutableMap<Item, ItemPredicate> LOOT_ITEMS;

    public static boolean isAdmirableItem(ItemStack itemStack) {
        ItemPredicate predicate = LOOT_ITEMS.get(itemStack.getItem());

        if (predicate == null)
            return false;

        return predicate.test(itemStack) && predicate.amount() <= itemStack.getCount();
    }

    public static boolean isAdmirableItemIgnoredCost(ItemStack itemStack) {
        ItemPredicate predicate = LOOT_ITEMS.get(itemStack.getItem());

        if (predicate == null)
            return false;

        return predicate.test(itemStack);
    }

    public static int getItemCost(ItemStack itemStack) {
        ItemPredicate predicate = LOOT_ITEMS.get(itemStack.getItem());
        return predicate == null ? 0 : predicate.amount();
    }

    static {
        LOOT_ITEMS = ImmutableMap.<Item, ItemPredicate>builder()
                .put(Items.BOOK, new PredicateHasEnchant(Items.BOOK, Enchantments.SOUL_SPEED))
                .put(Items.IRON_BOOTS, new PredicateHasEnchant(Items.IRON_BOOTS, Enchantments.SOUL_SPEED))
                .put(Items.POTION, PiglinUtil::checkPotion)
                .put(Items.SPLASH_POTION, PiglinUtil::checkSplashPotion)
                .put(Items.IRON_NUGGET, new PredicateCountedItem(16))
                .put(Items.ENDER_PEARL, new PredicateCountedItem(3))
                .put(Items.STRING, new PredicateCountedItem(6))
                .put(Items.QUARTZ, new PredicateCountedItem(8))
                .put(Items.OBSIDIAN, new PredicateCountedItem(1))
                .put(Items.CRYING_OBSIDIAN, new PredicateCountedItem(2))
                .put(Items.FIRE_CHARGE, new PredicateCountedItem(1))
                .put(Items.LEATHER, new PredicateCountedItem(3))
                .put(Items.SOUL_SAND, new PredicateCountedItem(5))
                .put(Items.NETHER_BRICK, new PredicateCountedItem(5))
                .put(Items.SPECTRAL_ARROW, new PredicateCountedItem(9))
                .put(Items.GRAVEL, new PredicateCountedItem(12))
                .put(Items.BLACKSTONE, new PredicateCountedItem(12))
                .build();
    }

    @FunctionalInterface
    private interface ItemPredicate {
        default int amount() {
            return 1;
        }

        boolean test(ItemStack itemStack);
    }

    private record PredicateHasEnchant(Item item, Enchantment enchantment) implements ItemPredicate {
        @Override
        public boolean test(ItemStack itemStack) {
            return itemStack.is(item) && itemStack.getEnchantmentLevel(enchantment) != 0;
        }
    }

    private record PredicateCountedItem(int minimal) implements ItemPredicate {
        @Override
        public boolean test(ItemStack itemStack) {
            return true;
        }

        @Override
        public int amount() {
            return minimal;
        }
    }

    private static boolean checkPotion(ItemStack item) {
        List<MobEffectInstance> effects = PotionUtils.getPotion(item).getEffects();
        return effects.isEmpty() || effects.get(0).getEffect() == MobEffects.FIRE_RESISTANCE;
    }

    private static boolean checkSplashPotion(ItemStack item) {
        List<MobEffectInstance> effects = PotionUtils.getPotion(item).getEffects();
        return !effects.isEmpty() && effects.get(0).getEffect() == MobEffects.FIRE_RESISTANCE;
    }
}
