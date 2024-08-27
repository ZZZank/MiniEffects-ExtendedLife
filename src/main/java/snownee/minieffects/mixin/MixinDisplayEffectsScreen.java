package snownee.minieffects.mixin;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.List;

import net.minecraft.potion.Potion;
import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.minieffects.IAreasGetter;
import snownee.minieffects.MiniEffectsConfig;

@Mixin(InventoryEffectRenderer.class)
public abstract class MixinDisplayEffectsScreen extends GuiContainer implements IAreasGetter {

    @Unique
    private final Rectangle miniEff$iconArea = new Rectangle();
    @Unique
    private final Rectangle miniEff$expandedArea = new Rectangle();
    @Unique
    private boolean miniEff$expand;
    @Unique
    private int miniEff$effects;
    @Unique
    private final ItemStack miniEff$iconItem = new ItemStack(Items.POTIONITEM);

    private MixinDisplayEffectsScreen() {
        super(null);
    }

    @Inject(method = "initGui", at = @At("TAIL"))
    public void miniEffects$init(CallbackInfo ci) {
        miniEff$iconItem.setTagCompound(new NBTTagCompound());
        miniEffects$updateArea();
    }

    @Inject(method = "drawActivePotionEffects", cancellable = true, at = @At("HEAD"))
    private void miniEffects$render(CallbackInfo ci) {

        int effects = 0, bad = 0;
        for (PotionEffect effect : mc.player.getActivePotionEffects()) {
            Potion potion = effect.getPotion();
            if (potion.shouldRender(effect)) {
                ++effects;
                if (!potion.isBeneficial()) {
                    ++bad;
                }
            }
        }

        if (this.miniEff$effects != effects) {
            this.miniEff$effects = effects;
            if (effects == 0 || miniEff$expand) {
                miniEffects$updateArea();
            }
        }

        final ScaledResolution scaledresolution = new ScaledResolution(mc);
        final int scaledWidth = scaledresolution.getScaledWidth();
        final int scaledHeight = scaledresolution.getScaledHeight();
        final int x = Mouse.getX() * scaledWidth / mc.displayWidth;
        final int y = scaledHeight - Mouse.getY() * scaledHeight / mc.displayHeight - 1;

        boolean shouldExpand = miniEff$iconArea.contains(x, y);
        if (this.miniEff$expand) { //prevent shrinking if in expanded area and currently expanded
            shouldExpand |= miniEff$expandedArea.contains(x, y);
        }
        if (shouldExpand != this.miniEff$expand) {
            this.miniEff$expand = shouldExpand;
            miniEffects$updateArea();
        }

        if (effects <= 0 || shouldExpand) {
            return;
        }
        miniEffects$renderUnexpanded(effects, bad);
        ci.cancel();
    }

    @Unique
    private void miniEffects$renderUnexpanded(int effectsTotal, int effectsBad) {
        mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int x = miniEff$expand ? miniEff$expandedArea.x : miniEff$iconArea.x;
        int y = miniEff$expand ? miniEff$expandedArea.y : miniEff$iconArea.y;
        this.drawTexturedModalRect(x, y, 141, 166, 24, 24);

        int color = mc.player.getDataManager().get(AccessLivingEntity.getParameter());
        miniEff$iconItem.getTagCompound().setInteger("CustomPotionColor", color);
        mc.getRenderItem().renderItemAndEffectIntoGUI(miniEff$iconItem, x + 3, y + 4);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 200);
        x += 22;
        y += 14;
        if (effectsTotal - effectsBad > 0) {
            String s = Integer.toString(effectsTotal - effectsBad);
            mc.fontRenderer.drawStringWithShadow(s, x - mc.fontRenderer.getStringWidth(s), y, 16777215);
            y -= 10;
        }
        if (effectsBad > 0) {
            String s = Integer.toString(effectsBad);
            mc.fontRenderer.drawStringWithShadow(s, x - mc.fontRenderer.getStringWidth(s), y, 16733525);
        }
        GlStateManager.popMatrix();
    }

    @Unique
    private void miniEffects$updateArea() {
        int left = this.getGuiLeft();
        int top = this.getGuiTop();
        if (miniEff$expand) {
            miniEff$expandedArea.setBounds(left - 124, top, 119, Math.min(33 * 5, 33 * miniEff$effects));
        } else {
            miniEff$iconArea.setBounds(left - 25 + MiniEffectsConfig.xOffset, top + MiniEffectsConfig.yOffset, 20, 20);
        }
    }

    @Override
    public List<Rectangle> getAreas() {
        return miniEff$effects == 0 ? Collections.emptyList()
            : Collections.singletonList(miniEff$expand ? miniEff$expandedArea : miniEff$iconArea);
    }
}
