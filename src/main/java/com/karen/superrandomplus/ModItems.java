package com.karen.superrandomplus;

import net.minecraft.world.item.*;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Registers 200 items for SUPER-RANDOM-PLUS.
 * 150 functional tools and 50 crafting materials.
 */
public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, SuperRandomPlus.MOD_ID);

    // Keep the existing special items
    public static final RegistryObject<Item> KAREN_CRYSTAL = simple("karen_crystal", Rarity.RARE);
    public static final RegistryObject<Item> FIRE_GEM = simple("fire_gem", Rarity.EPIC);
    public static final RegistryObject<Item> ICE_GEM = simple("ice_gem", Rarity.RARE);
    public static final RegistryObject<Item> SHADOW_ESSENCE = simple("shadow_essence", Rarity.EPIC);
    public static final RegistryObject<Item> MAGIC_DUST = simple("magic_dust", Rarity.COMMON);
    public static final RegistryObject<Item> GOLDEN_HEART = simple("golden_heart", Rarity.RARE);
    public static final RegistryObject<Item> VOID_FRAGMENT = simple("void_fragment", Rarity.EPIC);
    public static final RegistryObject<Item> LUCKY_STONE = simple("lucky_stone", Rarity.COMMON);
    public static final RegistryObject<Item> THUNDER_CORE = simple("thunder_core", Rarity.EPIC);
    public static final RegistryObject<Item> KAREN_TOKEN = simple("karen_token", Rarity.COMMON);

    public static final List<RegistryObject<Item>> GENERATED = new ArrayList<>();

    private static RegistryObject<Item> simple(String id, Rarity rarity) {
        return ITEMS.register(id, () -> new Item(new Item.Properties().rarity(rarity)));
    }

    private static final Tier KAREN_TIER = new Tier() {
        public int getUses() { return 1800; }
        public float getSpeed() { return 9.0F; }
        public float getAttackDamageBonus() { return 4.5F; }
        public int getLevel() { return 4; }
        public int getEnchantmentValue() { return 22; }
        public net.minecraft.world.item.crafting.Ingredient getRepairIngredient() {
            return net.minecraft.world.item.crafting.Ingredient.of(Items.DIAMOND);
        }
    };

    private static RegistryObject<Item> tool(String id, String kind, int rarityIndex) {
        return ITEMS.register(id, () -> {
            Item.Properties props = new Item.Properties().rarity(
                rarityIndex >= 3 ? Rarity.EPIC : rarityIndex == 2 ? Rarity.RARE : Rarity.UNCOMMON);
            return switch (kind) {
                case "sword" -> new SwordItem(KAREN_TIER, 5 + rarityIndex, -2.4F - rarityIndex*0.04F, props);
                case "pickaxe" -> new PickaxeItem(KAREN_TIER, 2 + rarityIndex, -2.8F, props);
                case "axe" -> new AxeItem(KAREN_TIER, 7 + rarityIndex, -3.1F, props);
                case "shovel" -> new ShovelItem(KAREN_TIER, 2 + rarityIndex, -3.0F, props);
                case "hoe" -> new HoeItem(KAREN_TIER, -2 + rarityIndex, -1.0F, props);
                default -> new Item(props);
            };
        });
    }

    private static void addTools(String kind, int count, String prefix) {
        for (int i = 1; i <= count; i++) {
            int rarity = (i % 4);
            GENERATED.add(tool(prefix + String.format("%02d", i), kind, rarity));
        }
    }

    private static void addMaterials(String prefix, int count) {
        for (int i = 1; i <= count; i++) {
            final int r = i % 4;
            GENERATED.add(ITEMS.register(prefix + String.format("%02d", i),
                () -> new Item(new Item.Properties().rarity(r >= 3 ? Rarity.EPIC : r == 2 ? Rarity.RARE : Rarity.COMMON))));
        }
    }

    static {
        addTools("sword", 40, "crystal_sword_");
        addTools("pickaxe", 30, "crystal_pickaxe_");
        addTools("axe", 25, "crystal_axe_");
        addTools("shovel", 20, "crystal_shovel_");
        addTools("hoe", 15, "crystal_hoe_");
        addMaterials("karen_material_", 40);
        // 20 special item registrations (generic items until custom effects are wired)
        for (int i = 1; i <= 20; i++) {
            final int rarity = i % 4;
            GENERATED.add(ITEMS.register("karen_special_" + String.format("%02d", i),
                () -> new Item(new Item.Properties().rarity(rarity >= 3 ? Rarity.EPIC : rarity == 2 ? Rarity.RARE : Rarity.UNCOMMON))));
        }
    }

    private ModItems() {}
}
