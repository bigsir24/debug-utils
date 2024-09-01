package bigsir.debugutils;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.input.InputDevice;
import net.minecraft.client.option.BooleanOption;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.core.item.Item;
import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class DebugUtils implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint, ClientStartEntrypoint {
    public static final String MOD_ID = "debugutils";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        LOGGER.info("Debug Utils initialized.");
    }

	public static OptionsPage optionsPage;
	public static BooleanOption showDebugCubes;
	public static final KeyBinding debugCubes = new KeyBinding(MOD_ID + ".debugcubes").setDefault(InputDevice.keyboard, Keyboard.KEY_L);

	@Override
	public void beforeGameStart() {
	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}

	@Override
	public void beforeClientStart() {

	}

	@Override
	public void afterClientStart() {
		GameSettings settings = Minecraft.getMinecraft(Minecraft.class).gameSettings;
		showDebugCubes = new BooleanOption(settings, "showDebugCubes", false);
		optionsPage = new OptionsPage(MOD_ID+".options", Item.wandMonsterSpawner.getDefaultStack());
		OptionsPages.register(optionsPage);

		optionsPage.withComponent(
			new OptionsCategory(MOD_ID + ".category").withComponent(new KeyBindingComponent(debugCubes))
		);
	}
}
