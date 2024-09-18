package bigsir.debugutils.mixins;

import bigsir.debugutils.DebugUtils;
import net.minecraft.core.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Timer.class, remap = false)
public abstract class TimerMixin {

	@Shadow
	public float partialTicks;

	@Unique
	public float lastPartialTicks;

	@Inject(method = "advanceTime", at = @At("TAIL"))
	public void freezePartialTicks(CallbackInfo ci){
		if(DebugUtils.frozen && DebugUtils.allowedTicks <= 0) {
			DebugUtils.bypassTicks = partialTicks;
			partialTicks = lastPartialTicks;
		}else{
			lastPartialTicks = partialTicks;
		}
	}
}
