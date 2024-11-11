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

    @Inject(method = "drawActivePotionEffects", at = @At("HEAD"), cancellable = true, remap = false)
    private void miniEff$drawEffects(CallbackInfo ci) {
        val effectsTotalOld = mini$eff.effectsTotal;

        val updated = mini$eff.updateEffectCounter(mc.player.getActivePotionEffects());
        if (!updated) {
            return;
        }

        val capturedLeft = this.guiLeft - 124;
        val capturedTop = this.guiTop;
        if (mini$eff.effectsTotal != effectsTotalOld) {
            if (mini$eff.effectsTotal == 0 || mini$eff.expanded) {
                mini$eff.updateArea(capturedLeft, capturedTop);
            }
        }

        val shouldExpand = mini$eff.shouldExpand(mc, Mouse.getX(), Mouse.getY());
        if (this.mini$eff.expanded != shouldExpand) {
            this.mini$eff.expanded = shouldExpand;
            mini$eff.updateArea(capturedLeft, capturedTop);
        }

        if (mini$eff.effectsTotal <= 0 || shouldExpand) {
            return; //continue normal (expand) drawing
        }
        mini$eff.renderMini();
        ci.cancel();
    }

    @Override
    public List<Rectangle> miniEff$getAreas() {
        return mini$eff.miniEff$getAreas();
    }
}
