package bigsir.debugutils.mixins;

import bigsir.debugutils.DebugUtils;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.core.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = RenderGlobal.class, remap = false)
public class RenderGlobalMixin {

	//Uses non-frozen partialTick when rendering the player
	@ModifyArgs(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/EntityRenderDispatcher;renderEntity(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/core/entity/Entity;F)V", ordinal = 1))
	public void test(Args args){
		if(args.get(1) instanceof EntityPlayer) args.set(2, DebugUtils.chooseTick(args.get(2)));
	}
}
