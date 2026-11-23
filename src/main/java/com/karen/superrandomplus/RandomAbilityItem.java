package com.karen.superrandomplus;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.food.FoodProperties;

public class RandomAbilityItem extends Item {
    private final String category;
    private final int power;

    public RandomAbilityItem(String category, int power, Properties properties) {
        super(category.equals("food")
            ? properties.food(new FoodProperties.Builder().nutrition(4 + power % 5)
                .saturationMod(0.4F + (power % 5) * 0.1F).alwaysEat().build())
            : properties);
        this.category = category;
        this.power = power;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            UniqueAbilityEngine.activate(level, player, category, power);
            player.getCooldowns().addCooldown(this, 60);
            if (!player.getAbilities().instabuild) stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
