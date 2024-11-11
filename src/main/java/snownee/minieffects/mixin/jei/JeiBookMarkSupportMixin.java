package snownee.minieffects.mixin.jei;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @see snownee.minieffects.MiniEffectsJEIPlugin.MiniEffectsAreaProvider
 */
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
		ci.setReturnValue(Collections.emptyList());
	}
}