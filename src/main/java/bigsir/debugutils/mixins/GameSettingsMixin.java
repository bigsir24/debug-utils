package bigsir.debugutils.mixins;

import bigsir.debugutils.DebugUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.option.BooleanOption;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.Option;
import net.minecraft.client.option.RangeOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(value = GameSettings.class, remap = false)
public abstract class GameSettingsMixin {

	@Unique
	public BooleanOption showDebugCubes;
	@Unique
	public RangeOption lineWidth;
	@Unique
	public RangeOption cornerPointSize;
	@Unique
	public RangeOption rotationPointSize;
	@Unique
	public RangeOption lineOpacity;
	@Unique
	public BooleanOption disableShadows;
	@Inject(method = "getDisplayString", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/lang/I18n;getInstance()Lnet/minecraft/core/lang/I18n;"), cancellable = true)
	public void changeDisplayString(Option<?> option, CallbackInfoReturnable<String> cir){
		if(option == DebugUtils.lineWidth || option == DebugUtils.cornerPointSize || option == DebugUtils.rotationPointSize){
			cir.setReturnValue((int)option.value + 1 + "");
		}else if(option == DebugUtils.lineOpacity){
			cir.setReturnValue(option.value + "%");
		}
	}

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/b100/utils/ReflectUtils;getAllObjects(Ljava/lang/Class;Ljava/lang/Class;Ljava/lang/Object;)[Ljava/lang/Object;"))
	public void addOptions(Minecraft minecraft, File file, CallbackInfo ci){
		DebugUtils.optionsInit((GameSettings) (Object)this);

		this.showDebugCubes = DebugUtils.showDebugCubes;
		this.lineWidth = DebugUtils.lineWidth;
		this.cornerPointSize = DebugUtils.cornerPointSize;
		this.rotationPointSize = DebugUtils.rotationPointSize;
		this.lineOpacity = DebugUtils.lineOpacity;
		this.disableShadows = DebugUtils.disableShadows;
	}
}
