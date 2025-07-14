package bigsir.debugutils;

import net.minecraft.client.gui.options.components.BooleanOptionComponent;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.components.ToggleableOptionComponent;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.input.InputDevice;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.OptionBoolean;
import net.minecraft.client.option.OptionRange;
import net.minecraft.core.item.Items;
import org.lwjgl.input.Keyboard;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.OptionsInitEntrypoint;

public class DebugOptions implements OptionsInitEntrypoint {
	public static OptionBoolean showDebugCubes;
	public static OptionRange lineWidth;
	public static OptionRange cornerPointSize;
	public static OptionRange rotationPointSize;
	public static OptionRange lineOpacity;
	public static OptionBoolean disableShadows;
	public static OptionBoolean showCubeNames;
	public static OptionBoolean colorRotationPoint;
	public static OptionBoolean colorMode;
	public static OptionRange namesInRow;
	public static KeyBinding debugCubes;

	@Override
	public void initOptions(GameSettings settings) {
		debugCubes = new KeyBinding(DebugUtils.MOD_ID + ".debugcubes").setDefault(InputDevice.keyboard, Keyboard.KEY_L);
		showDebugCubes = new OptionBoolean(settings, "showDebugCubes", false);
		lineWidth = new OptionRange(settings, "debugCubeLineWidth", 4, 10);
		cornerPointSize = new OptionRange(settings, "cornerPointSize", 9, 20);
		rotationPointSize = new OptionRange(settings, "rotationPointSize", 9, 20);
		lineOpacity = new OptionRange(settings, "debugCubeLineOpacity", 50, 101);
		disableShadows  = new OptionBoolean(settings, "disableShadows", false);
		showCubeNames = new OptionBoolean(settings, "showCubeNames", false);
		namesInRow = new OptionRange(settings, "namesInRow", 1, 8);
		colorMode = new OptionBoolean(settings, "colorMode", false);
		colorRotationPoint = new OptionBoolean(settings, "colorRotation", true);
	}

	public static void initOptionsPages() {
		String MOD_ID = DebugUtils.MOD_ID;

		OptionsPages.register(new OptionsPage(MOD_ID + ".options", Items.WAND_MONSTER_SPAWNER.getDefaultStack()))
			.withComponent(
				new OptionsCategory(MOD_ID + ".category.keybinds").withComponent(new KeyBindingComponent(debugCubes)))
			.withComponent(
				new OptionsCategory(MOD_ID + ".category.visuals")
					.withComponent(new ToggleableOptionComponent<>(lineWidth))
					.withComponent(new ToggleableOptionComponent<>(cornerPointSize))
					.withComponent(new ToggleableOptionComponent<>(rotationPointSize))
					.withComponent(new BooleanOptionComponent(colorRotationPoint))
					.withComponent(new ToggleableOptionComponent<>(lineOpacity))
					.withComponent(new BooleanOptionComponent(disableShadows))
					.withComponent(new BooleanOptionComponent(showCubeNames))
					.withComponent(new ToggleableOptionComponent<>(namesInRow))
					.withComponent(new BooleanOptionComponent(colorMode)));
	}
}
