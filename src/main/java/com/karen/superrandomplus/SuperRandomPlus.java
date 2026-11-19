package com.karen.superrandomplus;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

@Mod(SuperRandomPlus.MOD_ID)
public class SuperRandomPlus {
    public static final String MOD_ID = "superrandomplus";
    public SuperRandomPlus() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(bus);
        bus.addListener(this::addCreativeItems);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(this::onBlockBreak);
    }
    private void addCreativeItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.INGREDIENTS) return;
        event.accept(ModItems.KAREN_CRYSTAL); event.accept(ModItems.FIRE_GEM);
        event.accept(ModItems.ICE_GEM); event.accept(ModItems.SHADOW_ESSENCE);
        event.accept(ModItems.MAGIC_DUST); event.accept(ModItems.GOLDEN_HEART);
        event.accept(ModItems.VOID_FRAGMENT); event.accept(ModItems.LUCKY_STONE);
        event.accept(ModItems.THUNDER_CORE); event.accept(ModItems.KAREN_TOKEN);
        for (var item : ModItems.GENERATED) event.accept(item);
    }
    private void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level) || event.getPlayer().isCreative()) return;
        int count = 1 + level.random.nextInt(3);
        for (int n = 0; n < count; n++) {
            var chosen = ModItems.GENERATED.get(level.random.nextInt(ModItems.GENERATED.size())).get();
            ItemEntity drop = new ItemEntity(level, event.getPos().getX()+0.5, event.getPos().getY()+0.5,
                event.getPos().getZ()+0.5, new ItemStack(chosen));
            level.addFreshEntity(drop);
        }
    }
}
