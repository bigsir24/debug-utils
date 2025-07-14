package bigsir.debugutils;

import bigsir.debugutils.commands.CommandRename;
import bigsir.debugutils.commands.CommandTick;
import bigsir.debugutils.commands.CommandTriggerEvent;
import bigsir.debugutils.interfaces.INameable;
import bigsir.debugutils.utils.ColorHelper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.model.Cube;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.item.Item;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.util.collection.NamespaceID;
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
	public static Map<Class<? extends Entity>, List<String>> cubeNameMap = new HashMap<>();
	public static boolean frozen = false;
	public static boolean wasFrozen = false;
	public static float bypassTicks = 0;
	public static int allowedTicks = 0;
	//public static boolean createItems = false;

	public static Item TOOL_DEBUG;

	@Override
	public void beforeGameStart() {
		//createDebugItems(20000);
	}

	@Override
	public void afterGameStart() {
		nameInit();
		ColorHelper.initHSB();

		CommandManager.registerCommand(new CommandTick());
		CommandManager.registerCommand(new CommandTriggerEvent());
		CommandManager.registerCommand(new CommandRename());
	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}

	/*public static void createDebugItems(int startID) {
		createItems = true;

		ItemBuilder builder = new ItemBuilder(MOD_ID).setStackSize(1);

		TOOL_DEBUG = builder.build(new ItemDebug(getID("item/tool_debug"), startID++));
	}*/

	public static NamespaceID getID(String value) {
		return NamespaceID.getPermanent(MOD_ID, value);
	}

	public static boolean isNotTicking(){
		return DebugUtils.frozen && DebugUtils.allowedTicks <= 0;
	}

	public static float chooseTick(float partialTick){
		return isNotTicking() ? bypassTicks : partialTick;
	}

	private void nameInit(){
		//Iterate through entity classes
		int n = 0;
		for (Class<? extends Entity> clazz : EntityDispatcher.classToIdMap.keySet()){

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
