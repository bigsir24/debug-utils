package bigsir.debugutils.mixins;

import bigsir.debugutils.interfaces.IPolygon;
import net.minecraft.client.Minecraft;
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
public abstract class CubeMixin {
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
	@Unique boolean lastValue = false;

	@Inject(method = "render", at = @At("HEAD"))
	public void recompile(float scale, CallbackInfo ci){
		boolean showBox = Minecraft.getMinecraft(Minecraft.class).gameSettings.showCollisionBoxes.value;
		if(showBox != lastValue){
			lastValue = showBox;
			this.compiled = false;
		}
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", shift = At.Shift.BEFORE))
	public void rotationPointRender(float scale, CallbackInfo ci){
		if(Minecraft.getMinecraft(Minecraft.class).gameSettings.showCollisionBoxes.value){
			GL11.glPushMatrix();
			float r = GL11.glGetFloat(GL11.GL_RED_SCALE);
			float g = GL11.glGetFloat(GL11.GL_GREEN_SCALE);
			float b = GL11.glGetFloat(GL11.GL_BLUE_SCALE);
			float a = GL11.glGetFloat(GL11.GL_ALPHA_SCALE);

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPointSize(10.0f);
			Tessellator tessellator = Tessellator.instance;

			tessellator.startDrawing(GL11.GL_POINTS);
			GL11.glColor4f(1.0f,0,0,1.0f);
			tessellator.addVertex(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
			tessellator.draw();

			GL11.glColor4f(r,g,b,a);

			GL11.glPopMatrix();
		}
	}
/*
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/Cube;compileDisplayList(F)V", shift = At.Shift.BY, by = 2))
	public void cubeCornerRender(float scale, CallbackInfo ci){
		if(Minecraft.getMinecraft(Minecraft.class).gameSettings.showCollisionBoxes.value){
			GL11.glPushMatrix();
			float r = GL11.glGetFloat(GL11.GL_RED_SCALE);
			float g = GL11.glGetFloat(GL11.GL_GREEN_SCALE);
			float b = GL11.glGetFloat(GL11.GL_BLUE_SCALE);
			float a = GL11.glGetFloat(GL11.GL_ALPHA_SCALE);

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPointSize(7.0f);
			Tessellator tessellator = Tessellator.instance;


			tessellator.startDrawing(GL11.GL_POINTS);
			GL11.glColor4f(0,0,1.0f,1.0f);
			double x = this.faces[2].vertexPositions[2].vector3D.xCoord;
			double y = this.faces[2].vertexPositions[2].vector3D.yCoord;
			double z = this.faces[2].vertexPositions[2].vector3D.zCoord;
			tessellator.addVertex(x * scale, y * scale, z * scale);
			tessellator.draw();

			GL11.glColor4f(r,g,b,a);

			GL11.glPopMatrix();
		}
	}*/

	@Inject(method = "addBox(FFFIIIFZ)V", at = @At("TAIL"))
	public void setFaceIndex(float minX, float minY, float minZ, int sizeX, int sizeY, int sizeZ, float expandAmount, boolean flipBottomUV, CallbackInfo ci){
		for (int i = 0; i < this.faces.length; i++) {
			((IPolygon)faces[i]).debug_utils$setCubeFaceIndex(i);
		}
	}
}
