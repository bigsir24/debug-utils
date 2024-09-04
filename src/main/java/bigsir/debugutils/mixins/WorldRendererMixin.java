package bigsir.debugutils.mixins;

import bigsir.debugutils.DebugUtils;
import bigsir.debugutils.utils.ColorHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldRenderer.class, remap = false)
public abstract class WorldRendererMixin {

	@Shadow
	public Minecraft mc;

	@Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/debug/Debug;change(Ljava/lang/String;)V", ordinal = 9, shift = At.Shift.AFTER))
	public void renderModDebug(float partialTick, long updateRenderersUntil, CallbackInfo ci){
		if(DebugUtils.showDebugCubes.value && DebugUtils.showCubeNames.value){
			renderNames(partialTick);
		}
	}

	private void renderNames(float partialTick){
		for (Entity entity : mc.theWorld.getLoadedEntityList()) {
			if(entity.distanceToSqr(mc.thePlayer) > 32*32) continue;

			ICamera cam = this.mc.activeCamera;

			if(DebugUtils.cubeNameMap.containsKey(entity.getClass())){
				GL11.glPushMatrix();

				GL11.glTranslated(-cam.getX(partialTick), -cam.getY(partialTick), -cam.getZ(partialTick));
				GL11.glTranslated(MathHelper.floor_double(entity.x) + 0.5, entity.y + entity.bbHeight + 1, MathHelper.floor_double(entity.z) + 0.5);

				GL11.glRotated(180 - cam.getYRot(partialTick), 0,1,0);

				GL11.glScalef(0.04f, -0.04f, 0.04f);

				int offset = 0;
				int xOff = 0;
				int space = mc.fontRenderer.getStringWidth(" ");

				int i = 0;
				String[] strings = new String[DebugUtils.namesInRow.value + 1];
				for(String string : DebugUtils.cubeNameMap.get(entity.getClass())){
					strings[i] = string;
					if( (i+1) / (DebugUtils.namesInRow.value+1) == 1 ){
						String temp = "";
						for (int j = 0; j < strings.length; j++) {
							temp += (j == 0 ? "" : " ") + strings[j];
						}
						int length = mc.fontRenderer.getStringWidth(temp);
						for (int j = 0; j < strings.length; j++) {
							mc.fontRenderer.drawString(strings[j], -length/2 + xOff + (xOff == 0 ? 0 : space), -offset, ColorHelper.getColor(strings[j]).getRGB());
							xOff+= mc.fontRenderer.getStringWidth(strings[j]);
							strings[j] = null;
						}
						offset+=mc.fontRenderer.fontHeight;
						xOff = 0;
					}
					i++;
					if(i % (DebugUtils.namesInRow.value + 1) == 0) i = 0;
				}
				String temp = "";
				for (int j = 0; j < strings.length; j++) {
					if(strings[j] != null) temp += (j == 0 ? "" : " ") + strings[j];
				}
				int length = mc.fontRenderer.getStringWidth(temp);
				for (int j = 0; j < strings.length; j++) {
					if(strings[j] != null) {
						mc.fontRenderer.drawString(strings[j], -length/2 + xOff + (xOff == 0 ? 0 : space), -offset, ColorHelper.getColor(strings[j]).getRGB());
						xOff+= mc.fontRenderer.getStringWidth(strings[j]);
					}
				}

				GL11.glPopMatrix();
			}
		}
	}
}
