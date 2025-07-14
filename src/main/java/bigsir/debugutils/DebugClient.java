package bigsir.debugutils;

import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.util.ClientStartEntrypoint;

public class DebugClient implements ClientStartEntrypoint {
	@Override
	public void beforeClientStart() {}

	@Override
	public void afterClientStart() {
		DebugOptions.initOptionsPages();

		//if (DebugUtils.createItems) assignModels();
	}

	protected static void assignModels() {
		ItemModelDispatcher.getInstance().addDispatch(DebugUtils.TOOL_DEBUG, getModel(DebugUtils.TOOL_DEBUG));
	}

	protected static ItemModelStandard getModel(Item item) {
		ItemModelStandard model = new ItemModelStandard(item, null);
		model.icon = TextureRegistry.getTexture(item.namespaceID);
		return model;
	}
}
