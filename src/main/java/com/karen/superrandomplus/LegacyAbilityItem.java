package com.karen.superrandomplus;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;

public class LegacyAbilityItem extends Item {
    private final int ability;
    public LegacyAbilityItem(int ability, Properties properties) {
        super(properties);
        this.ability = ability;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer sp) {
            switch (ability) {
                case 0: player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1)); break;
                case 1: player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 240, 0)); break;
                case 2:
                    for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(8), e -> e != player))
                        e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 1));
                    break;
                case 3: player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 160, 0)); break;
                case 4: player.giveExperiencePoints(10); break;
                case 5: player.heal(8.0F); break;
                case 6:
                    BlockPos spawn = level.getSharedSpawnPos();
                    sp.teleportTo(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5);
                    break;
                case 7: player.addEffect(new MobEffectInstance(MobEffects.LUCK, 600, 1)); break;
                case 8:
                    LightningBolt bolt = EntityTypeHelper.lightning(level);
                    if (bolt != null) { bolt.moveTo(player.getX(), player.getY(), player.getZ()); level.addFreshEntity(bolt); }
                    break;
                case 9:
                    Item reward = switch (level.random.nextInt(4)) {
                        case 0 -> Items.EMERALD; case 1 -> Items.GOLD_INGOT; case 2 -> Items.BREAD; default -> Items.DIAMOND;
                    };
                    player.getInventory().add(new ItemStack(reward));
                    break;
            }
            player.getCooldowns().addCooldown(this, 80);
            if (!player.getAbilities().instabuild) stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
    private static final class EntityTypeHelper {
        static LightningBolt lightning(Level l) {
            return l instanceof ServerLevel sl ? net.minecraft.world.entity.EntityType.LIGHTNING_BOLT.create(sl) : null;
        }
    }
}
