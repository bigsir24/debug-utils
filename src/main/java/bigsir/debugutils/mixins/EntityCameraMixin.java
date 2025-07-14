package bigsir.debugutils.mixins;

import bigsir.debugutils.DebugUtils;
import net.minecraft.client.render.camera.EntityCamera;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.core.util.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = EntityCamera.class, remap = false)
public abstract class EntityCameraMixin implements ICamera {

	//If the game is frozen these methods will have their partialTick arguments changed to a non-frozen partialTick value
	@Override
	public Vec3 getPosition(float partialTick) {
		return ICamera.super.getPosition(DebugUtils.chooseTick(partialTick));
	}

	@Override
	public Vec3 getViewVector(float partialTick) {
		return ICamera.super.getViewVector(DebugUtils.chooseTick(partialTick));
	}

	@Override
	public Vec3 getRotationVector(float partialTick) {
		return ICamera.super.getRotationVector(DebugUtils.chooseTick(partialTick));
	}

	@ModifyVariable(method = {"getX", "getY", "getZ", "getXRot", "getYRot"}, at = @At(value = "LOAD"), argsOnly = true)
	private float partialTickOverride(float partialTick){
		return DebugUtils.chooseTick(partialTick);
	}
}
