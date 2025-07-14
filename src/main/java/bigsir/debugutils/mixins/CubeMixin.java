package bigsir.debugutils.mixins;

import bigsir.debugutils.DebugOptions;
import bigsir.debugutils.interfaces.INameable;
import bigsir.debugutils.interfaces.IPolygon;
import bigsir.debugutils.utils.ColorHelper;
import net.minecraft.client.render.Polygon;
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
	private boolean compiled;
	@Shadow
	public float x;
	@Shadow
	public float y;
	@Shadow
	public float z;
	@Shadow
	private Polygon[] polygons;
	@Unique
	private boolean lastValue;
	@Unique
	private String name;

	@Override
	public void debug_utils$setName(String name) {
		this.name = name;
		for (int i = 0; i < this.polygons.length; i++) {
			((INameable)this.polygons[i]).debug_utils$setName(name);
		}
	}

	@Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/model/Cube;xRot:F", ordinal = 0, shift = At.Shift.BEFORE))
	public void rotationPointRender(float scale, CallbackInfo ci){

		if (DebugOptions.showDebugCubes.value) {
			GL11.glPushMatrix();

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPointSize(DebugOptions.rotationPointSize.value + 1);
			Tessellator tessellator = Tessellator.instance;

			tessellator.startDrawing(GL11.GL_POINTS);
			if (DebugOptions.colorRotationPoint.value) {
				float rr = ColorHelper.getRed(this.name);
				float gg = ColorHelper.getGreen(this.name);
				float bb = ColorHelper.getBlue(this.name);
				tessellator.setColorOpaque_F(rr, gg, bb);
			} else {
				tessellator.setColorOpaque_F(1, 0, 0);
			}

			tessellator.addVertex(this.x * scale, this.y * scale, this.z * scale);
			tessellator.draw();

			GL11.glPopMatrix();
		}
	}

	@Inject(method = "addBox(FFFIIIFZ)V", at = @At("TAIL"))
	public void setFaceIndex(float minX, float minY, float minZ, int sizeX, int sizeY, int sizeZ, float expandAmount, boolean flipBottomUV, CallbackInfo ci){
		for (int i = 0; i < this.polygons.length; i++) {
			((IPolygon)polygons[i]).debug_utils$setCubeFaceIndex(i);
		}
	}

	//Three injects for now might change to setting compiled to false in compileDisplayList later
	@Inject(method = "render", at = @At("HEAD"))
	public void recompileRender(float scale, CallbackInfo ci){
		boolean showBox = DebugOptions.showDebugCubes.value;
		if(showBox != lastValue){
			if(DebugOptions.showCubeNames.value && !showBox) ColorHelper.refreshColors();
			lastValue = showBox;
			this.compiled = false;
		}
	}
	@Inject(method = "renderWithRotation", at = @At("HEAD"))
	public void recompileRenderWithRotation(float scale, CallbackInfo ci){
		boolean showBox = DebugOptions.showDebugCubes.value;
		if(showBox != lastValue){
			if(DebugOptions.showCubeNames.value && !showBox) ColorHelper.refreshColors();
			lastValue = showBox;
			this.compiled = false;
		}
	}
	@Inject(method = "render", at = @At("HEAD"))
	public void recompilePostRender(float scale, CallbackInfo ci){
		boolean showBox = DebugOptions.showDebugCubes.value;
		if(showBox != lastValue){
			if(DebugOptions.showCubeNames.value && !showBox) ColorHelper.refreshColors();
			lastValue = showBox;
			this.compiled = false;
		}
	}
}
