package com.karen.superrandomplus;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class RandomAbilityItem extends Item {
    private final String category;
    private final int power;
    public RandomAbilityItem(String category, int power, Properties properties) {
        super(category.equals("food") ? properties.food(new FoodProperties.Builder().nutrition(4 + power % 5).saturationMod(0.4F + (power % 5)*0.1F).alwaysEat().build()) : properties);
        this.category = category;
        this.power = power;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            int duration = 200 + power * 10;
            int amp = Math.min(2, (power - 1) / 35);
            switch (category) {
                case "food":
                    player.heal(1.0F + power % 8);
                    player.getFoodData().eat(4 + power % 5, 0.5F + (power % 5)*0.1F);
                    break;
                case "weapon": player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, amp)); break;
                case "tool": player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, duration, amp)); break;
                case "armor": player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, amp)); break;
                case "technology": case "gadget":
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, amp));
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, duration, 0));
                    break;
                case "rare": case "legendary": case "relic":
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration, amp));
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, amp));
                    break;
                default:
                    MobEffect[] effects = {MobEffects.JUMP, MobEffects.FIRE_RESISTANCE, MobEffects.WATER_BREATHING,
                        MobEffects.REGENERATION, MobEffects.MOVEMENT_SPEED, MobEffects.DAMAGE_RESISTANCE};
                    player.addEffect(new MobEffectInstance(effects[(power - 1) % effects.length], duration, amp));
                    break;
            }
            player.getCooldowns().addCooldown(this, 60);
            if (!player.getAbilities().instabuild) stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
