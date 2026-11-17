package com.karen.superrandomplus;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.List;
import java.util.Random;

public class AbilityItem extends Item {
    public enum Ability {
        KAREN_CRYSTAL, FIRE_GEM, ICE_GEM, SHADOW_ESSENCE,
        MAGIC_DUST, GOLDEN_HEART, VOID_FRAGMENT, LUCKY_STONE,
        THUNDER_CORE, KAREN_TOKEN
    }

    private static final Random RANDOM = new Random();
    private final Ability ability;
    private final int cooldownTicks;

    public AbilityItem(Ability ability, Rarity rarity, int cooldownSeconds) {
        super(new Item.Properties().rarity(rarity).stacksTo(16));
        this.ability = ability;
        this.cooldownTicks = cooldownSeconds * 20;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        if (!level.isClientSide()) {
            switch (ability) {
                case KAREN_CRYSTAL -> {
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1));
                    message(player, "سرعت شما برای ۱۰ ثانیه افزایش یافت!");
                }
                case FIRE_GEM -> {
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 240, 0));
                    message(player, "برای ۱۲ ثانیه در برابر آتش مقاوم شدید!");
                }
                case ICE_GEM -> {
                    List<LivingEntity> nearby = level.getEntitiesOfClass(
                        LivingEntity.class, player.getBoundingBox().inflate(6.0),
                        entity -> entity != player && entity.isAlive()
                    );
                    for (LivingEntity entity : nearby) {
                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 1));
                    }
                    message(player, nearby.isEmpty()
                        ? "هیچ موجودی در نزدیکی شما نبود."
                        : nearby.size() + " موجود برای ۶ ثانیه کند شدند!");
                }
                case SHADOW_ESSENCE -> {
                    player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 160, 0));
                    message(player, "برای ۸ ثانیه نامرئی شدید!");
                }
                case MAGIC_DUST -> {
                    player.giveExperiencePoints(10);
                    message(player, "۱۰ امتیاز تجربه دریافت کردید!");
                }
                case GOLDEN_HEART -> {
                    player.heal(8.0F);
                    message(player, "۴ قلب سلامتی بازیابی شد!");
                }
                case VOID_FRAGMENT -> {
                    BlockPos spawn = level.getSharedSpawnPos();
                    player.teleportTo(spawn.getX() + 0.5, spawn.getY() + 1.0, spawn.getZ() + 0.5);
                    level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
                    message(player, "به نقطه شروع این جهان منتقل شدید!");
                }
                case LUCKY_STONE -> {
                    player.addEffect(new MobEffectInstance(MobEffects.LUCK, 600, 1));
                    message(player, "برای ۳۰ ثانیه شانس شما افزایش یافت!");
                }
                case THUNDER_CORE -> {
                    ServerLevel serverLevel = (ServerLevel) level;
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                        player.getX(), player.getY() + 1.0, player.getZ(),
                        45, 1.2, 1.0, 1.2, 0.12);
                    serverLevel.sendParticles(ParticleTypes.FLASH,
                        player.getX(), player.getY() + 1.0, player.getZ(),
                        1, 0.0, 0.0, 0.0, 0.0);
                    level.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER,
                        SoundSource.PLAYERS, 0.7F, 1.4F);
                    message(player, "انرژی رعدوبرق آزاد شد!");
                }
                case KAREN_TOKEN -> {
                    ItemStack reward = switch (RANDOM.nextInt(4)) {
                        case 0 -> new ItemStack(Items.EMERALD, 2);
                        case 1 -> new ItemStack(Items.GOLD_INGOT, 3);
                        case 2 -> new ItemStack(Items.BREAD, 5);
                        default -> new ItemStack(Items.DIAMOND, 1);
                    };
                    if (!player.getInventory().add(reward)) {
                        player.drop(reward, false);
                    }
                    message(player, "جایزه تصادفی Karen Token را دریافت کردید!");
                }
            }

            player.getCooldowns().addCooldown(this, cooldownTicks);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private static void message(Player player, String text) {
        player.sendSystemMessage(Component.literal("[SUPER RANDOM PLUS] " + text));
    }
}
