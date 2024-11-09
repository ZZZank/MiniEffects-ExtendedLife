package snownee.minieffects.mixin.jei;

import java.awt.*;
import java.util.List;

import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.minieffects.IAreasGetter;

@Mixin(targets = "mezz.jei.plugins.vanilla.InventoryEffectRendererGuiHandler", remap = false)
@Pseudo
public class JeiBookMarkSupportMixin {

	@Inject(
		method = "getGuiExtraAreas(Lnet/minecraft/client/renderer/InventoryEffectRenderer;)Ljava/util/List;",
		at = @At("HEAD"),
		cancellable = true,
		require = 0
	)
	public void getGuiExtraAreas(InventoryEffectRenderer gui, CallbackInfoReturnable<List<Rectangle>> ci) {
		if (gui instanceof IAreasGetter) {
			ci.setReturnValue(((IAreasGetter) gui).getAreas());
		}
	}
}