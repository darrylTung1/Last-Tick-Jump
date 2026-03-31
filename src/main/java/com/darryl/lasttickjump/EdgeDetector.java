package com.darryl.lasttickjump;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EdgeDetector {

    /**
     * Returns true if the player is on the last tick where onGround will be true — i.e.,
     * the next tick's movement will carry them past the block edge.
     */
    public static boolean detect(LocalPlayer player, Level level) {
        if (!player.onGround()) return false;
        if (!player.isSprinting()) return false;
        if (player.input == null) return false;
        // Must be actively moving horizontally
        Vec3 vel = player.getDeltaMovement();
        if (vel.x * vel.x + vel.z * vel.z < 0.001) return false;

        double px = player.getX();
        double py = player.getY();
        double pz = player.getZ();

        // Project one tick forward
        double nx = px + vel.x;
        double nz = pz + vel.z;

        // If next position still has ground, no edge yet.
        if (hasGroundBelow(level, player, nx, py, nz)) return false;

        // If there's a step-up-height block ahead, vanilla auto-step handles it — don't jump.
        if (hasSolidAtFeet(level, nx, py, nz)) return false;

        return true;
    }

    /**
     * True if there is any collision shape in the thin slab just below (x, y, z)
     * within the player's 0.6-wide footprint.
     */
    private static boolean hasGroundBelow(Level level, LocalPlayer player, double x, double y, double z) {
        AABB scanBox = new AABB(x - 0.299, y - 0.05, z - 0.299, x + 0.299, y, z + 0.299);
        for (VoxelShape shape : level.getBlockCollisions(player, scanBox)) {
            if (!shape.isEmpty()) return true;
        }
        return false;
    }

    /**
     * True if the block at foot level is within step-up height (≤ 0.6 blocks above feet).
     * Full walls are not suppressed — the player can't walk through them anyway.
     */
    private static boolean hasSolidAtFeet(Level level, double x, double y, double z) {
        BlockPos pos = BlockPos.containing(x, y, z);
        BlockState state = level.getBlockState(pos);
        VoxelShape shape = state.getCollisionShape(level, pos, CollisionContext.empty());
        if (shape.isEmpty()) return false;
        double shapeTop = shape.bounds().maxY + pos.getY();
        return shapeTop > y + 0.001 && shapeTop <= y + 0.6;
    }
}
