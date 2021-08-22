package io.github.nickacpt.lightcraft.launcher;

import org.spongepowered.asm.util.Constants;

public class MinecraftLaunchHelper {
    public static String getMinecraftMainClass() {
        return System.getProperty("lightcraft.launch.main");
    }

    public static String getMixinSide() {
        return System.getProperty("lightcraft.launch.mixin.side", Constants.SIDE_UNKNOWN);
    }
}
