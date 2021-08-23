/*
 * This file is part of Mixin, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.asm.launch;

import io.github.nickacpt.lightcraft.launcher.MinecraftLaunchHelper;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.platform.CommandLineOptions;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.service.MixinServiceAbstract;

/**
 * TweakClass for running mixins in production. Being a tweaker ensures that we
 * get injected into the AppClassLoader but does mean that we will need to
 * inject the FML coremod by hand if running under FML.
 */
public class LightCraftMixinTweaker implements ITweaker {
    
    /**
     * Hello world
     */
    public LightCraftMixinTweaker() {
        setMixinSide();
        MixinBootstrap.start();
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.launchwrapper.ITweaker#acceptOptions(java.util.List,
     *      java.io.File, java.io.File, java.lang.String)
     */
    @Override
    public final void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        MixinBootstrap.doInit(CommandLineOptions.ofArgs(args));
    }

    /* (non-Javadoc)
     * @see net.minecraft.launchwrapper.ITweaker#injectIntoClassLoader(
     *      net.minecraft.launchwrapper.LaunchClassLoader)
     */
    @Override
    public final void injectIntoClassLoader(LaunchClassLoader classLoader) {
        MixinBootstrap.inject();
    }

    private void setMixinSide() {
        // Set Environment side to the one specified by MinecraftLaunchHelper#getMixinSide()
        IMixinService service = MixinService.getService();
        if (service instanceof MixinServiceAbstract) {
            try {
                Field sideNameField = service.getClass().getSuperclass().getDeclaredField("sideName");
                sideNameField.setAccessible(true);
                sideNameField.set(service, MinecraftLaunchHelper.getMixinSide());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /* (non-Javadoc)
     * @see net.minecraft.launchwrapper.ITweaker#getLaunchTarget()
     */
    @Override
    public String getLaunchTarget() {
        return MinecraftLaunchHelper.getMinecraftMainClass();
    }

    /* (non-Javadoc)
     * @see net.minecraft.launchwrapper.ITweaker#getLaunchArguments()
     */
    @Override
    public String[] getLaunchArguments() {
        return new String[]{};
    }
    
}