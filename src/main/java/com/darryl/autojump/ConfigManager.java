package com.darryl.autojump;

public class ConfigManager {

    public static final ConfigManager INSTANCE = new ConfigManager();

    private boolean enabled = true;

    /**
     * Extra ticks of random delay added to the jump trigger (0 = always optimal tick).
     * Set to 1 for slight human-like variance, 2 for more. Values above 2 risk missing the edge.
     */
    private int varianceTicks = 0;

    public boolean isEnabled() {
        return enabled;
    }

    public void toggleEnabled() {
        enabled = !enabled;
        if (!enabled) JumpTrigger.cancel();
    }

    public int getVarianceTicks() {
        return varianceTicks;
    }

    public void setVarianceTicks(int v) {
        varianceTicks = Math.max(0, Math.min(2, v));
    }
}
