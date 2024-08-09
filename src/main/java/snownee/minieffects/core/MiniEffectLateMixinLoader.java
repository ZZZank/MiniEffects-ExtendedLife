package snownee.minieffects.core;

import snownee.minieffects.MiniEffectsInfo;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Collections;
import java.util.List;

/**
 * @author ZZZank
 */
public class MiniEffectLateMixinLoader implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList(MiniEffectsInfo.MIXIN_CONFIG_MOD);
    }
}
