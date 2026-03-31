package com.darryl.lasttickjump;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class LastTickJumpMod implements ClientModInitializer {

    public static final String MOD_ID = "lasttickjump";
    private static KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.lasttickjump.toggle",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_J,
            "category.lasttickjump"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                ConfigManager.INSTANCE.toggleEnabled();
            }

            if (client.player == null || client.level == null) return;
            if (!ConfigManager.INSTANCE.isEnabled()) return;
            // Don't schedule a new jump if one is already pending
            if (JumpTrigger.isPending()) return;

            if (EdgeDetector.detect(client.player, client.level)) {
                SprintManager.record(client.player.isSprinting());
                JumpTrigger.set(ConfigManager.INSTANCE.getVarianceTicks());
            }
        });
    }
}
