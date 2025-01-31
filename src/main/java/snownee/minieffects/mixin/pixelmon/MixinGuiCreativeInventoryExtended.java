package snownee.minieffects.mixin.pixelmon;

import com.pixelmonmod.pixelmon.client.gui.inventory.GuiCreativeInventoryExtended;
import lombok.val;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.minieffects.api.InjectedMiniEffectsHolder;
import snownee.minieffects.handlers.InjectedMiniEffects;

/**
 * @author ZZZank
 * @see GuiCreativeInventoryExtended#drawActivePotionEffects()
 */
@Mixin(GuiCreativeInventoryExtended.class)
public abstract class MixinGuiCreativeInventoryExtended extends GuiContainerCreative
    implements InjectedMiniEffectsHolder {

    @Unique
    private final InjectedMiniEffects mini$eff = new InjectedMiniEffects(this);

    @Inject(method = "initGui()V", at = @At("TAIL"))
    public void miniEffects$init(CallbackInfo ci) {
        mini$eff.updateArea(guiLeft + 196, guiTop + 1);
    }

    @Inject(method = "drawActivePotionEffects", at = @At("HEAD"), cancellable = true)
    private void miniEff$drawEffects(CallbackInfo ci) {
        val replaced = mini$eff.defaultAction(this.guiLeft + 196, this.guiTop + 1);
        if (replaced) {
            ci.cancel();
        }
    }

    @Override
    public InjectedMiniEffects miniEff$getInjected() {
        return mini$eff;
    }

    private MixinGuiCreativeInventoryExtended() {
        super(null);
    }
}
