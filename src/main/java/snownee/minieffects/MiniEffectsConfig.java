package snownee.minieffects;

import lombok.experimental.UtilityClass;
import net.minecraftforge.common.config.Config;

@UtilityClass
@Config(modid = MiniEffects.ID)
public class MiniEffectsConfig {

	@Config.Comment("default x offset of effect icon rendering position")
	public int xOffset = 0;
	@Config.Comment("default y offset of effect icon rendering position")
	public int yOffset = 0;
	@Config.Comment({
		"Screen specific x/y offset of effect icon rendering position",
		"each entry should be in 'type;x;y' format",
		"let's say if you want y offset to be 20 when in creative inventory, then 'type' should be the class name of creative menu",
		"the whole entry will then be 'net.minecraft.client.gui.inventory.GuiContainerCreative;0;20'"
	})
	public String[] offsetPerScreen = new String[0];
}