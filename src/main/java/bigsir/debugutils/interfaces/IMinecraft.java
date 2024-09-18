package bigsir.debugutils.interfaces;

import net.minecraft.core.Timer;

public interface IMinecraft {
	default Timer debug_utils$getTimer(){
		return null;
	}
}
