package bigsir.debugutils.mixins;

import net.minecraft.core.item.Item;
import net.minecraft.core.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.Commands;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.net.command.ClientCommandHandler;
import net.minecraft.core.net.command.ClientPlayerCommandSender;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class, remap = false)
public abstract class ItemStackMixin {
    // TODO: Use item on air as well
    @Inject(method = "useItem", at = @At(value = "HEAD"),  cancellable = true)
    void useItem(EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
        ItemStack is = (ItemStack) (Object) this;
        if (is.getItem() == Item.stick && is.hasCustomName()) {
            String name = is.getCustomName();
            if (name.charAt(0) != '/') return;
            // Run replacements
            name = name
                .replace("<x>", "" + blockX)
                .replace("<y>", "" + blockY)
                .replace("<z>", "" + blockZ);
            // Run command
            runCmd(name, entityplayer);
            cir.setReturnValue(true);
        }
    }

    void runCmd(String name, EntityPlayer player) {
        ClientPlayerCommandSender sender = new ClientPlayerCommandSender(Minecraft.getMinecraft(Minecraft.class), (EntityPlayerSP) player);
        ClientCommandHandler handler = Minecraft.getMinecraft(Minecraft.class).commandHandler;

        String[] args = name.substring(1).split(" ");
        String[] args1 = new String[args.length - 1];
        System.arraycopy(args, 1, args1, 0, args.length - 1);
        for (Command command : Commands.commands) {
            if (command.isName(args[0])) {
                try {
                    boolean success = command.execute(handler, sender, args1);
                    if (!success)
                        command.sendCommandSyntax(handler, sender);
                } catch (CommandError e) {
                    sender.sendMessage(TextFormatting.RED + e.getMessage());
                } catch (Throwable e) {
                    e.printStackTrace();
                    sender.sendMessage(TextFormatting.RED + "Error!");
                }
            }
        }
    }
}
