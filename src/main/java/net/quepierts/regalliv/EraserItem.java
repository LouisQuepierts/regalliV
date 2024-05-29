package net.quepierts.regalliv;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EraserItem extends Item {
    public EraserItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, InteractionHand p_41401_) {
        if (!(entity instanceof Player)) {
            if (!player.level().isClientSide && entity.isAlive() && entity.hasCustomName()) {
                if (entity instanceof AbstractVillager villager) {
                    if (villager.getCustomName().getString().equals("Dinnerbone")) {
                        Regalliv.flip(villager);
                    }
                }

                entity.setCustomName(null);
                itemStack.shrink(1);
            }

            return InteractionResult.sidedSuccess(player.level().isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}
