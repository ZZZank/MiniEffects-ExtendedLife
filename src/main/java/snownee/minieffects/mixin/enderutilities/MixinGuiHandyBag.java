package snownee.minieffects.mixin.enderutilities;

import fi.dy.masa.enderutilities.gui.client.GuiHandyBag;
import fi.dy.masa.enderutilities.gui.client.base.GuiContainerLargeStacks;
import lombok.val;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.minieffects.IAreasGetter;
import snownee.minieffects.handlers.InjectedMiniEffects;

import java.awt.*;
import java.util.List;

/**
 * @author ZZZank
 */
@Mixin(GuiHandyBag.class)
public abstract class MixinGuiHandyBag extends GuiContainerLargeStacks implements IAreasGetter {
    @Unique
    private final InjectedMiniEffects mini$eff = new InjectedMiniEffects(this);

    private MixinGuiHandyBag() {
        super(null ,0, 0, null);
    }

    @Inject(method = "initGui()V", at = @At("TAIL"))
    public void miniEffects$init(CallbackInfo ci) {
        mini$eff.updateArea(guiLeft, guiTop);
    }

    @Inject(method = "drawActivePotionEffects", at = @At("HEAD"), cancellable = true, remap = false)
    private void miniEff$drawEffects(CallbackInfo ci) {
        val replaced = mini$eff.defaultAction(this.guiLeft - 124, this.guiTop);
        if (replaced) {
            ci.cancel();
        }
    }

    @Override
    public List<Rectangle> miniEff$getAreas() {
        return mini$eff.miniEff$getAreas();
    }
}
