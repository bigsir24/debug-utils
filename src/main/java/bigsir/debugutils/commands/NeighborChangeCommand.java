package bigsir.debugutils.commands;

import net.minecraft.core.world.World;
import net.minecraft.core.block.Block;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.CommandHandler;

public class NeighborChangeCommand extends Command {
    public NeighborChangeCommand() {
        super("neighborchange", new String[0]);
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
        int id = parseInteger(args[3]);
        World world = sender.getPlayer().world;
        Block b = world.getBlock(x, y, z);
        if (b == null) return true;
        b.onNeighborBlockChange(world, x, y, z, id);
        return true;
    }

    public boolean opRequired(String[] args) {
        return false;
    }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("/neighborchange <x> <y> <z> <id>");
    }
}
