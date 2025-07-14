package bigsir.debugutils.mixins;

import bigsir.debugutils.DebugUtils;
import bigsir.debugutils.interfaces.ITimer;
import net.minecraft.core.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Timer.class, remap = false)
public abstract class TimerMixin implements ITimer {

	@Shadow
	public float partialTicks;

	@Shadow
	public int frames;
	@Unique
	public float lastPartialTicks;

	@Unique
	private int skipFrames = 0;

	@Override
	public void debug_utils$skipFrames(int frames) {
		this.skipFrames = frames;
	}

	@Inject(method = "advanceTime", at = @At("TAIL"))
	public void freezePartialTicks(CallbackInfo ci){
		if(DebugUtils.frozen && DebugUtils.allowedTicks <= 0) {
			DebugUtils.bypassTicks = partialTicks;
			partialTicks = lastPartialTicks;
		}else{
			lastPartialTicks = partialTicks;
		}
	}

	@Inject(method = "advanceTime", at = @At(value = "TAIL"))
	public void skip(CallbackInfo ci){
		if (this.skipFrames > 0) {
			this.frames = skipFrames;
			this.skipFrames = 0;
		}
	}
}
