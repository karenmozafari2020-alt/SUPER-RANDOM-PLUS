package com.karen.superrandomplus;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SuperRandomPlus.MOD_ID)
public class SuperRandomPlus {
    public static final String MOD_ID = "superrandomplus";

    public SuperRandomPlus() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modBus);
        modBus.addListener(this::addCreativeItems);
    }

    private void addCreativeItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.INGREDIENTS) return;
        event.accept(ModItems.KAREN_CRYSTAL);
        event.accept(ModItems.FIRE_GEM);
        event.accept(ModItems.ICE_GEM);
        event.accept(ModItems.SHADOW_ESSENCE);
        event.accept(ModItems.MAGIC_DUST);
        event.accept(ModItems.GOLDEN_HEART);
        event.accept(ModItems.VOID_FRAGMENT);
        event.accept(ModItems.LUCKY_STONE);
        event.accept(ModItems.THUNDER_CORE);
        event.accept(ModItems.KAREN_TOKEN);
        for (var item : ModItems.GENERATED) event.accept(item);
    }
}
