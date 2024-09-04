package bigsir.debugutils.utils;

import bigsir.debugutils.DebugUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ColorHelper {

	public static Map<String, Color> stringToHSBColorMap = new HashMap<>();
	public static Map<String, Color> stringToRGBColorMap = new HashMap<>();

	public static Color getColor(String name){
		if(name == null) return Color.white;
		if(!DebugUtils.colorMode.value) return stringToHSBColorMap.get(name);

		Random random = new Random();
		Color color;
		if(!stringToRGBColorMap.containsKey(name)) {
			color = getRandomColor();
			stringToRGBColorMap.put(name, color);
		}else{
			color = stringToRGBColorMap.get(name);
		}

		return color;
	}

	private static Color getRandomColor() {
		Random random = new Random();
		return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

	public static float getRed(String name){
		return getColor(name).getRed() / 255.0f;
	}
	public static float getGreen(String name){
		return getColor(name).getGreen() / 255.0f;
	}
	public static float getBlue(String name){
		return getColor(name).getBlue() / 255.0f;
	}

	public static void refreshColors(){
		if(!DebugUtils.colorMode.value) return;

        stringToRGBColorMap.replaceAll((k, v) -> getRandomColor());
	}

	public static void initHSB(){
		for(List<String> list : DebugUtils.cubeNameMap.values()){
			int step = list.size();
			int i = 0;
			for(String name : list){
				if(!stringToHSBColorMap.containsKey(name)){
					stringToHSBColorMap.put(name, new Color(Color.HSBtoRGB((float) i / step, 0.75f, 1.0f)));
					i++;
				}
			}
		}
	}
}
