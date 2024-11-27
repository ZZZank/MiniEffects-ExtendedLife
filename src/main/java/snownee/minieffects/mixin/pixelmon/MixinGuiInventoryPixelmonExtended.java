package snownee.minieffects.mixin.pixelmon;

import com.pixelmonmod.pixelmon.client.gui.inventory.GuiInventoryPixelmonExtended;
import lombok.val;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.minieffects.handlers.InjectedMiniEffects;

/**
 * @author ZZZank
 */
@Mixin(GuiInventoryPixelmonExtended.class)
public abstract class MixinGuiInventoryPixelmonExtended extends GuiInventory {

    @Unique
    private final InjectedMiniEffects mini$eff = new InjectedMiniEffects(this);

    @Inject(method = "initGui()V", at = @At("TAIL"))
    public void miniEffects$init(CallbackInfo ci) {
        mini$eff.updateArea(guiLeft, guiTop);
    }

    @Inject(method = "drawActivePotionEffects", at = @At("HEAD"), cancellable = true, remap = false)
    private void miniEff$drawEffects(CallbackInfo ci) {
        val replaced = mini$eff.defaultAction(this.guiLeft + 196, this.guiTop + 1);
        if (replaced) {
            ci.cancel();
        }
    }

    private MixinGuiInventoryPixelmonExtended() {
        super(null);
    }
}
