package snownee.minieffects.mixin.minecraft;

import lombok.val;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import snownee.minieffects.api.InjectedMiniEffectsHolder;
import snownee.minieffects.handlers.InjectedMiniEffects;

@Mixin(InventoryEffectRenderer.class)
public abstract class MixinDisplayEffectsScreen extends GuiContainer implements InjectedMiniEffectsHolder {

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
    public InjectedMiniEffects miniEff$getInjected() {
        return mini$effects;
    }
}
