package snownee.minieffects.api;

import snownee.minieffects.handlers.InjectedMiniEffects;

import java.awt.*;
import java.util.List;

/**
 * @author ZZZank
 */
public interface InjectedMiniEffectsHolder extends IAreasGetter {

    InjectedMiniEffects miniEff$getInjected();

    @Override
    default List<Rectangle> miniEff$getAreas() {
        return miniEff$getInjected().miniEff$getAreas();
    }
}
