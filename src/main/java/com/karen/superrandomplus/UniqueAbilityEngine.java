package com.karen.superrandomplus;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.Locale;

public final class UniqueAbilityEngine {
    private UniqueAbilityEngine() {}

    public static void activate(Level level, Player player, String category, int power) {
        int ability = (power - 1) % 12;
        BlockPos origin = player.blockPosition().relative(player.getDirection(), 3);
        switch (ability) {
            case 0 -> buildTank(level, origin, player.getDirection(), power);
            case 1 -> buildHouse(level, origin, player.getDirection(), category);
            case 2 -> buildBridge(level, origin, player.getDirection(), power);
            case 3 -> buildTower(level, origin, power);
            case 4 -> summonAnimal(level, player, power);
            case 5 -> summonGolem(level, player, power);
            case 6 -> reward(player, power, category);
            case 7 -> boost(player, power, category);
            case 8 -> shortTeleport(level, player, power);
            case 9 -> buildBeacon(level, origin, power);
            case 10 -> buildGarden(level, origin, player.getDirection(), power);
            default -> magicBurst(level, player, power);
        }
        player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
            "[SUPER RANDOM PLUS] Ability activated: " + category.toUpperCase(Locale.ROOT) + " #" + power));
    }

    private static BlockPos local(BlockPos base, Direction forward, int side, int up, int ahead) {
        Direction right = forward.getClockWise();
        return base.offset(right.getStepX() * side + forward.getStepX() * ahead,
                up, right.getStepZ() * side + forward.getStepZ() * ahead);
    }

    private static void set(Level level, BlockPos pos, net.minecraft.world.level.block.Block block) {
        if (level.isLoaded(pos) && level.getBlockState(pos).isAir()) {
            level.setBlock(pos, block.defaultBlockState(), 3);
        }
    }

    // A small block-built Minecraft tank sculpture, not a functional weapon.
    private static void buildTank(Level level, BlockPos base, Direction forward, int power) {
        int length = 3 + Math.min(2, power / 40);
        for (int x = -1; x <= 1; x++) {
            for (int z = -length; z <= 0; z++) {
                set(level, local(base, forward, x, 0, z), Blocks.BLACK_CONCRETE);
                set(level, local(base, forward, x, 1, z), Blocks.GRAY_CONCRETE);
            }
        }
        for (int x = -1; x <= 1; x++) {
            set(level, local(base, forward, x, 2, -1), Blocks.IRON_BLOCK);
        }
        set(level, local(base, forward, 0, 3, -1), Blocks.IRON_BLOCK);
        set(level, local(base, forward, 0, 3, -2), Blocks.IRON_BLOCK);
        set(level, local(base, forward, 0, 3, -3), Blocks.IRON_BLOCK);
        set(level, local(base, forward, 0, 3, -4), Blocks.IRON_BLOCK);
    }

    private static void buildHouse(Level level, BlockPos base, Direction forward, String category) {
        net.minecraft.world.level.block.Block wall = category.equals("technology") || category.equals("gadget")
            ? Blocks.IRON_BLOCK : Blocks.OAK_PLANKS;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                set(level, local(base, forward, x, 0, z), Blocks.OAK_PLANKS);
                if (Math.abs(x) == 2 || Math.abs(z) == 2) {
                    for (int y = 1; y <= 3; y++) {
                        if (!(x == 0 && z == 2 && y <= 2)) set(level, local(base, forward, x, y, z), wall);
                    }
                }
            }
        }
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++)
                set(level, local(base, forward, x, 4, z), Blocks.OAK_SLAB);
        set(level, local(base, forward, -2, 2, 0), Blocks.GLASS);
        set(level, local(base, forward, 2, 2, 0), Blocks.GLASS);
        set(level, local(base, forward, 0, 3, 2), Blocks.LANTERN);
    }

    private static void buildBridge(Level level, BlockPos base, Direction forward, int power) {
        int length = 5 + Math.min(4, power / 25);
        for (int z = 0; z < length; z++) {
            for (int x = -1; x <= 1; x++) {
                set(level, local(base, forward, x, 0, z), Blocks.STONE_BRICKS);
            }
            set(level, local(base, forward, -2, 1, z), Blocks.OAK_FENCE);
            set(level, local(base, forward, 2, 1, z), Blocks.OAK_FENCE);
        }
    }

    private static void buildTower(Level level, BlockPos base, int power) {
        int height = 4 + Math.min(5, power / 20);
        for (int y = 0; y < height; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 || z == 0 || y == 0) set(level, base.offset(x, y, z), Blocks.COBBLESTONE);
                }
            }
        }
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++)
                set(level, base.offset(x, height, z), Blocks.STONE_BRICK_SLAB);
        set(level, base.offset(0, height + 1, 0), Blocks.LANTERN);
    }

    private static void summonAnimal(Level level, Player player, int power) {
        Entity entity;
        if (power % 3 == 0) entity = EntityType.WOLF.create(level);
        else if (power % 3 == 1) entity = EntityType.COW.create(level);
        else entity = EntityType.PIG.create(level);
        if (entity == null) return;
        entity.moveTo(player.getX() + 1.5, player.getY(), player.getZ() + 1.5, player.getYRot(), 0);
        level.addFreshEntity(entity);
    }

    private static void summonGolem(Level level, Player player, int power) {
        IronGolem golem = EntityType.IRON_GOLEM.create(level);
        if (golem != null) {
            golem.moveTo(player.getX() + 2, player.getY(), player.getZ() + 2, player.getYRot(), 0);
            golem.setPlayerCreated(true);
            level.addFreshEntity(golem);
        }
    }

    private static void reward(Player player, int power, String category) {
        Item item = switch (power % 6) {
            case 0 -> Items.DIAMOND;
            case 1 -> Items.EMERALD;
            case 2 -> Items.GOLD_INGOT;
            case 3 -> Items.ENDER_PEARL;
            case 4 -> Items.EXPERIENCE_BOTTLE;
            default -> Items.AMETHYST_SHARD;
        };
        int amount = 1 + Math.min(4, power / 25);
        ItemStack stack = new ItemStack(item, amount);
        if (!player.getInventory().add(stack)) player.drop(stack, false);
    }

    private static void boost(Player player, int power, String category) {
        int duration = 100 + power * 10;
        switch (power % 4) {
            case 0 -> player.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.MOVEMENT_SPEED, duration, Math.min(2, power / 40)));
            case 1 -> player.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.JUMP, duration, Math.min(2, power / 40)));
            case 2 -> player.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.REGENERATION, duration, Math.min(1, power / 60)));
            default -> player.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.DAMAGE_RESISTANCE, duration, Math.min(1, power / 60)));
        }
    }

    private static void shortTeleport(Level level, Player player, int power) {
        Direction d = player.getDirection();
        int distance = 3 + power % 5;
        double x = player.getX() + d.getStepX() * distance;
        double z = player.getZ() + d.getStepZ() * distance;
        for (int dy = 2; dy >= -2; dy--) {
            double y = player.getY() + dy;
            if (level.noCollision(player, player.getBoundingBox().move(x - player.getX(), y - player.getY(), z - player.getZ()))) {
                player.teleportTo(x, y, z);
                return;
            }
        }
    }

    private static void buildBeacon(Level level, BlockPos base, int power) {
        int h = 2 + Math.min(4, power / 20);
        for (int y = 0; y < h; y++) set(level, base.offset(0, y, 0), Blocks.GLOWSTONE);
        for (int x = -1; x <= 1; x++)
            for (int z = -1; z <= 1; z++)
                set(level, base.offset(x, h, z), Blocks.GLASS);
        if (level instanceof ServerLevel server)
            server.sendParticles(ParticleTypes.END_ROD, base.getX()+0.5, base.getY()+h+1, base.getZ()+0.5, 25, 0.4, 0.6, 0.4, 0.02);
    }

    private static void buildGarden(Level level, BlockPos base, Direction forward, int power) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                set(level, local(base, forward, x, 0, z), Blocks.FARMLAND);
                set(level, local(base, forward, x, 1, z), power % 2 == 0 ? Blocks.CARROTS.defaultBlockState().getBlock() : Blocks.WHEAT.defaultBlockState().getBlock());
            }
        }
        set(level, local(base, forward, 0, 0, 0), Blocks.WATER);
    }

    private static void magicBurst(Level level, Player player, int power) {
        player.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.NIGHT_VISION, 200 + power * 5, 0));
        if (level instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY()+1, player.getZ(), 35, 1, 1, 1, 0.15);
        }
    }
}
