package com.darryl.lasttickjump.mixin;

import com.darryl.lasttickjump.JumpTrigger;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {

    @Inject(method = "tick(ZF)V", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        if (JumpTrigger.consumeJump()) {
            this.jumping = true;
        }
    }
}
