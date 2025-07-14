package bigsir.debugutils.commands;

import bigsir.debugutils.DebugUtils;
import bigsir.debugutils.interfaces.IMinecraft;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeInteger;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.Minecraft;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;

public class CommandTick implements CommandManager.CommandRegistry {

	public static int stopCommand(CommandContext<CommandSource> context) {
		DebugUtils.frozen = !DebugUtils.frozen;
		DebugUtils.allowedTicks = 0;
		return 1;
	}

	public static int advanceCommand(CommandContext<CommandSource> context) {
		if (!DebugUtils.frozen) {
			return 1;
		}
		DebugUtils.allowedTicks = Math.max(0, DebugUtils.allowedTicks) + 2;
		return 1;
	}

	public static int advanceByCommand(CommandContext<CommandSource> context) {
		if (!DebugUtils.frozen) {
			return 1;
		}
		int amount = context.getArgument("amount", Integer.class);
		// The "+ 1" at the end may seem wrong, but remember that the command is being run mid-tick
		DebugUtils.allowedTicks = amount + 1;
		return 1;
	}

	public static int tpsCommand(CommandContext<CommandSource> context) {
		((IMinecraft) Minecraft.getMinecraft()).debug_utils$getTimer().ticksPerSecond = context.getArgument("amount", Integer.class);
		return 1;
	}

	public void register(CommandDispatcher<CommandSource> commandDispatcher) {
		commandDispatcher.register(
			ArgumentBuilderLiteral
				.<CommandSource>literal("tick")
				.requires(CommandSource::hasAdmin)
				.then(ArgumentBuilderLiteral.<CommandSource>literal("stop")
					.executes(CommandTick::stopCommand))
				.then(ArgumentBuilderLiteral.<CommandSource>literal("advance")
					.executes(CommandTick::advanceCommand)
					.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("amount", ArgumentTypeInteger.integer(1))
						.executes(CommandTick::advanceByCommand)))
				.then(ArgumentBuilderLiteral.<CommandSource>literal("tps")
					.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("amount", ArgumentTypeInteger.integer(2))
						.executes(CommandTick::tpsCommand)))
		);
	}
}
