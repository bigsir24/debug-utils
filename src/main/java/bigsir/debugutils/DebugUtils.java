package bigsir.debugutils;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.options.components.*;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPageRegistry;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.input.InputDevice;
import net.minecraft.client.option.*;
import net.minecraft.core.item.Item;
import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class DebugUtils implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "debugutils";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        LOGGER.info("Debug Utils initialized.");
    }

	public static OptionsPage optionsPage;
	public static BooleanOption showDebugCubes;
	public static RangeOption lineWidth;
	public static RangeOption cornerPointSize;
	public static RangeOption rotationPointSize;
	public static RangeOption lineOpacity;
	public static BooleanOption disableShadows;
	public static KeyBinding debugCubes;

	@Override
	public void beforeGameStart() {
	}

	@Override
	public void afterGameStart() {
		optionsPage = new OptionsPage(MOD_ID+".options", Item.wandMonsterSpawner.getDefaultStack());
		OptionsPages.register(optionsPage);

		optionsPage
			.withComponent(
				new OptionsCategory(MOD_ID + ".category.keybinds").withComponent(new KeyBindingComponent(debugCubes)))
			.withComponent(
				new OptionsCategory(MOD_ID + ".category.visuals")
					.withComponent(new ToggleableOptionComponent<>(lineWidth))
					.withComponent(new ToggleableOptionComponent<>(cornerPointSize))
					.withComponent(new ToggleableOptionComponent<>(rotationPointSize))
					.withComponent(new ToggleableOptionComponent<>(lineOpacity))
					.withComponent(new BooleanOptionComponent(disableShadows)));
	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}

	public static void optionsInit(GameSettings settings){
		debugCubes = new KeyBinding(MOD_ID + ".debugcubes").setDefault(InputDevice.keyboard, Keyboard.KEY_L);
		showDebugCubes = new BooleanOption(settings, "showDebugCubes", false);
		lineWidth = new RangeOption(settings, "debugCubeLineWidth", 4, 10);
		cornerPointSize = new RangeOption(settings, "cornerPointSize", 9, 20);
		rotationPointSize = new RangeOption(settings, "rotationPointSize", 9, 20);
		lineOpacity = new RangeOption(settings, "debugCubeLineOpacity", 50, 101);
		disableShadows  = new BooleanOption(settings, "disableShadows", false);
	}
}
