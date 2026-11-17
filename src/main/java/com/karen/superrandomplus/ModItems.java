
package com.karen.superrandomplus;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, SuperRandomPlus.MOD_ID);

    public static final RegistryObject<Item> KAREN_CRYSTAL = register("karen_crystal", Rarity.RARE);
    public static final RegistryObject<Item> FIRE_GEM = register("fire_gem", Rarity.EPIC);
    public static final RegistryObject<Item> ICE_GEM = register("ice_gem", Rarity.RARE);
    public static final RegistryObject<Item> SHADOW_ESSENCE = register("shadow_essence", Rarity.EPIC);
    public static final RegistryObject<Item> MAGIC_DUST = register("magic_dust", Rarity.COMMON);
    public static final RegistryObject<Item> GOLDEN_HEART = register("golden_heart", Rarity.RARE);
    public static final RegistryObject<Item> VOID_FRAGMENT = register("void_fragment", Rarity.EPIC);
    public static final RegistryObject<Item> LUCKY_STONE = register("lucky_stone", Rarity.COMMON);
    public static final RegistryObject<Item> THUNDER_CORE = register("thunder_core", Rarity.EPIC);
    public static final RegistryObject<Item> KAREN_TOKEN = register("karen_token", Rarity.COMMON);

    private static RegistryObject<Item> register(String name, Rarity rarity) {
        return ITEMS.register(name,
            () -> new Item(new Item.Properties().rarity(rarity)));
    }
}
