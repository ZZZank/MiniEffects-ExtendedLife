package snownee.minieffects.mixin.minecraft;

import java.awt.Rectangle;
import java.util.List;

import lombok.val;
import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import snownee.minieffects.IAreasGetter;
import snownee.minieffects.handlers.InjectedMiniEffects;

@Mixin(InventoryEffectRenderer.class)
public abstract class MixinDisplayEffectsScreen extends GuiContainer implements IAreasGetter {

    @Unique
    private final InjectedMiniEffects mini$effects = new InjectedMiniEffects(this);

    private MixinDisplayEffectsScreen() {
        super(null);
    }

    @Inject(method = "initGui()V", at = @At("TAIL"))
    public void miniEffects$init(CallbackInfo ci) {
        mini$effects.updateArea(guiLeft, guiTop);
    }

    @Inject(
        method = "drawActivePotionEffects",
        cancellable = true,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/entity/EntityPlayerSP;getActivePotionEffects()Ljava/util/Collection;"
        ),
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void miniEffects$render(CallbackInfo ci, int capturedLeft, int capturedTop) {
        val effectsTotalOld = mini$effects.effectsTotal;

        val updated = mini$effects.updateEffectCounter(mc.player.getActivePotionEffects());
        if (!updated) {
            return;
        }

        if (mini$effects.effectsTotal != effectsTotalOld) {
            if (mini$effects.effectsTotal == 0 || mini$effects.expanded) {
                mini$effects.updateArea(capturedLeft, capturedTop);
            }
        }

        val shouldExpand = mini$effects.shouldExpand(mc, Mouse.getX(), Mouse.getY());
        if (this.mini$effects.expanded != shouldExpand) {
            this.mini$effects.expanded = shouldExpand;
            mini$effects.updateArea(capturedLeft, capturedTop);
        }

        if (mini$effects.effectsTotal <= 0 || shouldExpand) {
            return; //continue normal (expand) drawing
        }
        mini$effects.renderMini();
        ci.cancel();
    }

    @Override
    public List<Rectangle> miniEff$getAreas() {
        return mini$effects.miniEff$getAreas();
    }
}
