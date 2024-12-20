package snownee.minieffects;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import snownee.minieffects.handlers.MiniEffectsEventHandlers;

@Mod(
	modid = MiniEffectsInfo.MOD_ID,
	name = MiniEffectsInfo.MOD_NAME,
	version = MiniEffectsInfo.VERSION,
	acceptedMinecraftVersions = "[1.12, 1.13)",
	clientSideOnly = true
)
public class MiniEffects {
	public static final String ID = MiniEffectsInfo.MOD_ID;

	public MiniEffects() {
		if (FMLCommonHandler.instance().getSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(MiniEffectsEventHandlers.class);
		}
	}
}
