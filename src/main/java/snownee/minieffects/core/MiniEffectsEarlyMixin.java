package snownee.minieffects.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import snownee.minieffects.MiniEffectsInfo;
import zone.rong.mixinbooter.IEarlyMixinLoader;

public class MiniEffectsEarlyMixin implements IEarlyMixinLoader, IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

	@Override
	public List<String> getMixinConfigs() {
		return Collections.singletonList(MiniEffectsInfo.MIXIN_CONFIG);
	}
}
