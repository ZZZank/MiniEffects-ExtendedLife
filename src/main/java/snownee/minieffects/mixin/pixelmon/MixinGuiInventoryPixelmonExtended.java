package snownee.minieffects.mixin.pixelmon;

import com.pixelmonmod.pixelmon.client.gui.inventory.GuiInventoryPixelmonExtended;
import lombok.val;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.minieffects.api.IAreasGetter;
import snownee.minieffects.handlers.InjectedMiniEffects;

import java.awt.*;
import java.util.List;

/**
 * @see GuiInventoryPixelmonExtended#drawActivePotionEffects()
 * @author ZZZank
 */
@Mixin(GuiInventoryPixelmonExtended.class)
public abstract class MixinGuiInventoryPixelmonExtended extends GuiInventory implements IAreasGetter {

    @Unique
    private final InjectedMiniEffects mini$eff = new InjectedMiniEffects.RightPin(this);

    @Inject(method = "initGui()V", at = @At("TAIL"))
    public void miniEffects$init(CallbackInfo ci) {
        mini$eff.updateArea(guiLeft + 177, guiTop + 1);
    }

    @Inject(method = "drawActivePotionEffects", at = @At("HEAD"), cancellable = true)
    private void miniEff$drawEffects(CallbackInfo ci) {
        val replaced = mini$eff.defaultAction(this.guiLeft + 177, this.guiTop + 1);
        if (replaced) {
            ci.cancel();
        }
    }

    @Override
    public List<Rectangle> miniEff$getAreas() {
        return mini$eff.miniEff$getAreas();
    }

    private MixinGuiInventoryPixelmonExtended() {
        super(null);
    }
}
