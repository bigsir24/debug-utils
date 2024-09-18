package bigsir.debugutils.commands;

import bigsir.debugutils.DebugUtils;

import bigsir.debugutils.interfaces.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.entity.player.EntityPlayer;

import java.lang.Math;

public class FreezeCommand extends Command {
    public FreezeCommand() {
        super("tick", new String[0]);
    }

    public int parseInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            throw new CommandError("Not an int: \"" + str + "\"");
        }
    }

	public float parseFloat(String str) {
		try {
			return Float.parseFloat(str);
		} catch (Exception e) {
			throw new CommandError("Not a float: \"" + str + "\"");
		}
	}

    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].charAt(0) == 's') {
            DebugUtils.frozen = !DebugUtils.frozen;
            DebugUtils.allowedTicks = 0;
        } else if (args[0].charAt(0) == 'a') {
            if (!DebugUtils.frozen) {
                sender.sendMessage("The ticks need to be stopped!");
                return true;
            }
			// The "+ 1" at the end may seem wrong, but renember that the command is being run mid-tick
            if (args.length == 2) {
				DebugUtils.allowedTicks = parseInteger(args[1]) + 1;
            } else {
                DebugUtils.allowedTicks = Math.max(0, DebugUtils.allowedTicks) + 2;
            }
        }else if(args[0].charAt(0) == 't'){
			float tps = parseFloat(args[1]);
			if(tps < 2 || tps > 200) {
				throw new CommandError("Value must be between 2 and 200");
			}else{
				((IMinecraft)Minecraft.getMinecraft(Minecraft.class)).debug_utils$getTimer().ticksPerSecond = tps;
			}
		}
        return true;
    }

    public boolean opRequired(String[] args) {
        return false;
    }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("/tick s");
        sender.sendMessage("/tick a [amount=1]");
        sender.sendMessage("/tick t <tps>");
    }
}
