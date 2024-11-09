package snownee.minieffects.mixin.enderutilities;

import fi.dy.masa.enderutilities.gui.client.GuiHandyBag;
import fi.dy.masa.enderutilities.gui.client.base.GuiContainerLargeStacks;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author ZZZank
 */
@Mixin(GuiHandyBag.class)
public abstract class MixinGuiHandyBag extends GuiContainerLargeStacks {

    private MixinGuiHandyBag() {
        super(null ,0, 0, null);
    }
}
