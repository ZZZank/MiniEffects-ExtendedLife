package snownee.minieffects.handlers;

import lombok.val;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import snownee.minieffects.mixin.minecraft.AccessLivingEntity;

/**
 * @author ZZZank
 */
public class MiniEffectsRenderer {

    public static void renderMini(GuiContainer screen, int x, int y, ItemStack iconItem, int effectsTotal, int effectsBad) {
        val mc = screen.mc;
        mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        screen.drawTexturedModalRect(x, y, 141, 166, 24, 24);

        int color = mc.player.getDataManager().get(AccessLivingEntity.getParameter());
        iconItem.getTagCompound().setInteger("CustomPotionColor", color);
        mc.getRenderItem().renderItemAndEffectIntoGUI(iconItem, x + 3, y + 4);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 200);
        x += 22;
        y += 14;
        if (effectsTotal - effectsBad > 0) {
            val s = Integer.toString(effectsTotal - effectsBad);
            mc.fontRenderer.drawStringWithShadow(s, x - mc.fontRenderer.getStringWidth(s), y, 16777215);
            y -= 10;
        }
        if (effectsBad > 0) {
            val s = Integer.toString(effectsBad);
            mc.fontRenderer.drawStringWithShadow(s, x - mc.fontRenderer.getStringWidth(s), y, 16733525);
        }
        GlStateManager.popMatrix();
    }
}
