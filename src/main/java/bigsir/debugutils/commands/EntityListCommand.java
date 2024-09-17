package bigsir.debugutils.commands;

import net.minecraft.core.world.World;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.CommandHandler;

public class EntityListCommand extends Command {
    public EntityListCommand() {
        super("entitylist", new String[0]);
    }

    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        World world = sender.getPlayer().world;
        for (Entity e : world.loadedEntityList) {
            System.out.println(e);
        }
        return true;
    }

    public boolean opRequired(String[] args) {
        return false;
    }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("/entitylist");
    }
}
