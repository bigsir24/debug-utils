package bigsir.debugutils.commands;

import net.minecraft.core.world.World;
import net.minecraft.core.block.Block;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.CommandHandler;

public class RandTickCommand extends Command {
    public RandTickCommand() {
        super("rtick", new String[0]);
    }

    public int parseInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            throw new CommandError("Not an int: \"" + str + "\"");
        }
    }

    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        int x = parseInteger(args[0]);
        int y = parseInteger(args[1]);
        int z = parseInteger(args[2]);
        World world = sender.getPlayer().world;
        if (args.length != 4) {
            Block b = world.getBlock(x, y, z);
            if (b == null) return true;
            b.updateTick(world, x, y, z, world.rand);
        } else {
            int delay = parseInteger(args[3]);
            if (delay == -1) {
                Block b = world.getBlock(x, y, z);
                if (b == null) return true;
                b.randomDisplayTick(world, x, y, z, world.rand);
            } else {
                world.scheduleBlockUpdate(x, y, z, world.getBlockId(x, y, z), delay);
            }
        }
        return true;
    }

    public boolean opRequired(String[] args) {
        return false;
    }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("/tick <x> <y> <z> [delay, -1 for display mode]");
    }
}
