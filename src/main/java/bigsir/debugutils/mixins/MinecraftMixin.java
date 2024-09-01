package bigsir.debugutils.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import bigsir.debugutils.DebugUtils;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {

	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;next()Z", shift = At.Shift.AFTER))
	public void keyPressCheck(CallbackInfo ci){
		if(DebugUtils.showDebugCubes != null && DebugUtils.debugCubes.isPressed()) DebugUtils.showDebugCubes.toggle();
	}
}
