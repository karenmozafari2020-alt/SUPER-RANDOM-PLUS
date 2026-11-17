package com.karen.superrandomplus;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, SuperRandomPlus.MOD_ID);

    public static final RegistryObject<Item> KAREN_CRYSTAL =
        register("karen_crystal", AbilityItem.Ability.KAREN_CRYSTAL, Rarity.RARE, 30);
    public static final RegistryObject<Item> FIRE_GEM =
        register("fire_gem", AbilityItem.Ability.FIRE_GEM, Rarity.EPIC, 45);
    public static final RegistryObject<Item> ICE_GEM =
        register("ice_gem", AbilityItem.Ability.ICE_GEM, Rarity.RARE, 35);
    public static final RegistryObject<Item> SHADOW_ESSENCE =
        register("shadow_essence", AbilityItem.Ability.SHADOW_ESSENCE, Rarity.EPIC, 45);
    public static final RegistryObject<Item> MAGIC_DUST =
        register("magic_dust", AbilityItem.Ability.MAGIC_DUST, Rarity.COMMON, 10);
    public static final RegistryObject<Item> GOLDEN_HEART =
        register("golden_heart", AbilityItem.Ability.GOLDEN_HEART, Rarity.RARE, 25);
    public static final RegistryObject<Item> VOID_FRAGMENT =
        register("void_fragment", AbilityItem.Ability.VOID_FRAGMENT, Rarity.EPIC, 60);
    public static final RegistryObject<Item> LUCKY_STONE =
        register("lucky_stone", AbilityItem.Ability.LUCKY_STONE, Rarity.COMMON, 40);
    public static final RegistryObject<Item> THUNDER_CORE =
        register("thunder_core", AbilityItem.Ability.THUNDER_CORE, Rarity.EPIC, 30);
    public static final RegistryObject<Item> KAREN_TOKEN =
        register("karen_token", AbilityItem.Ability.KAREN_TOKEN, Rarity.COMMON, 20);

    private static RegistryObject<Item> register(
        String name, AbilityItem.Ability ability, Rarity rarity, int cooldownSeconds
    ) {
        return ITEMS.register(name,
            () -> new AbilityItem(ability, rarity, cooldownSeconds));
    }
}
