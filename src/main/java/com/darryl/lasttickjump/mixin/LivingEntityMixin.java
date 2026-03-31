package com.darryl.lasttickjump.mixin;

import com.darryl.lasttickjump.JumpTrigger;
import com.darryl.lasttickjump.SprintManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow private int noJumpDelay;

    // Clear the jump cooldown so a recent manual jump can't suppress the auto-jump.
    @Inject(method = "aiStep()V", at = @At("HEAD"))
    private void clearJumpCooldown(CallbackInfo ci) {
        if (!((Object) this instanceof LocalPlayer)) return;
        if (JumpTrigger.isPending()) {
            this.noJumpDelay = 0;
        }
    }

    @Inject(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;jumpFromGround()V"
        )
    )
    private void beforeJumpFromGround(CallbackInfo ci) {
        if (!((Object) this instanceof LocalPlayer)) return;
        LocalPlayer player = (LocalPlayer)(Object)this;
        if (SprintManager.wasSprintingBeforeJump()) {
            player.setSprinting(true);
        }
        SprintManager.reset();
    }
}
