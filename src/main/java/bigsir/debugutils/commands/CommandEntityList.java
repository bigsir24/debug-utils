package bigsir.debugutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.exceptions.CommandExceptions;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

public class CommandEntityList implements CommandManager.CommandRegistry {

	@Unique
	private static int listCommand(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		Player sender = source.getSender();
		if (sender == null || sender.world == null) {
			throw CommandExceptions.notInWorld().create();
		} else {
			List<Entity> entities = sender.world.loadedEntityList;

			for (Entity entity : entities) {
				System.out.println(entity);
			}
			return 1;
		}
	}

	@Override
	public void register(CommandDispatcher<CommandSource> commandDispatcher) {
		commandDispatcher.register(
			ArgumentBuilderLiteral
				.<CommandSource>literal("entitylist")
				.requires(CommandSource::hasAdmin)
				.executes(CommandEntityList::listCommand)
		);
	}
}
