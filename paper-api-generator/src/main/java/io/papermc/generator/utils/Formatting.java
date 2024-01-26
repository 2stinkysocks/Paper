package io.papermc.generator.utils;

import java.util.Locale;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

public final class Formatting {

    public static String formatKeyAsField(Key key) {
        return formatPathAsField(key.value());
    }

    public static String formatPathAsField(String path) {
        return path.toUpperCase(Locale.ENGLISH).replaceAll("[.-/]", "_"); // replace invalid field name chars
    }

    public static String formatTagFieldPrefix(String name, ResourceKey<? extends Registry<?>> registryKey) {
        if (registryKey == Registries.BLOCK) {
            return "";
        }
        if (registryKey == Registries.GAME_EVENT) {
            return "GAME_EVENT_"; // Paper doesn't follow the format (should be GAME_EVENTS_)
        }
        return name.toUpperCase(Locale.ENGLISH) + "_";
    }

    public static String formatFeatureFlagSet(FeatureFlagSet featureFlagSet) {
        for (FeatureFlag flag : FeatureFlags.REGISTRY.names.values()) {
            if (flag == FeatureFlags.VANILLA) {
                continue;
            }
            if (featureFlagSet.contains(flag)) {
                return formatFeatureFlag(flag);
            }
        }
        return "";
    }

    public static String formatFeatureFlag(FeatureFlag featureFlag) {
        Set<ResourceLocation> names = FeatureFlags.REGISTRY.toNames(FeatureFlagSet.of(featureFlag));
        String name = names.iterator().next().getPath(); // eww
        return formatFeatureFlagName(name);
    }

    public static String formatFeatureFlagName(String name) {
        int updateIndex = name.indexOf("update_");
        if (updateIndex == 0) {
            return "update %s".formatted(name.substring(updateIndex + "update_".length()).replace('_', '.'));
        }
        return name.replace('_', ' ') + " feature";
    }

    public static String formatTagKey(String tagDir, String resourcePath) {
        int tagsIndex = resourcePath.indexOf(tagDir);
        int dotIndex = resourcePath.lastIndexOf('.');
        if (tagsIndex == -1 || dotIndex == -1) {
            return "none";
        }
        return resourcePath.substring(tagsIndex + tagDir.length() + 1, dotIndex); // tags/registry_key/[tagKey].json
    }

    private Formatting() {
    }
}
