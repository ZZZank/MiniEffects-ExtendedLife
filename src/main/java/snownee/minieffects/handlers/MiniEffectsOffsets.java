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
public final class MiniEffectsOffsets {

    private static Map<Class<?>, Vec2i> OFFSETS;
    private static Vec2i DEFAULT;
    private static int timeStamp = 0;

    public static void refreshFromConfig() {
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
                val c = Class.forName(split[0]);
                val x = Integer.parseInt(split[1]);
                val y = Integer.parseInt(split[2]);
                tmp.put(c, new Vec2i(x, y));
            } catch (ClassNotFoundException e) {
                MiniEffects.LOGGER.error("error happened when processing config entry '{}', skipping", entry);
            }
        }
        OFFSETS = ImmutableMap.copyOf(tmp);
        DEFAULT = new Vec2i(MiniEffectsConfig.xOffset, MiniEffectsConfig.yOffset);
        timeStamp++;
    }

    public static Vec2i get(Class<?> type) {
        if (type == null) {
            return null;
        }
        return OFFSETS.get(type);
    }

    public static Vec2i getOrDefault(Class<?> type) {
        val got = get(type);
        return got == null ? DEFAULT : got;
    }

    public static Vec2i getDefault() {
        return MiniEffectsOffsets.DEFAULT;
    }

    public static int timeStamp() {
        return MiniEffectsOffsets.timeStamp;
    }

    @AllArgsConstructor
    public static class Vec2i {
        public final int x;
        public final int y;
    }
}
