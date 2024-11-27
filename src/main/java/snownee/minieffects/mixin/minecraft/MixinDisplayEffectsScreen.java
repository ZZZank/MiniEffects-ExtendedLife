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
        val replaced = mini$effects.defaultAction(capturedLeft, capturedTop);
        if (replaced) {
            ci.cancel();
        }
    }

    @Override
    public List<Rectangle> miniEff$getAreas() {
        return mini$effects.miniEff$getAreas();
    }
}
