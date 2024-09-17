package bigsir.debugutils.commands;

import net.minecraft.core.world.World;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.entity.player.EntityPlayer;

public class KillAllCommand extends Command {
    public KillAllCommand() {
        super("killall", new String[0]);
    }

    public int parseInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            throw new CommandError("Not an int: \"" + str + "\"");
        }
    }

    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        World world = sender.getPlayer().world;
        for (Entity e : world.loadedEntityList) {
            if (!(e instanceof EntityPlayer)) e.remove();
        }
        return true;
    }

    public boolean opRequired(String[] args) {
        return false;
    }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("/killall");
    }
}
