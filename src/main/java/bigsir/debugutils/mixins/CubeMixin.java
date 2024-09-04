package bigsir.debugutils.mixins;

import bigsir.debugutils.utils.ColorHelper;
import bigsir.debugutils.DebugUtils;
import bigsir.debugutils.interfaces.INameable;
import bigsir.debugutils.interfaces.IPolygon;
import net.minecraft.client.render.Polygon;
import net.minecraft.client.render.Vertex;
import net.minecraft.client.render.model.Cube;
import net.minecraft.client.render.tessellator.Tessellator;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Cube.class, remap = false)
public abstract class CubeMixin implements INameable {
	@Shadow
	private Vertex[] corners;
	@Shadow
	private boolean compiled;
	@Shadow
	public float rotationPointX;
	@Shadow
	public float rotationPointY;
	@Shadow
	public float rotationPointZ;
	@Shadow
	private Polygon[] faces;
	@Unique
	private boolean lastValue;
	@Unique
	private String name;

	@Override
	public void debug_utils$setName(String name) {
		this.name = name;
		for (int i = 0; i < this.faces.length; i++) {
			((INameable)this.faces[i]).debug_utils$setName(name);
		}
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/Cube;compileDisplayList(F)V", shift = At.Shift.BY, by = 2))
	public void rotationPointRender(float scale, CallbackInfo ci){

		if(DebugUtils.showDebugCubes.value){
			GL11.glPushMatrix();
			float r = GL11.glGetFloat(GL11.GL_RED_SCALE);
			float g = GL11.glGetFloat(GL11.GL_GREEN_SCALE);
			float b = GL11.glGetFloat(GL11.GL_BLUE_SCALE);
			float a = GL11.glGetFloat(GL11.GL_ALPHA_SCALE);

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPointSize(DebugUtils.rotationPointSize.value + 1);
			Tessellator tessellator = Tessellator.instance;

			tessellator.startDrawing(GL11.GL_POINTS);
			GL11.glColor4f(1.0f,0,0,1.0f);
			tessellator.addVertex(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
			tessellator.draw();

			GL11.glColor4f(r,g,b,a);

			GL11.glPopMatrix();
		}
	}

	@Inject(method = "addBox(FFFIIIFZ)V", at = @At("TAIL"))
	public void setFaceIndex(float minX, float minY, float minZ, int sizeX, int sizeY, int sizeZ, float expandAmount, boolean flipBottomUV, CallbackInfo ci){
		for (int i = 0; i < this.faces.length; i++) {
			((IPolygon)faces[i]).debug_utils$setCubeFaceIndex(i);
		}
	}

	//Three injects for now might change to setting compiled to false in compileDisplayList later
	@Inject(method = "render", at = @At("HEAD"))
	public void recompileRender(float scale, CallbackInfo ci){
		boolean showBox = DebugUtils.showDebugCubes.value;
		if(showBox != lastValue){
			if(DebugUtils.showCubeNames.value && !showBox) ColorHelper.refreshColors();
			lastValue = showBox;
			this.compiled = false;
		}
	}
	@Inject(method = "renderWithRotation", at = @At("HEAD"))
	public void recompileRenderWithRotation(float scale, CallbackInfo ci){
		boolean showBox = DebugUtils.showDebugCubes.value;
		if(showBox != lastValue){
			if(DebugUtils.showCubeNames.value && !showBox) ColorHelper.refreshColors();
			lastValue = showBox;
			this.compiled = false;
		}
	}
	@Inject(method = "postRender", at = @At("HEAD"))
	public void recompilePostRender(float scale, CallbackInfo ci){
		boolean showBox = DebugUtils.showDebugCubes.value;
		if(showBox != lastValue){
			if(DebugUtils.showCubeNames.value && !showBox) ColorHelper.refreshColors();
			lastValue = showBox;
			this.compiled = false;
		}
	}
}
