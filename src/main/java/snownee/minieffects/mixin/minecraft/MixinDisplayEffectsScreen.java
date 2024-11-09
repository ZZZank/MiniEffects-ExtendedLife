package snownee.minieffects.mixin.minecraft;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.List;

import lombok.val;
import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.nbt.NBTTagCompound;
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
        mini$effects.icon.setTagCompound(new NBTTagCompound());
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
    private void miniEffects$render(CallbackInfo ci, int capturedEffectLeft, int capturedEffectTop) {
        val effectsTotalOld = mini$effects.effectsTotal;

        val updated = mini$effects.updateEffectCounter(mc.player.getActivePotionEffects());
        if (!updated) {
            return;
        }

        if (mini$effects.effectsTotal != effectsTotalOld) {
            if (mini$effects.effectsTotal == 0 || mini$effects.expanded) {
                mini$effects.updateArea(capturedEffectLeft, capturedEffectTop);
            }
        }

        val scaledresolution = new ScaledResolution(mc);
        val scaledWidth = scaledresolution.getScaledWidth();
        val scaledHeight = scaledresolution.getScaledHeight();
        val x = Mouse.getX() * scaledWidth / mc.displayWidth;
        val y = scaledHeight - Mouse.getY() * scaledHeight / mc.displayHeight - 1;

        boolean shouldExpand = mini$effects.shouldExpand(x, y);
        if (this.mini$effects.expanded != shouldExpand) {
            this.mini$effects.expanded = shouldExpand;
            mini$effects.updateArea(capturedEffectLeft, capturedEffectTop);
        }

        if (mini$effects.effectsTotal <= 0 || shouldExpand) {
            return; //continue normal (expand) drawing
        }
        mini$effects.renderMini();
        ci.cancel();
    }

    @Override
    public List<Rectangle> getAreas() {
        return mini$effects.effectsTotal == 0 ? Collections.emptyList()
            : Collections.singletonList(mini$effects.expanded ? mini$effects.expandedArea : mini$effects.iconArea);
    }
}
