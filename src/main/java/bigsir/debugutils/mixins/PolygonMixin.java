package bigsir.debugutils.mixins;

import bigsir.debugutils.utils.ColorHelper;
import bigsir.debugutils.DebugUtils;
import bigsir.debugutils.interfaces.INameable;
import bigsir.debugutils.interfaces.IPolygon;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.Polygon;
import net.minecraft.client.render.Vertex;
import net.minecraft.client.render.tessellator.Tessellator;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Polygon.class, remap = false)
public abstract class PolygonMixin implements IPolygon, INameable {
	@Shadow
	public Vertex[] vertexPositions;
	@Shadow
	public int nVertices;
	@Unique
	int cubeFaceIndex;
	@Unique
	private String name;

	@Override
	public void debug_utils$setCubeFaceIndex(int cubeFaceIndex) {
		this.cubeFaceIndex = cubeFaceIndex;
	}

	@Override
	public void debug_utils$setName(String name) {
		this.name = name;
	}

	@Inject(method = "<init>([Lnet/minecraft/client/render/Vertex;)V", at = @At("TAIL"))
	public void init(Vertex[] vertices, CallbackInfo ci){
		this.cubeFaceIndex = -1;
	}

	@Inject(method = "draw", at = @At(value = "HEAD"), cancellable = true)
	public void drawFrame(Tessellator tessellator, float scale, CallbackInfo ci){
		if(DebugUtils.showDebugCubes.value) {
			float r = GL11.glGetFloat(GL11.GL_RED_SCALE);
			float g = GL11.glGetFloat(GL11.GL_GREEN_SCALE);
			float b = GL11.glGetFloat(GL11.GL_BLUE_SCALE);
			float a = GL11.glGetFloat(GL11.GL_ALPHA_SCALE);

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			if(!DebugUtils.showCubeNames.value) {
				GL11.glColor4f(1.0f, 1.0f, 1.0f, DebugUtils.lineOpacity.value / 100.0f);
			}else{
				GL11.glColor4f(ColorHelper.getRed(this.name), ColorHelper.getGreen(this.name), ColorHelper.getBlue(this.name), DebugUtils.lineOpacity.value / 100.0f);
			}
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glLineWidth(DebugUtils.lineWidth.value + 1);
			if(LightmapHelper.isLightmapEnabled()){
				LightmapHelper.setLightmapCoord(LightmapHelper.getLightmapCoord(15,15));
			}
			tessellator.startDrawing(GL11.GL_LINE_STRIP);
			tessellator.addVertex(vertexPositions[0].vector3D.xCoord * scale, vertexPositions[0].vector3D.yCoord * scale, vertexPositions[0].vector3D.zCoord * scale);
			tessellator.addVertex(vertexPositions[1].vector3D.xCoord * scale, vertexPositions[1].vector3D.yCoord * scale, vertexPositions[1].vector3D.zCoord * scale);
			tessellator.addVertex(vertexPositions[2].vector3D.xCoord * scale, vertexPositions[2].vector3D.yCoord * scale, vertexPositions[2].vector3D.zCoord * scale);
			tessellator.addVertex(vertexPositions[3].vector3D.xCoord * scale, vertexPositions[3].vector3D.yCoord * scale, vertexPositions[3].vector3D.zCoord * scale);
			tessellator.addVertex(vertexPositions[0].vector3D.xCoord * scale, vertexPositions[0].vector3D.yCoord * scale, vertexPositions[0].vector3D.zCoord * scale);

			tessellator.draw();
			if(cubeFaceIndex == 1 && this.nVertices != 0){
				GL11.glPointSize(DebugUtils.cornerPointSize.value + 1);
				tessellator.startDrawing(GL11.GL_POINTS);
				GL11.glColor4f(0,0,1.0f,1.0f);
				tessellator.addVertex(vertexPositions[0].vector3D.xCoord * scale, vertexPositions[0].vector3D.yCoord * scale, vertexPositions[0].vector3D.zCoord * scale);
				tessellator.draw();
			}

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);

			GL11.glColor4f(r,g,b,a);
			ci.cancel();
		}
	}
}
