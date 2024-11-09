package snownee.minieffects.handlers;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import snownee.minieffects.MiniEffectsConfig;

import java.awt.*;

/**
 * @author ZZZank
 */
public class InjectedMiniEffects {

    public final Rectangle iconArea = new Rectangle();
    public final Rectangle expandedArea = new Rectangle();
    public final GuiContainer screen;
    public final ItemStack icon = new ItemStack(Items.POTIONITEM);

    public boolean expanded;
    public int effectsCount;

    public InjectedMiniEffects(GuiContainer screen) {
        this.screen = screen;
    }

    public boolean shouldExpand(int x, int y) {
        boolean shouldExpand = iconArea.contains(x, y);
        if (this.expanded) { //prevent shrinking if in expanded area and currently expanded
            shouldExpand = shouldExpand || expandedArea.contains(x, y);
        }
        return shouldExpand;
    }

    public void renderMini(int effectsTotal, int effectsBad) {
        MiniEffectsRenderer.renderMini(
            screen,
            expanded ? expandedArea.x : iconArea.x,
            expanded ? expandedArea.y : iconArea.y,
            icon,
            effectsTotal,
            effectsBad
        );
    }

    public void updateArea(int capturedLeft, int capturedTop) {
        if (expanded) {
            expandedArea.setBounds(
                capturedLeft,
                capturedTop,
                119,
                33 * Math.min(5, effectsCount)
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
}
