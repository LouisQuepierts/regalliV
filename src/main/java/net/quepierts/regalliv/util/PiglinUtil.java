package net.quepierts.regalliv.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.RandomSource;
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

        return predicate.test(itemStack) && itemStack.getCount() >= predicate.min();
    }

    public static boolean isAdmirableItemIgnoredCost(ItemStack itemStack) {
        ItemPredicate predicate = LOOT_ITEMS.get(itemStack.getItem());

        if (predicate == null)
            return false;

        return predicate.test(itemStack);
    }

    public static int getRandomCost(ItemStack itemStack, RandomSource random) {
        ItemPredicate predicate = LOOT_ITEMS.get(itemStack.getItem());
        if (predicate == null)
            return 0;
        if (predicate.min() == predicate.max())
            return predicate.min();
        return Math.min(random.nextInt(predicate.min(), predicate.max()), itemStack.getCount());

    }

    static {
        LOOT_ITEMS = ImmutableMap.<Item, ItemPredicate>builder()
                .put(Items.BOOK, new PredicateHasEnchant(Items.BOOK, Enchantments.SOUL_SPEED))
                .put(Items.IRON_BOOTS, new PredicateHasEnchant(Items.IRON_BOOTS, Enchantments.SOUL_SPEED))
                .put(Items.POTION, PiglinUtil::checkPotion)
                .put(Items.SPLASH_POTION, PiglinUtil::checkSplashPotion)
                .put(Items.IRON_NUGGET, new PredicateCountedItem(10, 36))
                .put(Items.ENDER_PEARL, new PredicateCountedItem(2, 4))
                .put(Items.STRING, new PredicateCountedItem(3, 9))
                .put(Items.QUARTZ, new PredicateCountedItem(5, 12))
                .put(Items.OBSIDIAN, new PredicateCountedItem(1, 1))
                .put(Items.CRYING_OBSIDIAN, new PredicateCountedItem(1, 3))
                .put(Items.FIRE_CHARGE, new PredicateCountedItem(1, 1))
                .put(Items.LEATHER, new PredicateCountedItem(2, 4))
                .put(Items.SOUL_SAND, new PredicateCountedItem(2, 8))
                .put(Items.NETHER_BRICK, new PredicateCountedItem(2, 8))
                .put(Items.SPECTRAL_ARROW, new PredicateCountedItem(6, 12))
                .put(Items.GRAVEL, new PredicateCountedItem(8, 16))
                .put(Items.BLACKSTONE, new PredicateCountedItem(8, 16))
                .build();
    }

    @FunctionalInterface
    private interface ItemPredicate {
        default int min() {
            return 1;
        }
        default int max() {
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

    private record PredicateCountedItem(int min, int max) implements ItemPredicate {
        @Override
        public boolean test(ItemStack itemStack) {
            return true;
        }

        @Override
        public int min() {
            return min;
        }

        @Override
        public int max() {
            return max;
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
