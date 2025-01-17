package snownee.minieffects.handlers;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import snownee.minieffects.MiniEffects;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

/**
 * @author ZZZank
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = MiniEffects.ID)
public class MiniEffectsEventHandlers {

    @SubscribeEvent
    public static void onConfigReload(@Nonnull ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MiniEffects.ID)) {
            ConfigManager.sync(MiniEffects.ID, Config.Type.INSTANCE);
            MiniEffectsOffsets.markConfigChanged();
        }
    }

    @SubscribeEvent
    public static void onPotionShift(@Nonnull GuiScreenEvent.PotionShiftEvent event) {
        event.setCanceled(true);
    }
}
