package bigsir.debugutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeString;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.arguments.ArgumentTypeEntity;
import net.minecraft.core.net.command.helpers.EntitySelector;

import java.util.List;

public class CommandRename implements CommandManager.CommandRegistry {

	public static int renameCommand(CommandContext<CommandSource> context) throws CommandSyntaxException {
		List<? extends Entity> players = context.getArgument("player", EntitySelector.class).get(context.getSource());
		for (Entity player : players) {
			ItemStack heldItem = ((Player)player).getHeldItem();
			if (heldItem != null) {
				heldItem.setCustomName(context.getArgument("name", String.class));
			}
		}
		return 1;
	}

	@Override
	public void register(CommandDispatcher<CommandSource> commandDispatcher) {
		commandDispatcher.register(
			ArgumentBuilderLiteral
				.<CommandSource>literal("rename")
				.requires(CommandSource::hasAdmin)
				.then(ArgumentBuilderRequired.<CommandSource, EntitySelector>argument("player", ArgumentTypeEntity.usernames())
				.then(ArgumentBuilderRequired.<CommandSource, String>argument("name", ArgumentTypeString.greedyString())
					.executes(CommandRename::renameCommand)))
		);
	}
}
