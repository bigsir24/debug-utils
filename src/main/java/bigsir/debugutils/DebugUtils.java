package bigsir.debugutils;

import bigsir.debugutils.interfaces.INameable;
import bigsir.debugutils.utils.ColorHelper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.options.components.*;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.input.InputDevice;
import net.minecraft.client.option.*;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.model.Cube;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.item.Item;
import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
	public static BooleanOption showCubeNames;
	public static BooleanOption colorMode;
	public static RangeOption namesInRow;
	public static KeyBinding debugCubes;
	public static Map<Class<? extends Entity>, List<String>> cubeNameMap = new HashMap<>();

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
					.withComponent(new BooleanOptionComponent(disableShadows))
					.withComponent(new BooleanOptionComponent(showCubeNames))
					.withComponent(new ToggleableOptionComponent<>(namesInRow))
					.withComponent(new BooleanOptionComponent(colorMode)));

		nameInit();
		ColorHelper.initHSB();

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
		showCubeNames = new BooleanOption(settings, "showCubeNames", false);
		namesInRow = new RangeOption(settings, "namesInRow", 1, 8);
		colorMode = new BooleanOption(settings, "colorMode", false);
	}

	private void nameInit(){
		//Iterate through entity classes
		int n = 0;
		for (Class<? extends Entity> clazz : EntityDispatcher.classToKeyMap.keySet()){

			//List for Field names to be linked to entity class
			List<String> nameList = new ArrayList<>();
			EntityRenderer<?> entityRenderer = EntityRenderDispatcher.instance.getRenderer(clazz);
			Field[] entityRendererFields = entityRenderer.getClass().getFields();

			//Iterate through fields of assigned entity renderer
			for (Field field : entityRendererFields){
				try {
					Object checkedObject = field.get(entityRenderer);
					if(checkedObject instanceof ModelBase) {
						//modelsInRenderer.add((ModelBase) checkedObject);
						Field[] modelFields = checkedObject.getClass().getFields();

						//Iterate through fields of entity model
						for (Field field1 : modelFields){
							Object checkedObject1 = field1.get(checkedObject);

							//If cube add to list and set Cube name
							if(checkedObject1 instanceof Cube) {
								nameList.add(field1.getName());
								((INameable)checkedObject1).debug_utils$setName(field1.getName());
								n++;
							}
						}
					}
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}

			if(!nameList.isEmpty())cubeNameMap.put(clazz, nameList);
		}
		LOGGER.info("Loaded " + n + " part names");
	}
}
