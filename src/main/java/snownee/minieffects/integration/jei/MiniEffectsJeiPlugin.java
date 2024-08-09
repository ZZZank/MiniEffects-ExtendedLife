package snownee.minieffects.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

/**
 * @author ZZZank
 */
@JEIPlugin
public class MiniEffectsJeiPlugin implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        registry.addAdvancedGuiHandlers(new MiniEffectsJeiGuiHandler());
    }
}
