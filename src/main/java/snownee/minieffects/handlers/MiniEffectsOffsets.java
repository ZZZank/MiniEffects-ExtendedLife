package snownee.minieffects.handlers;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.val;
import snownee.minieffects.MiniEffects;
import snownee.minieffects.MiniEffectsConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZZZank
 */
@UtilityClass
public class MiniEffectsOffsets {

    private Map<Class<?>, Vec2i> OFFSETS;
    private Vec2i DEFAULT;
    private int timeStamp = 0;
    private boolean refreshed = false;

    public void markConfigChanged() {
        refreshed = false;
    }

    public void refreshFromConfig() {
        val tmp = new HashMap<Class<?>, Vec2i>();
        for (val entry : MiniEffectsConfig.offsetPerScreen) {
            val split = entry.split(";");
            if (split.length != 3) {
                MiniEffects.LOGGER.error(
                    "expected config entry to be in 'type;x;y' format, but got '{}', skipping",
                    entry
                );
                continue;
            }
            try {
                val c = Class.forName(split[0], false, MiniEffectsOffsets.class.getClassLoader());
                val x = Integer.parseInt(split[1]);
                val y = Integer.parseInt(split[2]);
                tmp.put(c, new Vec2i(x, y));
            } catch (ClassNotFoundException e) {
                MiniEffects.LOGGER.error("class '{}' not found, skipping config entry '{}'", split[0], entry);
            } catch (Exception e) {
                MiniEffects.LOGGER.error("error happened when processing config entry '{}', skipping", entry, e);
            }
        }
        OFFSETS = ImmutableMap.copyOf(tmp);
        DEFAULT = new Vec2i(MiniEffectsConfig.xOffset, MiniEffectsConfig.yOffset);
        timeStamp++;
    }

    public Vec2i get(Class<?> type) {
        if (!refreshed) {
            refreshFromConfig();
            refreshed = true;
        }
        if (type == null) {
            return null;
        }
        return OFFSETS.get(type);
    }

    public Vec2i getOrDefault(Class<?> type) {
        val got = get(type);
        return got == null ? DEFAULT : got;
    }

    public Vec2i getDefault() {
        return MiniEffectsOffsets.DEFAULT;
    }

    public int timeStamp() {
        return MiniEffectsOffsets.timeStamp;
    }

    @AllArgsConstructor
    public static class Vec2i {
        public final int x;
        public final int y;
    }
}
