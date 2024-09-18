package bigsir.debugutils.mixins;

import bigsir.debugutils.DebugUtils;
import net.minecraft.client.render.camera.EntityCamera;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.core.util.phys.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = EntityCamera.class, remap = false)
public abstract class EntityCameraMixin implements ICamera {

	//If the game is frozen these methods will have their partialTick arguments changed to a non-frozen partialTick value
	@Override
	public Vec3d getPosition(float partialTick) {
		return ICamera.super.getPosition(DebugUtils.chooseTick(partialTick));
	}

	@Override
	public Vec3d getViewVector(float partialTick) {
		return ICamera.super.getViewVector(DebugUtils.chooseTick(partialTick));
	}

	@Override
	public Vec3d getRotationVector(float partialTick) {
		return ICamera.super.getRotationVector(DebugUtils.chooseTick(partialTick));
	}

	@ModifyVariable(method = "getX", at = @At(value = "LOAD"), argsOnly = true)
	private float tickX(float partialTick){
		return DebugUtils.chooseTick(partialTick);
	}

	@ModifyVariable(method = "getY", at = @At(value = "LOAD"), argsOnly = true)
	private float tickY(float partialTick){
		return DebugUtils.chooseTick(partialTick);
	}

	@ModifyVariable(method = "getZ", at = @At(value = "LOAD"), argsOnly = true)
	private float tickZ(float partialTick){
		return DebugUtils.chooseTick(partialTick);
	}

	@ModifyVariable(method = "getXRot", at = @At(value = "LOAD"), argsOnly = true)
	private float tickXRot(float partialTick){
		return DebugUtils.chooseTick(partialTick);
	}

	@ModifyVariable(method = "getYRot", at = @At(value = "LOAD"), argsOnly = true)
	private float tickYRot(float partialTick){
		return DebugUtils.chooseTick(partialTick);
	}
}
