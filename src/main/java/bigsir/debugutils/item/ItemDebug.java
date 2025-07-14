package bigsir.debugutils.item;

import bigsir.debugutils.commands.CommandTriggerEvent;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.MobSpider;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class ItemDebug extends Item {
	public ItemDebug(NamespaceID namespaceID, int id) {
		super(namespaceID, id);
		this.setKey(namespaceID.value().split("/")[1].replaceAll("_", "."));
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onUseItemOnBlock(ItemStack itemstack, Player entityplayer, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced) {
		CommandTriggerEvent.triggerUpdateCommandImmediate(world, x, y, z, 1);

		return true;
	}

	@Override
	public int getDamageVsEntity(Entity entity, ItemStack itemStack) {
		return entity instanceof MobSpider ? 9999 : 1;
	}
}
