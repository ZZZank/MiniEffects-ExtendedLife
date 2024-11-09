package snownee.minieffects.handlers;

import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import snownee.minieffects.MiniEffectsConfig;

import java.awt.*;
import java.util.Collection;

/**
 * @author ZZZank
 */
public class InjectedMiniEffects {

    public final Rectangle iconArea = new Rectangle();
    public final Rectangle expandedArea = new Rectangle();
    public final GuiContainer screen;
    public final ItemStack icon = new ItemStack(Items.POTIONITEM);

    public boolean expanded;
    public int effectsTotal;
    public int effectsBad;

    public InjectedMiniEffects(GuiContainer screen) {
        this.screen = screen;
    }

    /**
     * @return {@code true} if updated, otherwise (aka {@code effects} is empty) false
     */
    public boolean updateEffectCounter(Collection<PotionEffect> effects) {
        if (effects.isEmpty()) {
            return false;
        }
        for (val effect : effects) {
            val potion = effect.getPotion();
            if (potion.shouldRender(effect)) {
                ++effectsTotal;
                if (!potion.isBeneficial()) {
                    ++effectsBad;
                }
            }
        }
        return true;
    }

    public boolean shouldExpand(int x, int y) {
        boolean shouldExpand = iconArea.contains(x, y);
        if (this.expanded) { //prevent shrinking if in expanded area and currently expanded
            shouldExpand = shouldExpand || expandedArea.contains(x, y);
        }
        return shouldExpand;
    }

    public void renderMini() {
        MiniEffectsRenderer.renderMini(
            screen,
            expanded ? expandedArea.x : iconArea.x,
            expanded ? expandedArea.y : iconArea.y,
            icon,
            this.effectsTotal,
            this.effectsBad
        );
    }

    public void updateArea(int capturedLeft, int capturedTop) {
        if (expanded) {
            expandedArea.setBounds(
                capturedLeft,
                capturedTop,
                119,
                33 * Math.min(5, effectsTotal)
            );
        } else {
            iconArea.setBounds(
                //not capturedLeft because if `capturedLeft` is tweaked, it's supposed to be
                //targeting "expanded" situation
                screen.getGuiLeft() - 25 + MiniEffectsConfig.xOffset,
                screen.getGuiTop() + MiniEffectsConfig.yOffset,
                25,
                25
            );
        }
    }

    public boolean shouldExpand(Minecraft mc, int mouseX, int mouseY) {
        val scaledResolution = new ScaledResolution(mc);
        val scaledWidth = scaledResolution.getScaledWidth();
        val scaledHeight = scaledResolution.getScaledHeight();
        val x = mouseX * scaledWidth / mc.displayWidth;
        val y = scaledHeight - mouseY * scaledHeight / mc.displayHeight - 1;
        return shouldExpand(x, y);
    }
}
