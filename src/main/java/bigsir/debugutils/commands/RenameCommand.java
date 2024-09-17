package bigsir.debugutils.commands;

import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.CommandHandler;

public class RenameCommand extends Command {
    public RenameCommand() {
        super("rename", new String[0]);
    }

    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        if (args.length == 0) return false;
        StringBuilder name = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
            if (i > 0)
                name.append(' ');
            name.append(args[i]);
        }
        sender.getPlayer().getCurrentEquippedItem().setCustomName("" + name);
        return true;
    }

    public boolean opRequired(String[] args) {
        return false;
    }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("/rename <text..>");
    }
}
