package bigsir.debugutils.mixins;

import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class, remap = false)
public abstract class ItemStackMixin {
    // TODO: Use item on air as well
    @Inject(method = "useItem", at = @At(value = "HEAD"), cancellable = true)
    void useItem(Player player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
		ItemStack thisStack = (ItemStack) (Object) this;
		if(thisStack.hasCustomName()) {
			String name = thisStack.getCustomName()
				.replaceFirst("<x>", "" + blockX)
				.replaceFirst("<y>", "" + blockY)
				.replaceFirst("<z>", "" + blockZ);
			if(name.startsWith("/")) {
				((PlayerLocal)player).sendChatMessage(name);
				cir.setReturnValue(true);
			}
		}
    }

	@Inject(method = "useItemRightClick", at = @At("HEAD"), cancellable = true)
	public void useOnAir(World world, Player player, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack thisStack = (ItemStack) (Object) this;
		if(thisStack.hasCustomName()) {
			String name = thisStack.getCustomName();
			boolean targeted = name.contains("<x>") || name.contains("<y>") || name.contains("<z>");
			if(name.startsWith("/") && !targeted) {
				((PlayerLocal)player).sendChatMessage(name);
				cir.setReturnValue(thisStack);
			}
		}
	}
}
