package bigsir.debugutils.mixins;

import bigsir.debugutils.DebugOptions;
import bigsir.debugutils.interfaces.INameable;
import bigsir.debugutils.interfaces.IPolygon;
import bigsir.debugutils.utils.ColorHelper;
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
	public Vertex[] vertices;
	@Shadow
	public int vertexCount;
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

	@Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
	public void drawFrame(Tessellator tessellator, float scale, CallbackInfo ci){
		if(DebugOptions.showDebugCubes.value) {
			float r = GL11.glGetFloat(GL11.GL_RED_SCALE);
			float g = GL11.glGetFloat(GL11.GL_GREEN_SCALE);
			float b = GL11.glGetFloat(GL11.GL_BLUE_SCALE);
			float a = GL11.glGetFloat(GL11.GL_ALPHA_SCALE);

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			if(!DebugOptions.showCubeNames.value) {
				GL11.glColor4f(1.0f, 1.0f, 1.0f, DebugOptions.lineOpacity.value / 100.0f);
			}else{
				GL11.glColor4f(ColorHelper.getRed(this.name), ColorHelper.getGreen(this.name), ColorHelper.getBlue(this.name), DebugOptions.lineOpacity.value / 100.0f);
			}
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glLineWidth(DebugOptions.lineWidth.value + 1);
			if(LightmapHelper.isLightmapEnabled()){
				LightmapHelper.setLightmapCoord(LightmapHelper.getLightmapCoord(15,15));
			}
			tessellator.startDrawing(GL11.GL_LINE_STRIP);
			tessellator.addVertex(vertices[0].pos.x * scale, vertices[0].pos.y * scale, vertices[0].pos.z * scale);
			tessellator.addVertex(vertices[1].pos.x * scale, vertices[1].pos.y * scale, vertices[1].pos.z * scale);
			tessellator.addVertex(vertices[2].pos.x * scale, vertices[2].pos.y * scale, vertices[2].pos.z * scale);
			tessellator.addVertex(vertices[3].pos.x * scale, vertices[3].pos.y * scale, vertices[3].pos.z * scale);
			tessellator.addVertex(vertices[0].pos.x * scale, vertices[0].pos.y * scale, vertices[0].pos.z * scale);

			tessellator.draw();
			if(cubeFaceIndex == 1 && this.vertexCount != 0){
				GL11.glPointSize(DebugOptions.cornerPointSize.value + 1);
				tessellator.startDrawing(GL11.GL_POINTS);
				GL11.glColor4f(0,0,1.0f,1.0f);
				tessellator.addVertex(vertices[0].pos.x * scale, vertices[0].pos.y * scale, vertices[0].pos.z * scale);
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
