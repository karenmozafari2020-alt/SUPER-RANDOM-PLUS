
package com.karen.superrandomplus;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod(SuperRandomPlus.MOD_ID)
public class SuperRandomPlus {

    public static final String MOD_ID = "superrandomplus";

    private static final Random RANDOM = new Random();

    private static final List<Item> COMMON = new ArrayList<>();
    private static final List<Item> RARE = new ArrayList<>();
    private static final List<Item> LEGENDARY = new ArrayList<>();

    private static boolean loaded = false;

    public SuperRandomPlus() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static void loadItems() {
        if (loaded) return;

        for (Item item : ForgeRegistries.ITEMS) {
            if (item == Items.AIR) continue;

            ItemStack stack = item.getDefaultInstance();
            if (stack.isEmpty()) continue;

            Rarity rarity = item.getRarity(stack);

            switch (rarity) {
                case COMMON:
                    COMMON.add(item);
                    break;
                case UNCOMMON:
                case RARE:
                    RARE.add(item);
                    break;
                case EPIC:
                    LEGENDARY.add(item);
                    break;
            }
        }

        loaded = true;
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer().level().isClientSide()) return;

        loadItems();

        int chance = RANDOM.nextInt(100);

        List<Item> pool;
        String label;
        ChatFormatting color;

        if (chance < 70) {
            pool = COMMON;
            label = "COMMON";
            color = ChatFormatting.GREEN;
        } else if (chance < 95) {
            pool = RARE;
            label = "RARE";
            color = ChatFormatting.BLUE;
        } else {
            pool = LEGENDARY;
            label = "LEGENDARY";
            color = ChatFormatting.LIGHT_PURPLE;
        }

        if (pool.isEmpty()) {
            pool = COMMON;
            label = "COMMON";
            color = ChatFormatting.GREEN;
        }

        if (pool.isEmpty()) return;

        Item item = pool.get(RANDOM.nextInt(pool.size()));

        event.getPlayer().getInventory().add(item.getDefaultInstance());

        event.getPlayer().sendSystemMessage(
            Component.literal("[SUPER RANDOM PLUS] ")
                .withStyle(ChatFormatting.GOLD)
                .append(Component.literal(label + " ")
                    .withStyle(color))
                .append(Component.literal(item.getDescription().getString())
                    .withStyle(ChatFormatting.WHITE))
        );
    }
}
