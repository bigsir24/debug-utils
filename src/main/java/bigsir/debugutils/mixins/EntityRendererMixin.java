package bigsir.debugutils.mixins;

import bigsir.debugutils.DebugUtils;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class, remap = false)
public abstract class EntityRendererMixin<T extends Entity> {

	@Inject(method = "renderShadow", at = @At("HEAD"), cancellable = true)
	public void disableShadows(Tessellator tessellator, T entity, double posX, double posY, double posZ, float opacity, float partialTick, CallbackInfo ci){
		if(DebugUtils.disableShadows.value) ci.cancel();
	}
}
