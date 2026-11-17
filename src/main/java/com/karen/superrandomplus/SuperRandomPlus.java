
package com.karen.superrandomplus;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
    private static final List<Item> RANDOM_ITEMS = new ArrayList<>();

    public SuperRandomPlus() {
        for (Item item : ForgeRegistries.ITEMS) {
            if (item != Items.AIR && !item.getDefaultInstance().isEmpty()) {
                RANDOM_ITEMS.add(item);
            }
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer().level().isClientSide()) {
            return;
        }

        if (RANDOM_ITEMS.isEmpty()) {
            return;
        }

        Item randomItem = RANDOM_ITEMS.get(
            RANDOM.nextInt(RANDOM_ITEMS.size())
        );

        event.getPlayer().getInventory().add(randomItem.getDefaultInstance());
    }
}
