package bigsir.debugutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeInteger;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.arguments.ArgumentTypeBlock;
import net.minecraft.core.net.command.arguments.ArgumentTypeIntegerCoordinates;
import net.minecraft.core.net.command.helpers.BlockInput;
import net.minecraft.core.net.command.helpers.IntegerCoordinates;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class CommandTriggerEvent implements CommandManager.CommandRegistry {

	private static int triggerEventCommand(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		IntegerCoordinates pos = context.getArgument("position", IntegerCoordinates.class);
		int data = context.getArgument("data", Integer.class);
		int x = pos.getX(source);
		int y = pos.getY(source, true);
		int z = pos.getZ(source);

		World world = source.getWorld();
		if(world != null) {
			triggerEventCommand(world, x, y, z, data);
		}
		return 1;
	}

	public static void triggerEventCommand(@NotNull World world, int x, int y, int z, int data) {
		Block<?> block = world.getBlock(x, y, z);
		if (block != null) block.triggerEvent(world, x, y, z, data, world.getBlockMetadata(x, y, z));
	}

	private static int triggerUpdateCommand(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		IntegerCoordinates pos = context.getArgument("position", IntegerCoordinates.class);
		int x = pos.getX(source);
		int y = pos.getY(source, true);
		int z = pos.getZ(source);

		World world = source.getWorld();
		if(world != null) {
			world.scheduleBlockUpdate(x, y, z, world.getBlockId(x, y, z), 0);
		}
		return 1;
	}

	private static int triggerUpdateCommandImmediate(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		IntegerCoordinates pos = context.getArgument("position", IntegerCoordinates.class);
		int iterations = context.getArgument("iterations", Integer.class);
		int x = pos.getX(source);
		int y = pos.getY(source, true);
		int z = pos.getZ(source);

		World world = source.getWorld();
		if(world != null) {
			triggerUpdateCommandImmediate(world, x, y, z, iterations);
		}
		return 1;
	}

	public static void triggerUpdateCommandImmediate(@NotNull World world, int x, int y, int z, int count) {
		Block<?> block = world.getBlock(x, y, z);
		if (block == null) return;

		TileEntity tileEntity = world.getTileEntity(x, y, z);

		for (int i = 0; i < count; i++) {
			block.updateTick(world, x, y, z, world.rand);
			if (tileEntity != null) tileEntity.tick();
		}
	}

	private static int triggerDisplayCommand(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		IntegerCoordinates pos = context.getArgument("position", IntegerCoordinates.class);
		int x = pos.getX(source);
		int y = pos.getY(source, true);
		int z = pos.getZ(source);

		World world = source.getWorld();
		if(world != null) {
			triggerDisplayCommand(world, x, y, z, 1);
		}
		return 1;
	}

	private static int triggerDisplayCommandMultiple(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		IntegerCoordinates pos = context.getArgument("position", IntegerCoordinates.class);
		int iterations = context.getArgument("iterations", Integer.class);
		int x = pos.getX(source);
		int y = pos.getY(source, true);
		int z = pos.getZ(source);

		World world = source.getWorld();
		if(world != null) {
			triggerDisplayCommand(world, x, y, z, iterations);
		}
		return 1;
	}

	private static void triggerDisplayCommand(@NotNull World world, int x, int y, int z, int iterations) {
		Block<?> block = world.getBlock(x, y, z);
		if (block == null) return;

		for (int i = 0; i < iterations; i++) {
			block.animationTick(world, x, y, z, world.rand);
		}
	}

	private static int triggerNeighborCommand(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		IntegerCoordinates pos = context.getArgument("position", IntegerCoordinates.class);
		BlockInput block = context.getArgument("block", BlockInput.class);
		int x = pos.getX(source);
		int y = pos.getY(source, true);
		int z = pos.getZ(source);

		World world = source.getWorld();
		Block<?> b;
		if(world != null && (b = world.getBlock(x, y, z)) != null) {
			b.onNeighborBlockChange(world, x, y, z, block.getBlockId());
		}
		return 1;
	}

	private static int triggerNeighborCommandNoBlock(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		IntegerCoordinates pos = context.getArgument("position", IntegerCoordinates.class);
		int x = pos.getX(source);
		int y = pos.getY(source, true);
		int z = pos.getZ(source);

		World world = source.getWorld();
		Block<?> b;
		if(world != null && (b = world.getBlock(x, y, z)) != null) {
			b.onNeighborBlockChange(world, x, y, z, 0);
		}
		return 1;
	}

	@Override
	public void register(CommandDispatcher<CommandSource> commandDispatcher) {
		commandDispatcher.register(
			ArgumentBuilderLiteral
				.<CommandSource>literal("trigger")
				.requires(CommandSource::hasAdmin)
				.then(ArgumentBuilderLiteral.<CommandSource>literal("event")
					.then(ArgumentBuilderRequired.<CommandSource, IntegerCoordinates>argument("position", ArgumentTypeIntegerCoordinates.intCoordinates())
						.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("data", ArgumentTypeInteger.integer(0))
							.executes(CommandTriggerEvent::triggerEventCommand))))
				.then(ArgumentBuilderLiteral.<CommandSource>literal("update")
					.then(ArgumentBuilderRequired.<CommandSource, IntegerCoordinates>argument("position", ArgumentTypeIntegerCoordinates.intCoordinates())
						//.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("id", ArgumentTypeInteger.integer(0))
							.executes(CommandTriggerEvent::triggerUpdateCommand)
							.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("iterations", ArgumentTypeInteger.integer(1, Short.MAX_VALUE))
								.executes(CommandTriggerEvent::triggerUpdateCommandImmediate)))/*)*/)
				.then(ArgumentBuilderLiteral.<CommandSource>literal("display")
					.then(ArgumentBuilderRequired.<CommandSource, IntegerCoordinates>argument("position", ArgumentTypeIntegerCoordinates.intCoordinates())
						.executes(CommandTriggerEvent::triggerDisplayCommand)
						.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("iterations", ArgumentTypeInteger.integer(1, Short.MAX_VALUE))
							.executes(CommandTriggerEvent::triggerDisplayCommandMultiple))))
				.then(ArgumentBuilderLiteral.<CommandSource>literal("neighbor")
					.then(ArgumentBuilderRequired.<CommandSource, IntegerCoordinates>argument("position", ArgumentTypeIntegerCoordinates.intCoordinates())
						.executes(CommandTriggerEvent::triggerNeighborCommandNoBlock)
						.then(ArgumentBuilderRequired.<CommandSource, BlockInput>argument("block", ArgumentTypeBlock.block())
							.executes(CommandTriggerEvent::triggerNeighborCommand))))
		);
	}
}
