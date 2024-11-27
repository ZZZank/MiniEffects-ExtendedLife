package snownee.minieffects.mixin;

import net.minecraftforge.fml.common.Loader;
import snownee.minieffects.MiniEffectsInfo;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZZank
 */
public class MiniEffectsLateMixin implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        return Arrays.stream(MiniEffectsInfo.MIXIN_CONFIGS.split(" "))
            .filter(Loader::isModLoaded)
            .map(id -> String.format(MiniEffectsInfo.MIXIN_CONFIG_TEMPLATE, id))
            .collect(Collectors.toList());
    }
}
