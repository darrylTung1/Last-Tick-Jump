package com.darryl.autojump;

import java.util.Random;

public class JumpTrigger {

    private static int ticksRemaining = -1;
    private static final Random RANDOM = new Random();

    /**
     * Schedule a jump. If maxVariance > 0, adds a random 0..maxVariance tick delay
     * to avoid perfectly consistent timing that statistical anti-cheats flag.
     */
    public static void set(int maxVariance) {
        if (ticksRemaining >= 0) return; // already scheduled
        ticksRemaining = maxVariance > 0 ? RANDOM.nextInt(maxVariance + 1) : 0;
    }

    /**
     * Called each tick from KeyboardInputMixin. Returns true exactly once — on the tick the
     * jump should fire — then resets the state.
     */
    public static boolean consumeJump() {
        if (ticksRemaining < 0) return false;
        if (ticksRemaining > 0) {
            ticksRemaining--;
            return false;
        }
        // ticksRemaining == 0: fire
        ticksRemaining = -1;
        return true;
    }

    public static void cancel() {
        ticksRemaining = -1;
    }

    public static boolean isPending() {
        return ticksRemaining >= 0;
    }
}
