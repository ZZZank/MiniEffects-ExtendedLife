package snownee.minieffects.handlers;

import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Mouse;
import snownee.minieffects.IAreasGetter;
import snownee.minieffects.MiniEffectsConfig;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
        this.icon.setTagCompound(new NBTTagCompound());
    }

    public boolean defaultAction(int capturedLeft, int capturedTop) {
        val effectsTotalOld = this.effectsTotal;
        val mc = this.screen.mc;

        val updated = this.updateEffectCounter(mc.player.getActivePotionEffects());
        if (!updated) {
            return false;
        }

        if (this.effectsTotal != effectsTotalOld) {
            if (this.effectsTotal == 0 || this.expanded) {
                this.updateArea(capturedLeft, capturedTop);
            }
        }

        val shouldExpand = this.shouldExpand(mc, Mouse.getX(), Mouse.getY());
        if (this.expanded != shouldExpand) {
            this.expanded = shouldExpand;
            this.updateArea(capturedLeft, capturedTop);
        }

        if (this.effectsTotal <= 0 || shouldExpand) {
            return false; //continue normal (expand) drawing
        }
        this.renderMini();
        return true;
    }

    /**
     * @return {@code true} if updated, otherwise (aka {@code effects} is empty) false
     */
    public boolean updateEffectCounter(Collection<PotionEffect> effects) {
        if (effects.isEmpty()) {
            return false;
        }
        this.effectsTotal = 0;
        this.effectsBad = 0;
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
        if (expanded) {
            return;
        }
        MiniEffectsRenderer.renderMini(
            screen,
            iconArea.x,
            iconArea.y,
            icon,
            this.effectsTotal,
            this.effectsBad
        );
    }

    public void updateArea(int capturedLeft, int capturedTop) {
        if (expanded) {
            updateExpanded(capturedLeft, capturedTop);
        } else {
            //not capturedLeft because if `capturedLeft` is tweaked, it's supposed to be
            //targeting "expanded" situation
            updateFolded(
                this.screen.getGuiLeft() - 25 + MiniEffectsConfig.xOffset,
                this.screen.getGuiTop() + MiniEffectsConfig.yOffset
            );
        }
    }

    protected void updateFolded(int x, int y) {
        iconArea.setBounds(x, y, 25, 25);
    }

    protected void updateExpanded(int x, int y) {
        expandedArea.setBounds(x, y, 119, 33 * Math.min(5, effectsTotal));
    }

    public boolean shouldExpand(Minecraft mc, int mouseX, int mouseY) {
        val scaledResolution = new ScaledResolution(mc);
        val scaledWidth = scaledResolution.getScaledWidth();
        val scaledHeight = scaledResolution.getScaledHeight();
        val x = mouseX * scaledWidth / mc.displayWidth;
        val y = scaledHeight - mouseY * scaledHeight / mc.displayHeight - 1;
        return shouldExpand(x, y);
    }

    public List<Rectangle> miniEff$getAreas() {
        return effectsTotal == 0
            ? Collections.emptyList()
            : Collections.singletonList(expanded ? expandedArea : iconArea);
    }

    /**
     * folded position tweaked for effects rendering on the right side of GUI
     */
    public static class RightPin extends InjectedMiniEffects {
        public RightPin(GuiContainer screen) {
            super(screen);
        }

        @Override
        public void updateArea(int capturedLeft, int capturedTop) {
            if (expanded) {
                updateExpanded(capturedLeft, capturedTop);
            } else {
                updateFolded(capturedLeft + MiniEffectsConfig.xOffset, capturedTop + MiniEffectsConfig.yOffset);
            }
        }
    }
}
