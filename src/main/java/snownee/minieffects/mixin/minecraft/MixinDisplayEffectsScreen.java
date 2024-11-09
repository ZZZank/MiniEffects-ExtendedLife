package snownee.minieffects.mixin.minecraft;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.List;

import lombok.val;
import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
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

    @Inject(method = "initGui()V", at = @At("TAIL"))
    public void miniEffects$init(CallbackInfo ci) {
        miniEff$iconItem.setTagCompound(new NBTTagCompound());
        miniEffects$updateArea(guiLeft, guiTop);
    }

    @Inject(
        method = "drawActivePotionEffects",
        cancellable = true,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/entity/EntityPlayerSP;getActivePotionEffects()Ljava/util/Collection;"
        ),
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void miniEffects$render(CallbackInfo ci, int capturedEffectLeft, int capturedEffectTop) {
        val effects = mc.player.getActivePotionEffects();
        if (effects.isEmpty()) {
            return;
        }

        int effectsTotal = 0, effectsBad = 0;
        for (val effect : effects) {
            val potion = effect.getPotion();
            if (potion.shouldRender(effect)) {
                ++effectsTotal;
                if (!potion.isBeneficial()) {
                    ++effectsBad;
                }
            }
        }

        if (this.miniEff$effects != effectsTotal) {
            this.miniEff$effects = effectsTotal;
            if (effectsTotal == 0 || miniEff$expand) {
                miniEffects$updateArea(capturedEffectLeft, capturedEffectTop);
            }
        }

        val scaledresolution = new ScaledResolution(mc);
        val scaledWidth = scaledresolution.getScaledWidth();
        val scaledHeight = scaledresolution.getScaledHeight();
        val x = Mouse.getX() * scaledWidth / mc.displayWidth;
        val y = scaledHeight - Mouse.getY() * scaledHeight / mc.displayHeight - 1;

        boolean shouldExpand = miniEff$iconArea.contains(x, y);
        if (this.miniEff$expand) { //prevent shrinking if in expanded area and currently expanded
            shouldExpand = shouldExpand || miniEff$expandedArea.contains(x, y);
        }
        if (this.miniEff$expand != shouldExpand) {
            this.miniEff$expand = shouldExpand;
            miniEffects$updateArea(capturedEffectLeft, capturedEffectTop);
        }

        if (effectsTotal <= 0 || shouldExpand) {
            return; //continue normal (expand) drawing
        }
        miniEffects$renderUnexpanded(effectsTotal, effectsBad);
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

    @Unique
    private void miniEffects$updateArea(int capturedLeft, int capturedTop) {
        if (miniEff$expand) {
            miniEff$expandedArea.setBounds(
                capturedLeft,
                capturedTop,
                119,
                33 * Math.min(5, miniEff$effects)
            );
        } else {
            miniEff$iconArea.setBounds(
                //not capturedLeft because if `capturedLeft` is tweaked, it's supposed to be
                //targeting "expanded" situation
                guiLeft - 25 + MiniEffectsConfig.xOffset,
                guiTop + MiniEffectsConfig.yOffset,
                25,
                25
            );
        }
    }

    @Override
    public List<Rectangle> getAreas() {
        return miniEff$effects == 0 ? Collections.emptyList()
            : Collections.singletonList(miniEff$expand ? miniEff$expandedArea : miniEff$iconArea);
    }
}
