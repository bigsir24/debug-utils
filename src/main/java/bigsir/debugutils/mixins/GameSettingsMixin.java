package bigsir.debugutils.mixins;

import bigsir.debugutils.DebugOptions;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GameSettings.class, remap = false)
public abstract class GameSettingsMixin {
	@Inject(method = "getDisplayString", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/lang/I18n;getInstance()Lnet/minecraft/core/lang/I18n;"), cancellable = true)
	public void changeDisplayString(Option<?> option, CallbackInfoReturnable<String> cir){
		if(option == DebugOptions.lineWidth || option == DebugOptions.cornerPointSize || option == DebugOptions.rotationPointSize){
			cir.setReturnValue((int)option.value + 1 + "");
		}else if(option == DebugOptions.lineOpacity){
			cir.setReturnValue(option.value + "%");
		}else if(option == DebugOptions.namesInRow){
			cir.setReturnValue((int)option.value + 1 + "");
		}else if(option == DebugOptions.colorMode){
			cir.setReturnValue((boolean)option.value ? "Random" : "HSB");
		}
	}
}
