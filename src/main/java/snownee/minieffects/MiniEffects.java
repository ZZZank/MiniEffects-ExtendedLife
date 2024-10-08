package snownee.minieffects;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(
	modid = MiniEffectsInfo.MOD_ID, name = MiniEffectsInfo.MOD_NAME, version = MiniEffectsInfo.VERSION, acceptedMinecraftVersions = "[1.12, 1.13)", clientSideOnly = true
)
public class MiniEffects {
	public static final String ID = MiniEffectsInfo.MOD_ID;

	public MiniEffects() {
		if (FMLCommonHandler.instance().getSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(this);
			MinecraftForge.EVENT_BUS.register(MiniEffectsConfig.class);
		}
	}

	@SubscribeEvent
	public void onPotionShift(GuiScreenEvent.PotionShiftEvent event) {
		event.setCanceled(true);
	}
}
