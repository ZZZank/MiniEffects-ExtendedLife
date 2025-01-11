package snownee.minieffects;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
	modid = MiniEffectsInfo.MOD_ID,
	name = MiniEffectsInfo.MOD_NAME,
	version = MiniEffectsInfo.VERSION,
	acceptedMinecraftVersions = "[1.12, 1.13)",
	clientSideOnly = true
)
public class MiniEffects {
	public static final String ID = MiniEffectsInfo.MOD_ID;
	public static final Logger LOGGER = LogManager.getLogger(ID);

	public MiniEffects() {
	}
}
