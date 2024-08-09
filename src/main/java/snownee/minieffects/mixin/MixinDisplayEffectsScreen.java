package snownee.minieffects.mixin;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.List;

import net.minecraft.inventory.Container;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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
    private boolean expand;
    @Unique
    private int effects;
    @Unique
    private Rectangle area;
    @Unique
    private ItemStack iconItem = new ItemStack(Items.POTIONITEM);

    private MixinDisplayEffectsScreen(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "initGui", at = @At("TAIL"))
    public void minimaleffects_init(CallbackInfo ci) {
        iconItem.setTagCompound(new NBTTagCompound());
        updateArea();
    }

    @Inject(method = "drawActivePotionEffects", cancellable = true, at = @At("HEAD"))
    private void minimaleffects_renderEffects(CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        int effects = 0, bad = 0;
        for (PotionEffect effectInstance : mc.player.getActivePotionEffects()) {
            if (effectInstance.getPotion().shouldRender(effectInstance)) {
                ++effects;
                if (!effectInstance.getPotion().isBeneficial()) {
                    ++bad;
                }
            }
        }

        if (this.effects != effects) {
            this.effects = effects;
            if (effects == 0 || expand) {
                updateArea();
            }
        }
        final ScaledResolution scaledresolution = new ScaledResolution(mc);
        int i1 = scaledresolution.getScaledWidth();
        int j1 = scaledresolution.getScaledHeight();
        int x = Mouse.getX() * i1 / mc.displayWidth;
        int y = j1 - Mouse.getY() * j1 / mc.displayHeight - 1;
        boolean expand = area.contains(x, y);
        if (expand != this.expand) {
            this.expand = expand;
            updateArea();
        }

        if (effects <= 0 || expand) {
			return;
        }
        mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        x = area.x;
        y = area.y;
        this.drawTexturedModalRect(x, y, 141, 166, 24, 24);
        EntityPlayerSP player = mc.player;
        int color = player.getDataManager().get(MixinLivingEntity.getParameter());
        iconItem.getTagCompound().setInteger("CustomPotionColor", color);
        mc.getRenderItem().renderItemAndEffectIntoGUI(iconItem, x + 3, y + 4);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 200);
        x += 22;
        y += 14;
        if (effects - bad > 0) {
            String s = Integer.toString(effects - bad);
            mc.fontRenderer.drawStringWithShadow(s, x - mc.fontRenderer.getStringWidth(s), y, 16777215);
            y -= 10;
        }
        if (bad > 0) {
            String s = Integer.toString(bad);
            mc.fontRenderer.drawStringWithShadow(s, x - mc.fontRenderer.getStringWidth(s), y, 16733525);
        }
        GlStateManager.popMatrix();
        ci.cancel();
    }

    @Unique
    private void updateArea() {
        int left = this.getGuiLeft();
        int top = this.getGuiTop();
        if (expand) {
            int height = effects > 5 ? 165 : 33 * effects;
            area = new Rectangle(left - 124, top, 119, height);
        } else {
            area = new Rectangle(left - 25 + MiniEffectsConfig.xOffset, top + MiniEffectsConfig.yOffset, 20, 20);
        }
    }

    @Override
    public List<Rectangle> getAreas() {
        return area == null || effects == 0
            ? Collections.emptyList()
            : Collections.singletonList(area);
    }
}
