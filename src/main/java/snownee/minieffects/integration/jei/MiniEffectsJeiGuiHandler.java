package snownee.minieffects.integration.jei;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import snownee.minieffects.IAreasGetter;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

/**
 * @author ZZZank
 */
public class MiniEffectsJeiGuiHandler implements IAdvancedGuiHandler<InventoryEffectRenderer> {
    @Override
    public Class<InventoryEffectRenderer> getGuiContainerClass() {
        return InventoryEffectRenderer.class;
    }

    @Nullable
    @Override
    public List<Rectangle> getGuiExtraAreas(InventoryEffectRenderer gui) {
        return ((IAreasGetter) gui).getAreas();
    }
}
