package com.karen.superrandomplus;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, SuperRandomPlus.MOD_ID);

    public static final RegistryObject<Item> KAREN_CRYSTAL = legacy("karen_crystal", 0);
    public static final RegistryObject<Item> FIRE_GEM = legacy("fire_gem", 1);
    public static final RegistryObject<Item> ICE_GEM = legacy("ice_gem", 2);
    public static final RegistryObject<Item> SHADOW_ESSENCE = legacy("shadow_essence", 3);
    public static final RegistryObject<Item> MAGIC_DUST = legacy("magic_dust", 4);
    public static final RegistryObject<Item> GOLDEN_HEART = legacy("golden_heart", 5);
    public static final RegistryObject<Item> VOID_FRAGMENT = legacy("void_fragment", 6);
    public static final RegistryObject<Item> LUCKY_STONE = legacy("lucky_stone", 7);
    public static final RegistryObject<Item> THUNDER_CORE = legacy("thunder_core", 8);
    public static final RegistryObject<Item> KAREN_TOKEN = legacy("karen_token", 9);

    public static final List<RegistryObject<Item>> GENERATED = new ArrayList<>();
    public static final String[] CATEGORIES = {
        "weapon","tool","armor","food","potion","magic","technology","gadget",
        "strange","hidden","rare","legendary","crystal","material","relic"
    };

    private static RegistryObject<Item> legacy(String id, int ability) {
        return ITEMS.register(id, () -> new LegacyAbilityItem(ability, new Item.Properties()));
    }

    private static String number(int i) {
        return String.format(Locale.ROOT, "%03d", i);
    }

    static {
        for (String category : CATEGORIES) {
            for (int i = 1; i <= 100; i++) {
                final int power = i;
                final String id = category + "_" + number(i);
                GENERATED.add(ITEMS.register(id, () ->
                    new RandomAbilityItem(category, power,
                        new Item.Properties().rarity(rarityFor(category, power)))));
            }
        }
    }

    private static Rarity rarityFor(String category, int power) {
        if (category.equals("legendary") || category.equals("relic") || power >= 90) return Rarity.EPIC;
        if (category.equals("rare") || category.equals("hidden") || category.equals("crystal") || power >= 65) return Rarity.RARE;
        if (power >= 35) return Rarity.UNCOMMON;
        return Rarity.COMMON;
    }

    private ModItems() {}
}
