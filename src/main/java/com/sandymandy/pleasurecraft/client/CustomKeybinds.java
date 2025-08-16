package com.sandymandy.pleasurecraft.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CustomKeybinds {
    public static KeyBinding thrustKey;
    public static KeyBinding cumKey;

    public static void register() {
        thrustKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pleasurecraft.thrust", // translation key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_SHIFT, // default key
                "key.categories.pleasurecraft" // translation category
        ));

        cumKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pleasurecraft.cum",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_SPACE,
                "key.categories.pleasurecraft"
        ));


    }
}
