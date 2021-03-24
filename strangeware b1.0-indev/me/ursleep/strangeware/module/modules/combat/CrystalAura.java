package me.ursleep.strangeware.module.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import me.ursleep.strangeware.Main;
import me.ursleep.strangeware.module.Category;
import me.ursleep.strangeware.module.Module;
import me.ursleep.strangeware.util.api.utils.RenderUtils;
import me.ursleep.strangeware.util.api.utils.RotationUtils;
import me.ursleep.strangeware.util.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Written by @author YourSleep on 20 march of 2021.
 * 
 * It was my first time writing crystalaura.
 * 
 * Copyright 2021
 */

public class CrystalAura extends Module {
    public CrystalAura() {
        super("CrystalAura", "Automatically place and breaks crystals", Category.COMBAT);

        Main.instance.settingsManager.rSetting(new Setting("Explode Range", this, 4.5f, 1, 6.0f, true));
        Main.instance.settingsManager.rSetting(new Setting("Place range", this, 4.5f, 1, 6.0f, true));
        Main.instance.settingsManager.rSetting(new Setting("CA Speed", this, 50, 0, 50, true));
        Main.instance.settingsManager.rSetting(new Setting("Rotations", this, true));
    }

    public static boolean canPlaceCrystal(final BlockPos pos) {
        final Block block = mc.world.getBlockState(pos).getBlock();

        return (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) && mc.world.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.AIR;
    }

    private void destroy_able_crystal() {
    	try {
            for (Entity entity : mc.world.getLoadedEntityList()) { // FIXME: do modern entities iteration
                if (!(entity instanceof EntityEnderCrystal) || mc.player.getDistance(entity) >= Main.instance.settingsManager.getSettingByName(this, "Explode Range").getValDouble()) continue;
                List<EntityLivingBase> targets = mc.world.loadedEntityList.stream()
                        .filter(EntityLivingBase.class::isInstance)
                        .map(EntityLivingBase.class::cast).sorted(Comparator.comparingDouble(t -> t.getDistance(entity)))
                        .collect(Collectors.toList());
                if (can_apply_crystal(targets, entity)) {
                		mc.player.swingArm(EnumHand.MAIN_HAND);
                		mc.player.connection.sendPacket(new CPacketUseEntity(entity));	
                	}
    	}
            } catch (Exception q) {
    			q.printStackTrace();
            }
    }

    private boolean can_apply_crystal(List<EntityLivingBase> targets, Entity crystal) {
        return targets.get(0) != null && targets.get(0).getHealth() > 0 && !targets.get(0).isDead && targets.get(0) != mc.player && targets.get(0).getDistance(crystal) < mc.player.getDistance(crystal);
    }

    private boolean check_crystal_placing(Entity p) {
        boolean haveEndCrystal = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        return !(mc.player.getDistance(p) >= Main.instance.settingsManager.getSettingByName(this, "Place Range").getValDouble() || p == mc.player || !haveEndCrystal || !(p instanceof EntityLivingBase));
    }

    private List<BlockPos> get_bruteforce_poses(BlockPos bp) {
        int xBruteForce = 2;
        int yBruteForce = 1;
        int zBruteForce = 2;
        List<BlockPos> poses = new ArrayList<>();
        for (int x = -xBruteForce; x < xBruteForce; x++) {
            for (int y = -yBruteForce; y < yBruteForce; y++) {
                for (int z = -zBruteForce; z < zBruteForce; z++) {
                    if (!(y == 0 && z == 0 && x == 0)) poses.add(bp.add(x, y, z));
                }
            }
        }
        return poses;
    }

    private long lastMS;

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(double milliseconds) {
        return ((getCurrentMS() - this.lastMS) >= milliseconds);
    }

    public void reset() {
        this.lastMS = getCurrentMS();
    }

    private void place_crystal() {
    	try {
    		for (EntityPlayer p : mc.world.playerEntities) {
                if (!check_crystal_placing(p))
                    continue;

                List<BlockPos> poses = get_bruteforce_poses(new BlockPos(p.posX, p.posY, p.posZ).down());
                BlockPos bestPos = getPlaceBestPos(p, poses);

                if (bestPos == null) return;

                int delay = 0;
                if (hasReached(p.getHealth() + p.getAbsorptionAmount() < Main.instance.settingsManager.getSettingByName(this, "CA Speed").getValDouble() ? 0 : 200)) {
                    place(bestPos);
                    reset();
                }

                poses.clear();
            }
    	} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
    private BlockPos getPlaceBestPos(Entity p, List<BlockPos> poses) {
        BlockPos bestPos = null;

        for (BlockPos nigg: poses) {
            if (!canPlaceCrystal(nigg) || !mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(nigg)).isEmpty()) continue;
            if (bestPos == null) {
                bestPos = nigg;
            } else if (bestPos.getDistance((int)p.posX, (int)p.posY, (int)p.posZ) > nigg.getDistance((int)p.posX, (int)p.posY, (int)p.posZ)) {
                bestPos = nigg;
            }
        }
        return bestPos;
    }

    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (!(entity instanceof EntityEnderCrystal) || mc.player.getDistance(entity) >= Main.instance.settingsManager.getSettingByName(this, "Explode Range").getValDouble()) continue;
            RenderUtils.drawBlockESP(new BlockPos(entity.posX, entity.posY, entity.posZ), 255, 0, 0);
        }

        for (Entity p : mc.world.getLoadedEntityList()) {
            if (!check_crystal_placing(p))
                continue;
            List<BlockPos> poses = get_bruteforce_poses(new BlockPos(p.posX, p.posY, p.posZ).down());

            BlockPos bestPos = getPlaceBestPos(p, poses);

            if (bestPos == null) return;

            RenderUtils.drawBlockESP(bestPos, 0, 255, 0);

            poses.clear();
        }
    }
    @SubscribeEvent
        public void handler(TickEvent.ClientTickEvent event) {
            destroy_able_crystal();
            place_crystal();
    }

    private void place(BlockPos pos) {
        if (pos.getDistance((int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ) >= Main.instance.settingsManager.getSettingByName(this, "Place Range").getValDouble() * 3) return;
        if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
        } else if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.DOWN, EnumHand.OFF_HAND, 0, 0, 0));
        }
    }

}