package turniplabs.examplemod.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.examplemod.ExampleMod;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {

	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;next()Z", shift = At.Shift.AFTER))
	public void keyPressCheck(CallbackInfo ci){
		if(ExampleMod.showDebugCubes != null && ExampleMod.debugCubes.isPressed()) ExampleMod.showDebugCubes.toggle();
	}
}
