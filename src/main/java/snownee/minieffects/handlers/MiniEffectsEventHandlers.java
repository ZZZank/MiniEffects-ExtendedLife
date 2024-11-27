package snownee.minieffects.handlers;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import snownee.minieffects.MiniEffects;

/**
 * @author ZZZank
 */
@Config(modid = MiniEffects.ID)
public class MiniEffectsEventHandlers {

    @SubscribeEvent
    public static void onConfigReload(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MiniEffects.ID)) {
            ConfigManager.sync(MiniEffects.ID, Config.Type.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void onPotionShift(GuiScreenEvent.PotionShiftEvent event) {
        event.setCanceled(true);
    }
}
