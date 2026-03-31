package com.darryl.autojump;

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
     *
     * Called at START_CLIENT_TICK, before entity movement processing.
     * At this point, player position/velocity reflect the state after last tick's move().
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

        // Project one tick forward. The current deltaMovement IS the displacement that will
        // be applied this tick (before friction/accel on this tick), so it's the right delta.
        double nx = px + vel.x;
        double nz = pz + vel.z;

        // If next position still has ground, no edge yet.
        if (hasGroundBelow(level, player, nx, py, nz)) return false;

        // If there's a solid block AT foot level ahead, the player steps up — don't jump.
        if (hasSolidAtFeet(level, nx, py, nz)) return false;

        return true;
    }

    /**
     * True if there is any collision shape in the thin slab just below (x, y, z)
     * within the player's 0.6-wide footprint.
     *
     * Uses a 0.299 inset instead of 0.3 to avoid re-sampling the current edge block
     * when the player is exactly at the corner.
     */
    private static boolean hasGroundBelow(Level level, LocalPlayer player, double x, double y, double z) {
        AABB scanBox = new AABB(x - 0.299, y - 0.05, z - 0.299, x + 0.299, y, z + 0.299);
        for (VoxelShape shape : level.getBlockCollisions(player, scanBox)) {
            if (!shape.isEmpty()) return true;
        }
        return false;
    }

    /**
     * True if the block at foot level protrudes above the player's current foot position.
     * A shape that tops out at or below the player's feet (e.g. a same-level trapdoor)
     * is not a wall — the player would land on it, not be blocked by it.
     */
    private static boolean hasSolidAtFeet(Level level, double x, double y, double z) {
        BlockPos pos = BlockPos.containing(x, y, z);
        BlockState state = level.getBlockState(pos);
        VoxelShape shape = state.getCollisionShape(level, pos, CollisionContext.empty());
        if (shape.isEmpty()) return false;
        double shapeTop = shape.bounds().maxY + pos.getY();
        // Only suppress for step-up-height blocks (vanilla auto-step handles those).
        // Full walls (> y + 0.6) should not suppress — the player can't walk through them
        // anyway, and there may be a platform above worth jumping to.
        return shapeTop > y + 0.001 && shapeTop <= y + 0.6;
    }
}
