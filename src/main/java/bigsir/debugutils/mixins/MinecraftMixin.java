package bigsir.debugutils.mixins;

import bigsir.debugutils.DebugUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.InputDevice;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Minecraft.class, remap = false)
public abstract class MinecraftMixin {
    @Inject(method = "checkBoundInputs", at = @At(value = "HEAD"), cancellable = true)
    public void keyBindingCheck(InputDevice currentInputDevice, CallbackInfoReturnable<Boolean> cir){
        if(DebugUtils.debugCubes.isPressEvent(currentInputDevice)) {
            DebugUtils.showDebugCubes.toggle();
            cir.setReturnValue(true);
        }
    }

    private static int ticker = 0;
    private boolean isGameReallyPaused(boolean old, boolean last) {
        boolean ret = old || (DebugUtils.frozen && DebugUtils.allowedTicks <= 0);
        if (last && DebugUtils.allowedTicks > 0) {
            DebugUtils.allowedTicks--;
        }
        return ret;
    }

    @Inject(method = "runTick", at = @At(value = "HEAD"))
    private void resetTicker(CallbackInfo ci) {
        ticker = 0;
    }

    @WrapOperation(method = "runTick", at = @At(value = "FIELD", target = "isGamePaused"), remap = false)
    private boolean isGamePaused_i0(Minecraft mc, Operation<Boolean> og) {
        if (!DebugUtils.frozen) return og.call(mc);
        ticker++;
        // 1st and 2nd (and maybe 3rd) are fine, the rest need to be changed
        if (ticker <= 3) return og.call(mc);
        // On the 5th, tick the player
        if (ticker == 5) mc.thePlayer.tick();
        return isGameReallyPaused(og.call(mc), ticker == 8);
    }
}
