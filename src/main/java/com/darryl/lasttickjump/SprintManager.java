package com.darryl.lasttickjump;

/**
 * Preserves sprint state across the jump tick.
 *
 * Problem: the sprint-cancel check in LocalPlayer.aiStep() runs BEFORE the jump check
 * in LivingEntity.aiStep(). If something cancels sprint between edge detection and
 * jumpFromGround(), the +0.2 horizontal boost is lost.
 *
 * Solution: snapshot sprint state at edge-detection time, then re-apply it just before
 * jumpFromGround() fires (in LivingEntityMixin).
 */
public class SprintManager {

    private static boolean wasSprintingAtEdge = false;

    public static void record(boolean isSprinting) {
        wasSprintingAtEdge = isSprinting;
    }

    /** Called from LivingEntityMixin just before jumpFromGround(). */
    public static boolean wasSprintingBeforeJump() {
        return wasSprintingAtEdge;
    }

    public static void reset() {
        wasSprintingAtEdge = false;
    }
}
