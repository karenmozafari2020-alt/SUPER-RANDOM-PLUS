package com.karen.superrandomplus;


import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SuperRandomPlus.MOD_ID);

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
    public static final String[] CATEGORIES = {"weapon","tool","armor","food","potion","magic","technology","gadget","strange","hidden","rare","legendary","crystal","material","relic"};

    private static RegistryObject<Item> legacy(String id, int ability) {
        return ITEMS.register(id, () -> new LegacyAbilityItem(ability, new Item.Properties()));
    }
    private static String number(int n, int digits) { return String.format(Locale.ROOT, "%0" + digits + "d", n); }

    private static final Tier KAREN_TIER = new Tier() {
        public int getUses() { return 1800; }
        public float getSpeed() { return 9.0F; }
        public float getAttackDamageBonus() { return 4.5F; }
        public int getLevel() { return 4; }
        public int getEnchantmentValue() { return 22; }
        public Ingredient getRepairIngredient() { return Ingredient.of(Items.DIAMOND); }
    };

    private static RegistryObject<Item> tool(String id, String type, int rank) {
        return ITEMS.register(id, () -> {
            Item.Properties p = new Item.Properties().rarity(rank >= 3 ? Rarity.EPIC : rank == 2 ? Rarity.RARE : Rarity.UNCOMMON);
            return switch (type) {
                case "sword" -> new SwordItem(KAREN_TIER, 5 + rank, -2.4F - rank * 0.04F, p);
                case "pickaxe" -> new PickaxeItem(KAREN_TIER, 2 + rank, -2.8F, p);
                case "axe" -> new AxeItem(KAREN_TIER, 7 + rank, -3.1F, p);
                case "shovel" -> new ShovelItem(KAREN_TIER, 2 + rank, -3.0F, p);
                case "hoe" -> new HoeItem(KAREN_TIER, -2 + rank, -1.0F, p);
                default -> new Item(p);
            };
        });
    }
    private static void addTools(String type, int count, String prefix) {
        for (int i = 1; i <= count; i++) GENERATED.add(tool(prefix + number(i, 2), type, i % 4));
    }
    private static void addMaterials(String prefix, int count, int digits) {
        for (int i = 1; i <= count; i++) {
            final int r = i % 4;
            GENERATED.add(ITEMS.register(prefix + number(i, digits), () -> new Item(new Item.Properties().rarity(r >= 3 ? Rarity.EPIC : r == 2 ? Rarity.RARE : Rarity.COMMON))));
        }
    }
    private static Rarity rarityFor(String category, int power) {
        if (category.equals("legendary") || category.equals("relic") || power >= 90) return Rarity.EPIC;
        if (category.equals("rare") || category.equals("hidden") || category.equals("crystal") || power >= 65) return Rarity.RARE;
        if (power >= 35) return Rarity.UNCOMMON;
        return Rarity.COMMON;
    }
    static {
        // 1,500 category items
        for (String category : CATEGORIES) for (int i = 1; i <= 100; i++) {
            final int power = i;
            final String id = category + "_" + number(i, 3);
            GENERATED.add(ITEMS.register(id, () -> new RandomAbilityItem(category, power, new Item.Properties().rarity(rarityFor(category, power)))));
        }
        // Restore the original Crystal tool IDs used by the legacy models and recipes
        addTools("sword", 40, "crystal_sword_");
        addTools("pickaxe", 30, "crystal_pickaxe_");
        addTools("axe", 25, "crystal_axe_");
        addTools("shovel", 20, "crystal_shovel_");
        addTools("hoe", 15, "crystal_hoe_");
        addMaterials("karen_material_", 40, 2);
        for (int i = 1; i <= 20; i++) {
            final int r = i % 4;
            GENERATED.add(ITEMS.register("karen_special_" + number(i, 2), () -> new Item(new Item.Properties().rarity(r >= 3 ? Rarity.EPIC : r == 2 ? Rarity.RARE : Rarity.UNCOMMON))));
        }
    }
    private ModItems() {}
}
