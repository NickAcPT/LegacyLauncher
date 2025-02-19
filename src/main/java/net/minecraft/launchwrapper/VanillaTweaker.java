package net.minecraft.launchwrapper;

import io.github.nickacpt.lightcraft.launcher.MinecraftLaunchHelper;
import java.io.File;
import java.util.List;

public class VanillaTweaker implements ITweaker {
    private List<String> args;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.args = args;
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        classLoader.registerTransformer("net.minecraft.launchwrapper.injector.VanillaTweakInjector");
    }

    @Override
    public String getLaunchTarget() {
        return MinecraftLaunchHelper.getMinecraftMainClass();
    }

    @Override
    public String[] getLaunchArguments() {
        return args.toArray(new String[0]);
    }
}
