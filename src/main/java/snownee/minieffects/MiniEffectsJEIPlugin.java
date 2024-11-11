package snownee.minieffects;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.inventory.GuiContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * @author ZZZank
 */
@JEIPlugin
public class MiniEffectsJEIPlugin implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        registry.addAdvancedGuiHandlers(new MiniEffectsAreaProvider());
    }

    public static final class MiniEffectsAreaProvider implements IAdvancedGuiHandler<GuiContainer> {

        @Override
        @Nonnull
        public Class<GuiContainer> getGuiContainerClass() {
            return GuiContainer.class;
        }

        @Nullable
        @Override
        public List<Rectangle> getGuiExtraAreas(@Nonnull GuiContainer gui) {
            return gui instanceof IAreasGetter
                ? ((IAreasGetter) gui).miniEff$getAreas()
                : Collections.emptyList();
        }
    }
}
